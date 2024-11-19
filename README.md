
# RentaMov : aplicacion alquiler de vehiculos

Esta aplicación web proporciona una plataforma intuitiva para el alquiler de vehículos. Emplea una arquitectura de microservicios para modularizar el desarrollo y facilitar la escalabilidad. Además, se integra con FIWARE para obtener datos de geolocalización en tiempo real y ofrecer funcionalidades avanzadas como seguimiento de vehículos y recomendaciones personalizadas.


## Authors

- [Alberto Avila](https://www.github.com/octokatherine)


## Tecnologías usadas


**Microservicios**: Desarrollados con Spring Framework para una modularidad y escalabilidad óptimas.

**Base de datos**: MySQL como sistema de gestión de bases de datos relacional.
Contenedores: Docker para la contenetización de los microservicios, facilitando la implementación y orquestación.

**Servidor de autenticación**: Keycloak para gestionar la autenticación y autorización de usuarios de forma segura.

## Despliegue

Requisitos previos:

**Docker**: Asegúrate de tener instalado Docker en tu sistema. Puedes descargarlo e instalarlo [en este enlace](https://www.docker.com)


**Git**: Necesitarás Git para clonar el repositorio.

### Pasos

1. **Clonar el repositorio**

```bash
  git clone https://github.com/datalbert/Trabajo-Fin-Grado.git
```

2. **Accede al directorio del proyecto**
```bash
   cd trabajo-fingrado
```

3. **Inicia docker**

4. **Ejecuta el contenedor del proyecto**
```bash
  cd docker-compose
  docker-compose up -d
```
Esto iniciara todos los servicios definidos en el docker-compose en segundo plano



## Demo

Insert gif or link to demo


## Lessons Learned

What did you learn while building this project? What challenges did you face and how did you overcome them?

