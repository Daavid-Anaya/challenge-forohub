<h1 align="center">Challenge ForoHub</h1>

<p align="center">
  <img src="https://img.shields.io/badge/STATUS-EN DESARROLLO-blue">
</p>

Con este desafío propuesto en la Formación de Java y Spring Boot, creando una API REST. Se busca poner en práctica conceptos avanzados de Java y Spring Boot, Como la creación de endpoints, implementación de capas de seguridad, entre otros.

---
## 💡 Sobre el Desafío 💡

El desafío **Foro Hub** consiste en replicar a nivel de Back-End el funcionamiento 
de un foro, construyendo una **API REST** con Spring Boot.

La API se centra específicamente en la gestión de tópicos, permitiendo a los usuarios:

- 📝 Crear un nuevo tópico
- 📋 Listar todos los tópicos disponibles
- 🔍 Consultar un tópico específico
- ✏️ Actualizar un tópico existente
- 🗑️ Eliminar un tópico

Implementando las mejores prácticas del modelo REST, incluyendo validaciones, 
autenticación/autorización y persistencia de datos.

#### 🎯OBJETIVO

Al final de nuestro desarrollo tendremos una API REST con las siguientes funcionalidades:

1. API con rutas implementadas siguiendo las mejores prácticas del modelo REST.
2. Validaciones realizadas según reglas de negocio.
3. Implementación de una base de datos para la persistencia de la información.
4. Servicio de autenticación/autorización para restringir el acceso a la información.

---
## ✨ Características Funcionalidades de la API

|     Recurso    | Crear | Listar todos | Ver uno | Actualizar | Eliminar |
|----------------|:-----:|:------------:|:-------:|:----------:|:--------:|
|  📌 Tópicos   |   ✅  |      ✅     |    ✅   |     ✅    |    ✅    |
| 💬 Respuestas |   ✅  |      ✅     |    ✅   |     ✅    |    ✅    |
|   📚 Cursos   |   ✅  |      ✅     |    ✅   |     ✅    |    ✅    |
|  👤 Usuarios  |   ✅  |      ✅     |    ✅   |     ✅    |    ✅    |
|  🔖 Perfiles  |   ✅  |      ✅     |    ✅   |     ✅    |    ✅    |

### 🔑 Resumen de permisos por perfil

#### Perfiles del sistema:

- **ROLE_ADMIN**
- **ROLE_USER**
- **ROLE_MODERADOR**

De acuerdo a los 3 perfiles del sistema (*ROLE_ADMIN, ROLE_USER, ROLE_MODERADOR*), la configuración de accesos es la siguiente:

|    Recurso    |   Acción  | ADMIN | MODERADOR | USER |
|---------------|-----------|-------|-----------|------|
|    `/auth`    |    POST   |  ✅  |     ✅    |  ✅  |
|  `/perfiles`  |    GET    |  ✅  |    ✅     |  ❌  |
|  `/perfiles`  | POST/PUT/DELETE | ✅ |    ❌    |  ❌  |
|   `/cursos`   |    GET    |  ✅  |     ✅    |  ✅  |
|   `/cursos`   | POST/PUT/DELETE | ✅ |    ❌    |  ❌  |
|  `/usuarios`  |    GET    |  ✅  |     ✅    |  ❌  |
|  `/usuarios`  |    PUT    |  ✅  |     ❌    |  ✅  |
|  `/usuarios`  |   DELETE  |  ✅  |     ❌    |  ❌  |
|  `/topicos`   |  GET/POST |  ✅  |     ✅    |  ✅  |
|  `/topicos`   |    PUT    |  ✅  |     ✅    |  ❌  |
|  `/topicos`   |   DELETE  |  ✅  |     ❌    |  ❌  |
| `/respuestas` | GET/POST  |  ✅  |     ✅    |  ✅  |
| `/respuestas` |    PUT    |  ✅  |     ✅    |  ✅  |
| `/respuestas` |   DELETE  |  ✅  |     ❌    |  ❌  |

---
## 🖥 Tecnologías utilizadas

