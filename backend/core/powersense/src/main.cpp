#include "BluetoothSerial.h"
#include <stdio.h>

#include <esp32-hal-adc.h>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
int fuerza = 36; // el pin de lectura

void setup()
{
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
  // pinMode(fuerza,INPUT);
  printf("Iniciando...");
}

void loop()
{
  // printf("leyendo analógico\n");
  analogReadResolution(12);
  analogSetAttenuation(ADC_11db);
  // printf("calculado potencia en watts\n");
  // printf("enviando potencia al dispositivo bluetooth\n");
  int random_number = rand() % 10 + 48;
  int resp = SerialBT.write(random_number);
  printf("rand: %c - ok: %d \n",random_number, resp);
  while (SerialBT.available())
  { 
    char lectura = SerialBT.read();
    Serial.print(lectura);
  }

  delay(2000);
}