package com.example.powermeter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BTConnService {

    private static final String TAG = "BTConnectionServ";
    private static final String appName = "MYAPP";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter mBTAdapter;

    Context mContext;

    private  AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private  UUID deviceUUID;
    ProgressDialog mProcessDialog;

    public ConnectedThread mConnectedThead;
    public BTConnService(Context context) {
        mContext = context;
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBTAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
            }catch (Exception e) {
                Log.e(TAG,"Exception: " + e.getMessage());
            }
            Log.d(TAG,"AcceptThread from:" + MY_UUID_INSECURE);
            mServSocket = tmp;
        }

        @Override
        public void run() {
//            super.run();
            Log.d(TAG,"run: Accept running");
            BluetoothSocket socket = null;
            try {
                Log.d(TAG, "run: RFCON server socket start ......");
                socket = mServSocket.accept();
                Log.d(TAG, "run: RFCON server accepted connection......");
            }catch (Exception e) {
                Log.e(TAG,"Exception: " + e.getMessage());
            }

            if (socket != null){
                connected(socket,mmDevice);
            }
            Log.i(TAG,"End mAcceptThread");
        }

        public void cancel() {
            Log.i(TAG,"cancel: Cancelling  AcceptThread.");
            try {
                mServSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of connect socket failed" +  e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread{
        private  BluetoothSocket mmSocket;


        public ConnectThread(BluetoothDevice device , UUID uuid) {
            Log.d(TAG,"ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG,"ConnectThread: run");
            try {
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            }catch (Exception e){
                Log.e(TAG, "ConnectThread: connect createInsecureRfcommSocketToServiceRecord failed" +  e.getMessage());
            }
            mmSocket = tmp;
            mBTAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d(TAG,"ConnectThread: socket connected.");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG,"ConnectThread: socket closed.");
                } catch (IOException e2) {
                    Log.e(TAG, "ConnectThread: close socket failed" +  e2.getMessage());
                }
                Log.d(TAG,"ConnectThread: could not connect to UUID" + MY_UUID_INSECURE);
            }
            connected(mmSocket,mmDevice);
        }

        public void cancel() {
            Log.i(TAG,"cancel: Cancelling  AcceptThread.");
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of connect socket failed" +  e.getMessage());
            }
        }
    }


    public synchronized  void  start(){
        Log.d(TAG,"start");
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread=null;
        }
        if (mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }

    }

    public void startClient ( BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: started");
        mProcessDialog = ProgressDialog.show(mContext,"connecting bluetooth", "please wait...", true);
        mConnectThread = new ConnectThread(device,uuid);
        mConnectThread.start();
    }

    private class  ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: starting");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut =  null;

            mProcessDialog.dismiss();

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while (true){
                try {
                    bytes = mmInputStream.read(buffer);
                    String incomminMsg = new String(buffer,0,bytes);
                    Log.d(TAG, "InputStream: " + incomminMsg);
                } catch (IOException e) {
                    Log.e(TAG, "read: error reading InputStream" +  e.getMessage());
                    break;
                }

            }
        }
        public void write(byte[] bytes){
            String text  = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writting to outputstream " + text);
            try {
                mmOutputStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: OutputStream failed" +  e.getMessage());
            }
        }

        public void cancel(){
            try {
                mmSocket.close();
//                Log.d(TAG, "InputStream: " + incomminMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: starting");
        mConnectedThead = new ConnectedThread(mmSocket);
        mConnectedThead.start();
    }

    public void write(byte[] out){
        ConnectedThread r;
        Log.d(TAG, "write: called");
        mConnectedThead.write(out);

    }

}