<div align="center">
  
  |    Tecnología   |                           Descripción                          |                              Icon                               |
  | :-------------: | :------------------------------------------------------------: | :-------------------------------------------------------------: |
  |      Java       |                    Lenguaje de programación                    |   <img src="https://skillicons.dev/icons?i=java" width="48">    |
  |       Git       |                Sistema de control de versiones                 |    <img src="https://skillicons.dev/icons?i=git" width="48">    |
  |      Maven      |               gestión y construcción de proyecto               |   <img src="https://skillicons.dev/icons?i=maven" width="48">   |
  |   Spring Boot   |                           Framework                            |   <img src="https://skillicons.dev/icons?i=spring" width="48">  |
  |      MySQL      |          sistema de almacen y gestión de datos (DB)            |   <img src="https://skillicons.dev/icons?i=mysql" width="48">   |
  |    Insomnia     | Plataforma de desarrollo, diseño, depuración y pruebas de APIs |             <img src="" width="48" alt="Insomnia">              |
  
</div>

---
## 📂 Estructura del Proyecto

```
FORO-HUB/
├── .mvn/                          # Configuración del Maven Wrapper
├── .vscode/                       # Configuración del editor VS Code
└── src/
    └── main/
        ├── java/com/david/foro_hub/
        │   ├── controller/        # Controladores REST (endpoints de la API)
        │   ├── domain/            # Lógica de negocio y entidades del dominio
        │   │   ├── curso/         # Entidad y lógica relacionada a cursos
        │   │   ├── perfil/        # Entidad y lógica relacionada a perfiles de usuario
        │   │   ├── respuesta/     # Entidad y lógica de respuestas en el foro
        │   │   ├── topicos/       # Entidad y lógica de tópicos/hilos del foro
        │   │   └── usuario/       # Entidad y lógica de usuarios
        │   ├── infra/             # Infraestructura técnica transversal
        │   │   ├── exceptions/    # Manejo global de excepciones
        │   │   └── security/      # Configuración de seguridad (JWT, filtros, etc.)
        │   ├── repository/        # Repositorios JPA para acceso a base de datos
        │   └── ForoHubApplication.java  # Clase principal, punto de entrada
        └── resources/
            ├── db/migration/      # Scripts de migración con Flyway
            │   └── V1__create-data-base-forohub.sql
            ├── static/            # Recursos estáticos (si aplica)
            ├── templates/         # Plantillas (si aplica)
            └── application.properties  # Configuración de la aplicación
```

### 📦 Descripción de Paquetes

|       Paquete      |                                                           Descripción                                                             |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------|
|    `controller`    | Contiene los controladores REST que exponen los endpoints de la API. Reciben las solicitudes HTTP y delegan la lógica al dominio. |
|   `domain/curso`   |                   Gestiona la entidad `Curso`, incluyendo sus DTOs, validaciones y reglas de negocio asociadas.                   |
|   `domain/perfil`  |                       Gestiona la entidad `Perfil`, que define los roles o tipos de usuario dentro del sistema.                   |
| `domain/respuesta` |                Gestiona la entidad `Respuesta`, que representa los mensajes de reply dentro de un tópico del foro.                |
|  `domain/topicos`  |               Gestiona la entidad `Topico`, núcleo del foro: creación, actualización, cierre y eliminación de hilos.              |
|  `domain/usuario`  |                       Gestiona la entidad `Usuario`, incluyendo autenticación y datos del perfil de cuenta.                       |
| `infra/exceptions` |           Centraliza el manejo de errores mediante `@RestControllerAdvice`, devolviendo respuestas HTTP estandarizadas.           |
|  `infra/security`  |                     Configura Spring Security: autenticación JWT, filtros de seguridad y protección de rutas.                     |
|    `repository`    |         Interfaces que extienden `JpaRepository` para las operaciones CRUD y consultas personalizadas a la base de datos.         |
|   `db/migration`   |          Scripts SQL versionados gestionados por **Flyway** para el control de cambios en el esquema de la base de datos.         |

