# potentiometer

Un medidor de potencia de bajo coste que puede hacer uno mismo

## Getting Started

Este proyecto pretende ser una guía y marco de referencia para poder crear un medidor de potencia para cualquier bicicleta y o rodillo.
Las intrucciones que se detallarán a continuación estan dividida en 3 partes.

* Hardware (componentes, construcción y montaje)
* Software (instalación, configuración y calibración)
* Mejoras (siguientes pasos, desarrollo de hard y soft)

### Prerequisitos

Voy a exponer una solución completa con la utilización de componentes mínimos. En este punto daré un diagrama esquemático y general, pero mas adelante entraré en detalles de como funciona cada componente.

Básicamente lo que se necesita a nivel de hardware son tres cosas.
* Medidor de fuerza. (Para medir la fuerza que se aplica al pedal)
* Medidor de Cadencia. (Para medir la distancia que recorre el pedal)
* Conector bluetooth/wifi. (para enviar datos al movil o smartwatch)

## Building
### Fase 1 - Hardware
Para más detalles de como construir el hardware, conexiones y cómo funciona cada componente, ir al siguiente enlace
* [Wiki_Hardware](https://github.com/lasserfox/potentiometer/wiki/hardware) - The hardware and connections.

### Fase 2 - Software
Proyect structure:

```
+--backend
    +-- core (all backend code to manage esp32 modules)
    +-- test
+--frontend
    +-- mobile-app (app development)
    +-- web (web app - future)
    +-- test
+-- doc (documentation for all)

```


**TODO**

## Contribuciones

Esto esta recién comenzando y aún falta mucho por hacer. Toda ayuda, por poco que parezca es bienvenida.
Yo he puesto uns lista de cosas que me gustaría hacer, poned las que os interese y que creáis que sea técnicamente posible.

Por favor leer [CONTRIBUTING.md](https://github.com/lasserfox/potentiometer) para ver los detalles del código de conducta y cómo hacer los porcedimientos para pull requests, updates, etc.

## Autores

* **Unknown** - *Initial work* - [lasserfox](https://github.com/lasserfox)

See also the list of [contributors](https://github.com/lasserfox/potentiometer/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
