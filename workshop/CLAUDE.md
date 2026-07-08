# CLAUDE.md

Este archivo proporciona instrucciones para Claude Code (claude.ai/code) al trabajar con el código de este repositorio.

## Descripción General del Proyecto

Este proyecto es una aplicación desarrollada con **Spring Boot 3.5.7** utilizando **Java 21**. Su objetivo es demostrar el uso del framework **Vaadin** para la interfaz de usuario junto con **jOOQ** para el acceso seguro y tipado a la base de datos **PostgreSQL**. Es un ejemplo utilizado en el taller **"AI Track and Field"**.

### Tecnologías Principales

- **Framework:** Spring Boot 3.5.7 con Spring Boot DevTools para recarga automática (Hot Reload).
- **Interfaz de Usuario:** Vaadin 24.9.3 (framework web Java del lado del servidor).
- **Base de Datos:** PostgreSQL con jOOQ para consultas SQL tipadas.
- **Migraciones de Base de Datos:** Flyway (las migraciones deben ubicarse en `src/main/resources/db/migration/`).
- **Pruebas:** Testcontainers, Karibu Testing (para pruebas de Vaadin) y Playwright para pruebas End-to-End (E2E).

---

# Compilación y Ejecución

## Desarrollo

Ejecutar la aplicación utilizando PostgreSQL dentro de Testcontainers:

```bash
./mvnw spring-boot:test-run
```

Este comando utiliza `TestApplication`, que configura automáticamente un contenedor PostgreSQL.

Ejecutar la aplicación de forma tradicional (requiere una instancia externa de PostgreSQL):

```bash
./mvnw spring-boot:run
```

---

## Pruebas

Ejecutar todas las pruebas:

```bash
./mvnw test
```

Ejecutar las pruebas mostrando información detallada:

```bash
./mvnw -B test
```

---

## Compilación

Limpiar y compilar el proyecto:

```bash
./mvnw clean compile
```

Generar las clases de jOOQ a partir del esquema de la base de datos:

```bash
./mvnw generate-sources
```

---

## Compilación para Producción

Construir la aplicación optimizada para producción (incluye la optimización del frontend de Vaadin):

```bash
./mvnw clean package -Pproduction
```

---

# Arquitectura

## Capa de Base de Datos

### Generación de Código con jOOQ

Las clases de jOOQ se generan automáticamente mediante el plugin:

```
testcontainers-jooq-codegen-maven-plugin
```

durante la fase:

```
generate-sources
```

El proceso realiza las siguientes acciones:

1. Inicia un contenedor PostgreSQL mediante Testcontainers.
2. Ejecuta todas las migraciones Flyway ubicadas en:

```
src/main/resources/db/migration/
```

3. Genera automáticamente el código Java tipado para acceder a la base de datos.

### Ubicación del Código Generado

```
pe.joedayz.workshop.aitaf.db
```

### Generador Personalizado

Se utiliza el generador:

```
ch.martinelli.oss.jooq.EqualsAndHashCodeJavaGenerator
```

para generar POJOs que incluyen automáticamente los métodos `equals()` y `hashCode()`.

### Configuración de la Base de Datos

- Motor: PostgreSQL
- Esquema: `public`
- Se excluye la tabla:

```
flyway_schema_history
```

Credenciales utilizadas por Testcontainers:

| Parámetro | Valor |
|-----------|-------|
| Usuario | `aitaf` |
| Contraseña | `aitaf` |
| Base de datos | `aitaf` |

Flyway es el encargado de administrar todas las migraciones del esquema.

---

# Infraestructura de Pruebas

## Configuración con Testcontainers

La aplicación utiliza la integración de Spring Boot con Testcontainers mediante:

```
TestcontainersConfiguration
```

Esta configuración:

- Proporciona un contenedor PostgreSQL mediante `@ServiceConnection`.
- Lo conecta automáticamente a las pruebas mediante:

```java
@Import(TestcontainersConfiguration.class)
```

La clase:

```
TestAiTrackAndFieldApplication
```

es el punto de entrada utilizado durante el desarrollo local.

---

## Capas de Pruebas

La estrategia de pruebas se divide en tres niveles:

### 1. Pruebas Unitarias e Integración

Pruebas estándar de Spring Boot utilizando Testcontainers.

### 2. Pruebas de la Interfaz Vaadin

Se utiliza **Karibu Testing** (`karibu-testing-v10-spring`) para probar componentes Vaadin sin necesidad de un navegador.

### 3. Pruebas End-to-End (E2E)

Se utilizan:

- Playwright
- Mopo (extensión para facilitar pruebas Vaadin)

para automatizar pruebas en navegador.

### Clase Base para Pruebas

Existe una clase base denominada:

```
KaribuTest
```

que contiene la configuración común para las pruebas de Vaadin.

---

# Frontend Vaadin

## Modo Desarrollo

Durante el desarrollo se dispone de:

- Spring Boot DevTools.
- Recarga automática del navegador.
- Hot Reload del frontend.

Configuración relevante:

```
vaadin.launch-browser=true
```

---

## Modo Producción

Se activa mediante el perfil:

```bash
-Pproduction
```

Durante la compilación se ejecutan automáticamente las tareas:

- `prepare-frontend`
- `build-frontend`

Estas tareas:

- optimizan el frontend,
- empaquetan los recursos JavaScript/TypeScript,
- eliminan las dependencias de desarrollo (`vaadin-dev`).

---

# Administración del Esquema de Base de Datos

Cuando sea necesario agregar tablas o modificar el esquema, seguir el siguiente procedimiento.

## Paso 1

Crear una nueva migración Flyway dentro de:

```
src/main/resources/db/migration/
```

Utilizar el formato:

```
V{versión}__{descripción}.sql
```

Ejemplo:

```
V001__create_athletes_table.sql
```

---

## Paso 2

Regenerar las clases de jOOQ:

```bash
./mvnw generate-sources
```

El plugin realizará automáticamente las siguientes acciones:

- Iniciar PostgreSQL mediante Testcontainers.
- Ejecutar todas las migraciones Flyway.
- Generar el código Java tipado en:

```
pe.joedayz.workshop.aitaf.db
```

---

# Estructura de Paquetes

Paquete base del proyecto:

```
pe.joedayz.workshop.aitaf
```

Actualmente la estructura mínima incluye:

- Clase principal:
    - `Application`

- Configuración de pruebas:
    - `TestcontainersConfiguration`
    - `TestApplication`

- Código generado por jOOQ:
    - `pe.joedayz.workshop.aitaf.db`

A medida que el proyecto crezca, se espera incorporar paquetes como:

- Modelos de dominio
- Entidades
- Vistas Vaadin
- Componentes de interfaz
- Servicios con la lógica de negocio
- Repositorios basados en `DSLContext` de jOOQ

---

# Integración Continua (CI/CD)

El proyecto incluye un flujo de trabajo de **GitHub Actions** ubicado en:

```
.github/workflows/ci.yml
```

Este flujo se ejecuta cuando ocurre cualquiera de los siguientes eventos:

- Push al repositorio.
- Pull Request.
- Ejecución manual.

La configuración utiliza:

- Java 21 (AdoptOpenJDK).
- Caché de dependencias Maven.

Finalmente ejecuta:

```bash
./mvnw -B test
```

para validar que todas las pruebas sean exitosas antes de completar el proceso de integración continua.