---
## 🖥 Screenshots

<table align="center">
  <tr>
    <td align="center"><img src="" alt="Crear tópico" width="450"><br><sub><b>Crear un nuevo tópico</b></sub></td>
    <td align="center"><img src="" alt="Mostrar los tópicos" width="450"><br><sub><b>Mostrar todos los tópicos creados</b></sub></td>
  </tr>
  <tr>
    <td align="center"><img src="" alt="Tópico específico" width="450"><br><sub><b>Mostrar un tópico específico</b></sub></td>
    <td align="center"><img src="" alt="Actualizar tópico" width="450"><br><sub><b>Actualizar un tópico</b></sub></td>
  </tr>
   <tr>
    <td align="center"><img src="" alt="Eliminar tópico" width="500"><br><sub><b>Eliminar un tópico</b></sub></td>
    <td align="center"><img src="" alt="" width="200"><br><sub><b></b></sub></td>
  </tr>
</table>

---
## ✅ Prerrequisitos

Antes de comenzar, asegúrate de tener instalado lo siguiente en tu entorno local:
1. **Java JDK 17 o superior** — Necesario para ejecutar la aplicación Spring Boot.
   - Verificar versión: `java -version`

2. **Maven** *(Opcional)* — Aunque el proyecto incluye el Maven Wrapper (`mvnw`),
   es recomendable tenerlo instalado globalmente.
   - Verificar versión: `mvn -version`

3. **MySQL** — Base de datos relacional necesaria para persistir los datos de la API.
   - Verificar versión: `mysql --version`

4. **IDE o Editor de Texto** — Se recomienda:
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
   - [Visual Studio Code](https://code.visualstudio.com/) con el *Extension Pack for Java*

5. **Git** — Para clonar el repositorio.
   - Verificar versión: `git --version`

### ⚙️ Configuración de Variables de Entorno

Antes de ejecutar el proyecto, define las siguientes variables de entorno.
Si no se definen, se usarán los valores por defecto indicados.

|    Variable   |                Descripción               |              Valor por defecto           |
|---------------|------------------------------------------|------------------------------------------|
|    `DB_URL`   |    URL de conexión a la base de datos    | `jdbc:mysql://localhost:3306/forohub_db` |
| `DB_USERNAME` |             Usuario de MySQL             |                     —                    |
| `DB_PASSWORD` |           Contraseña de MySQL            |                     —                    |
|  `JWT_SECRET` | Clave secreta para firmar los tokens JWT |                `12345678`                |

> ⚠️ **Importante:** Se recomienda definir siempre `DB_USERNAME`, `DB_PASSWORD`
> y `JWT_SECRET` como variables de entorno reales en producción,
> nunca dejar los valores por defecto expuestos.

---
## 🚀 Ejecución del Proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/foro-hub.git
cd foro-hub
```

### 2. Configurar las variables de entorno

Define las variables en tu sistema operativo o en tu IDE antes de ejecutar:

**Linux / macOS:**
```bash
export DB_URL=jdbc:mysql://localhost:3306/forohub_db
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_contraseña
export JWT_SECRET=tu_clave_secreta
```

**Windows (CMD):**
```cmd
set DB_URL=jdbc:mysql://localhost:3306/forohub_db
set DB_USERNAME=tu_usuario
set DB_PASSWORD=tu_contraseña
set JWT_SECRET=tu_clave_secreta
```

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/forohub_db"
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
$env:JWT_SECRET="tu_clave_secreta"
```

### 3. Crear la base de datos en MySQL
```sql
CREATE DATABASE forohub_db;
```

### 4. Ejecutar el proyecto

**Con Maven Wrapper (recomendado):**
```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Con Maven instalado globalmente:**
```bash
mvn spring-boot:run
```

### 5. Verificar que la aplicación está corriendo

Una vez iniciada, la API estará disponible en:
```
http://localhost:8080
```

> 💡 **Tip:** Puedes probar los endpoints usando [Postman](https://www.postman.com/)
> o [Insomnia](https://insomnia.rest/).
