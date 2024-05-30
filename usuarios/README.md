
Readme del microservicio Gestión de usuario

El sistema de directorio es el siguiente

    -   controller
    -   dto: para aplicar el patrón DTO
    -   Audit: auditorio
    -   entity: con los objetos para persisitr en la base de datos
    -   exception: para tratar las excepciones y errores
    -   mapped: segun el patron dto mapped convierte las clases dto a entity y viceversa
    -   service

Ver como definir la base de datos

vamos a tener una única tabla en la base de datos con los siguientes campo

Tabla: Usuarios

ID (clave primaria)
Nombre
Apellidos
DNI
Email
Contraseña
Permiso_de_conducir (ruta o referencia a la imagen en tu sistema de archivos o una columna BLOB si lo estás almacenando directamente en la base de datos)
Teléfono_de_contacto


Para ver la documentacion de la api acceder a esta web http://localhost:8080/swagger-ui/index.html

