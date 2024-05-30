
# Trabajo de Fin de Grado

La plataforma de alquiler de automóviles por horas/días es una aplicación web diseñada exclusivamente para facilitar el proceso de alquiler de automóviles de manera rápida y conveniente. Con esta aplicación, los usuarios pueden buscar, reservar y alquilar una amplia variedad de automóviles según sus necesidades y preferencias, todo ello con un enfoque exclusivo en el alquiler de vehículos de cuatro ruedas


## Herramientas Usadas

**Cliente:** Angular

**Servidor:** SpringBoot, Arquitectura de Microservicios

**IoT:** Fiware 

**Bases de datos:** Mysql, MongoDb




## Authors

- [Alberto Avila Fernandez](https://github.com/datalbert)

## Diseño de la Aplicación

La aplicación sigue una arquitectura de cliente-servidor, donde el cliente es una aplicación web y el servidor proporciona los servicios necesarios para el alquiler de vehículos. A continuación, se detalla la estructura y el diseño general de la aplicación:

### Arquitectura General

La aplicación sigue un patrón de arquitectura de tres capas:

1. **Capa de Presentación**: Esta capa consiste en la interfaz de usuario de la aplicación web, desarrollada utilizando Angular y Material-UI. Aquí se encuentran las diferentes vistas y componentes de la aplicación que permiten a los usuarios buscar, reservar y gestionar sus alquileres de vehículos.

2. **Capa de Lógica de Negocio**: En esta capa, se encuentran los controladores y servicios que gestionan la lógica de negocio de la aplicación, como la gestión de usuarios, la reserva de vehículos y la interacción con la base de datos. Estos componentes están desarrollados utilizando Node.js y Express.js.

3. **Capa de Acceso a Datos**: La capa más baja de la aplicación, que se encarga de interactuar con la base de datos MongoDB para almacenar y recuperar la información relacionada con los usuarios, los vehículos y las reservas.

### Patrones de Diseño Utilizados


### Escalabilidad y Mantenimiento

La arquitectura de la aplicación se ha diseñado teniendo en cuenta la escalabilidad y el mantenimiento a largo plazo. La separación de las diferentes capas y el uso de patrones de diseño facilitan la incorporación de nuevas funcionalidades y la modificación del código existente sin comprometer la integridad del sistema.
