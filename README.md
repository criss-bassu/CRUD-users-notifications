<h1 align="center"> Cloudpath</h1>
<p align="center"><img src="https://cdn-icons-png.flaticon.com/512/4984/4984903.png"/></p> 


# Tabla de Contenidos 

---


- [Tabla de contenidos:](#tabla-de-contenidos-)
- [Microservicios](#microservicios)
- [Gestión de Usuarios](#gestión-de-usuarios)
    - [Caracterícticas del Microservicio](#detalles-de-implementacion-del-microservicio-de-usuarios)
    - [Dependencias Extras](#dependencias-especiales-para-el-manejo-de-tokens-y-control-de-seguridad)
- [Gestión de Notificaciones](#gestión-de-notificaciones)
    - [Detalles del Microservicio](#detalles-de-implementacion-del-microservicio-de-notificaciones)
    - [Dependencias Extras](#dependencias-necesarias-para-el-envío-de-notificaciones)
- [Dependencias Globales](#dependencias-generales-del-proyecto)
- [Frontend de los Microservicios](#frontend-de-los-microservicios)
  - [Usuarios](#frontend-del-microservicio-de-usuarios)
  - [Notificaciones](#frontend-del-microservicio-de-notificaicones)
- [Autor/es](#autores)
- [Información adicional](#notas-para-la-corrección)

# Microservicios
## **Gestión de Usuarios**

---


### Detalles de Implementacion del Microservicio de Usuarios

El control de integridad de los token se lleva acabo con códigos HMAC de criptografía simétrica con una clava de 256 bits almacenada en el fichero "application.properties". Dicho código HMAC es es verificado en cada filtro, al pasar la cadena almacenada en la cabecera de la petición, a la capa de negoció de la aplicación, según se explica en el repositorio de [la librería jwt](https://github.com/jwtk/jjwt#jws-read)   

Aconticnuación se muestra la configuración de la seguridad de la aplicación 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
                .requestMatchers("/passwordreset", "/login")// La white list de la aplicación
                .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }


### Dependencias Especiales para el manejo de tokens y control de seguridad

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
    </dependency>

Este es el resultado de la inyección de dependencias en tiempo de ejecucion del microservicio 

![](/home/alejandro/Downloads/diagram.jpg)

## **Gestión de Notificaciones**

---

### Detalles de Implementacion del Microservicio de Notificaciones

Una de las tareas en este microservicio es la realización del envío de sms's y correos electrónicos (el delivery del mensaje corre acuenta de las herramientas usadas para el desarrollo - mailtrap y twilio). 

En primer lugar, para el envio de sms hemos usado el api de [twilio api](https://www.twilio.com/docs/sms/quickstart/java), para accer uso de este api es necesario estar registrado dentro de la aplicación y validar un número de teléfono. No sabemos porque, la plataforma suele dar problemas con la verificación de números de telefonos. En algunos casos no deja enviar sms a nnúmeros verificados, o se anuña la verificación del número de telefono. La implementación se ha llevado acabo tal cual se especifica en la documentación, aun así aquí se vé una captura con el log de la plataforma

![](./doc/twilio.png)


Y en segundo lugar, hemos usado un servicio de pruebas que ofrece un servidor smtp para testear el envío de correos. La implementación del envío de correos se ha hecho según se especifica en la documentación de [spring-boot-starter-mail](https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/mail.html) 

### Dependencias Necesarias para el envío de notificaciones

    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>9.3.0</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>

Este es el resultado de la inyección de dependencias en tiempo de ejecucion del microservicio

![](/home/alejandro/Pictures/DependenciasNotificaciones/diagram.png)

    
# Dependencias generales del proyecto

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web-services</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.modelmapper</groupId>
        <artifactId>modelmapper</artifactId>
        <version>3.1.1</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>

# Frontend de los Microservicios

Instalación de paquetes para las aaplicaciones angular:

```shell
npm install
npm install bootstrap@4.1.1
```

## Frontend del microservicio de usuarios

Para que los microservicios atiendan las peticiones, es necesario incluir en las cabeceras un token de acceso, de lo contrario siempre se devolverá un error 403. Es por ello, que para la realizacion de pruebas, se ha introducido una modificación en el microservicio de usuarios. Ahora cada vez que la aplicación se lanza, se crea y almacena un usuario "anonimous" en la base de datos (consultar fichero UserLoader.java del paquete components del microservicio), del que se usarán sus credenciales para el desarrollo de las aplicaciones angular.

```
mysql> select * from usuario;
+----+-----------+-----------+-------------+-------------------------+-----------+----------+----------+
| id | apellido1 | apellido2 | contrasenia | email                   | nombre    | telefono | token_id |
+----+-----------+-----------+-------------+-------------------------+-----------+----------+----------+
|  1 | NULL      | NULL      | reset       | anonimous@anonimous.com | anonimous | NULL     |        1 |
+----+-----------+-----------+-------------+-------------------------+-----------+----------+----------+
```

Es por esto, que siempre aparece este usuario en la aplicación de Frontend. Además, por el funcionamiento del filtro JWT del microservico, si se eimina este usuario, se dejara de poder operar con el backend. Ya que cada vez que se recibe un token, además de revisar de que este no ha sido alterado, también se comprueba que las credenciales del token estén registradas en la BD del microservicio. 
De esta forma si queremos operar con el microservicio de usuarios, sin hacer login, debemos hacer uso de las credenciales del usuario anonimous. El token de acceso se ha incluido en el fichero enviroment.ts de cada aplicación angular:

```
export const environment = {
  userAccesToken: "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJSRVNQT05TQUJMRSIsIlZJQ0VSUkVDVE9SQURPIl0sInN1YiI6ImFub25pbW91c0Bhbm9uaW1vdXMuY29tIn0.X6qTsCeEHCI0kfVhLSmOZmTs1TWnjLgQc3aV0BVCjP8",
  apiBaseUrl: "http://localhost:8081"
};
```

Dicho fichero se genera automáticamente a travé de la línea de comandos

```shell
ng generate enviroments
```

## Frontend del microservicio de notificaicones

Para facilitar el desarrollo del frontend del microservicio de notificaciones, hemos añadido un nuevo endpoint en el api que nos permita recopilar todas las notificaciones del microservicio

```
@GetMapping("/all")
public ResponseEntity<List<NotificacionDTO>> getAllNotifications() {
    var notificaciones = this.servicio.obtenerTodasNotificaciones().stream().map(x -> mapper.map(x, NotificacionDTO.class)).toList();
    return  ResponseEntity.ofNullable(notificaciones);
}
```

# Autor/es

---

* Cristina Barón Suarez
* Alejandro Téllez Montiel 
* Patricia Martín Castillo
* Juan Miguel Pedrosa Garrido
* Gonzalo Cuervo Campos

# Información adicional


---
Con respecto a la gestión de notificaciones, comenzamos con la implementación de un botón para ver la notificación seleccionada con su form (se ve como se estaba implementando)
pero tras el feedback del profesor (consideró que el mismo read de la notificación se haría implícitamente con la tabla que almacena todas las notificaciones del momento) se comentó y se optó por hacer un CUD.

En relación a los tests, los que aparecen en rojo son los que elevan las excepciones (invocan a estas pero no tenemos nada diseñado dentro). No ceñirse estrictamente al color del test si se usa un IDE, mirar la cobertura del código.


# Notas para la corrección

---


En el repositorio se encuentran dos carpetas que almacenan dos proyectos maven diferentes para cada uno de los microservicios. Al no existir archivos de configuración de algún IDE, la incorporación de los módulos y configuración del proyecto variará en función de la herramienta que se use. 

En nuestro caso todos hemos decidido usar IntelliJ IDEA, para el cuál basta con clonar y abrir el proyecto completo, una vez ahí se deben seguir los siguientes pasos:

1. File -> "Project Structure..." -> Aparecerá una ventana con el menú de configuración del proyecto
2. En "Project Settings" -> Modules -> Add Module (boton "+") -> Import Module
3. Dentro de la jerarquía de directorios -> Seleccionar fichero pom.xml de cada microservicio.

Base de datos de Usuario: http://localhost:3306/Usuarios <br>

Base de datos de Notificacion: http://localhost:3306/Notificaciones <br>

```sql
    -- Como root o dba
    Create Database Notificaciones;
```

```sql
    -- Como root o dba
    Create Database Usuarios;
```

```sql
    -- Como root o dba
    Create User sa Identified By sa;
```


```sql
    -- En caso de tener problemas para cargar esquemas
    Grant DBA To sa;
```
Usuario para ambas bases de datos: sa

Contraseña para ambas: sa

En la entrega anterior la generación de esquemas ddl de ls microservicios, fué implementada con anotaciones de spring que usaban librerias deprecadas que daban problemas en el arranque de la aplicación y en la carga del esquema de las base de datos. Por ello se han sustituido el tipo de base de datos usada por los microservicios, y los esquemas ddl de cada microservicio se muestran en el log de la aplicación gracias a las siguientes propiedades:

    spring.jpa.generate-ddl=true
    spring.jpa.hibernate.ddl-auto=create-drop
    spring.jpa.show-sql=true


Un detalle importante es que el backend de cada uno de los microservicioes están preparados para aceptar peticiones única y exclusivamente de una aplicación angular y de tipo REST (GET, POST, PUT, DELETE). Es decir que si se lanzan los dos microservicios a la vez, una de las dos aplicaciones angular, no conseguirá acceder al servidor, a causa de la asignación de un puerto aleatorio en colisión con la que se lanzó primero. Configuracíón CORS de los microservicios:

```
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        UrlBasedCorsConfigurationSource urlSourceCfg = new UrlBasedCorsConfigurationSource();
        urlSourceCfg.registerCorsConfiguration("/**", corsConfiguration);


        return urlSourceCfg;
    }
```
