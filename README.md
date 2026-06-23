<h1 align="center">
  📚 Sistema Biblioteca
</h1>

<p align="center">
  Sistema de gestión bibliotecaria desktop desarrollado en <strong>Java 17</strong> con interfaz gráfica <strong>Swing</strong> y persistencia en <strong>MySQL 8</strong>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/NetBeans-IDE-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white"/>
</p>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Base de Datos](#-base-de-datos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución](#-ejecución)
- [Problemas Comunes](#-problemas-comunes)

---

## 📖 Descripción

**Sistema Biblioteca** es una aplicación de escritorio para la gestión integral de una biblioteca. Permite administrar socios, libros, autores, categorías y préstamos, incluyendo el cálculo de multas por devoluciones tardías. El sistema fue construido aplicando buenas prácticas de ingeniería de software y patrones de diseño GoF.

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java (JDK) | 17 | Lenguaje principal |
| MySQL | 8.0+ | Base de datos relacional |
| Maven | 3.x | Gestión de dependencias |
| Swing / AbsoluteLayout | — | Interfaz gráfica de usuario |
| MySQL Connector/J | 8.3.0 | Driver JDBC para MySQL |
| JUnit Jupiter | 5.10.1 | Pruebas unitarias |

---

## 🧩 Patrones de Diseño

Este proyecto aplica los siguientes patrones GoF (Gang of Four):

| Patrón | Implementación |
|---|---|
| **Singleton** | `Conn.java` — garantiza una única instancia de conexión a la BD |
| **DAO** (Data Access Object) | `DAOSocio`, `DAOAutor`, `DAOCategoria`, `DAOLibro`, `DAOPrestamo` |
| **MVC** (Model-View-Controller) | `Logica/` (Model) · `Data/` (DAO) · `GUI/` (View) |
| **Observer** | Swing `ActionListeners` y `MouseListeners` en las vistas |

> 📄 Para más detalle, consultá el archivo [`analisis_patrones_GoF.pdf`](./analisis_patrones_GoF.pdf) incluido en el repositorio.

---

## 📁 Estructura del Proyecto

```
SistemaBiblioteca/
├── src/
│   └── main/java/com/mycompany/sistemabiblioteca/
│       ├── Data/               # Capa de acceso a datos
│       │   ├── Conn.java       # ⚙️ Configuración de conexión (Singleton)
│       │   ├── DAOSocio.java
│       │   ├── DAOAutor.java
│       │   ├── DAOCategoria.java
│       │   ├── DAOLibro.java
│       │   └── DAOPrestamo.java
│       ├── Logica/             # Capa de lógica de negocio (Model)
│       └── GUI/                # Capa de interfaz gráfica (View)
│           └── Principal.java  # 🚀 Clase principal / punto de entrada
├── BSDSistemaBiblioteca.sql    # 🗄️ Script completo de la base de datos
├── analisis_patrones_GoF.pdf   # 📄 Documentación de patrones de diseño
├── Portafolio.pdf              # 📄 Portafolio del proyecto
└── pom.xml                     # 📦 Configuración Maven
```

---

## 🗄️ Base de Datos

La base de datos se llama `sistema_biblioteca` y contiene las siguientes tablas:

```
sistema_biblioteca
├── Categoria     (idCategoria, nombre, descripcion)
├── Autor         (idAutor, nombreCompleto, nacionalidad)
├── Libro         (idLibro, isbn, titulo, anioPublicacion, editorial,
│                  cantidadEjemplares, cantidadDisponible, idAutor, idCategoria)
├── Socio         (idSocio, rut, nombreCompleto, telefono, email,
│                  direccion, fechaRegistro, estado)
└── Prestamo      (idPrestamo, fechaPrestamo, fechaDevolucionPrevista,
                   fechaDevolucionReal, estado, multa, idSocio, idLibro)
```

> El archivo `BSDSistemaBiblioteca.sql` incluye la creación de la base de datos, todas las tablas y datos de ejemplo (socios, libros, autores y categorías precargados).

---

## 🚀 Instalación y Configuración

### ✅ Requisitos Previos

Asegurate de tener instalado antes de continuar:

- ☕ **JDK 17** o superior → [Descargar](https://adoptium.net/)
- 🐬 **MySQL 8.0** o superior → [Descargar](https://dev.mysql.com/downloads/)
- 🛠️ **Apache NetBeans** (recomendado) o cualquier IDE compatible con Maven
- 📦 **Apache Maven 3.x** (incluido en NetBeans)

---

### Paso 1 — Clonar el repositorio

```bash
git clone https://github.com/Salmon5g/SistemaBiblioteca.git
cd SistemaBiblioteca
```

---

### Paso 2 — Importar la Base de Datos

Tenés dos opciones para importar el archivo `BSDSistemaBiblioteca.sql`:

**Opción A — Desde la terminal:**

```bash
mysql -u tu_usuario -p < BSDSistemaBiblioteca.sql
```

> Esto crea automáticamente la base de datos `sistema_biblioteca`, sus tablas y los datos de ejemplo.

**Opción B — Desde MySQL Workbench:**

1. Abrí MySQL Workbench y conectate a tu servidor.
2. Ir a **Server** → **Data Import**.
3. Seleccionar **Import from Self-Contained File**.
4. Elegir el archivo `BSDSistemaBiblioteca.sql`.
5. Hacer clic en **Start Import**.

---

### Paso 3 — Configurar la Conexión

1. Abrí el proyecto en tu IDE.
2. Navegá hasta el archivo de conexión:

```
src/main/java/com/mycompany/sistemabiblioteca/Data/Conn.java
```

3. Localizá el bloque de configuración y **reemplazá los valores** con los datos de tu entorno:

```java
// ⚙️ Modificá estos valores con tu configuración local
private static final String URL      = "jdbc:mysql://localhost:3306/sistema_biblioteca";
private static final String USUARIO  = "tu_usuario";      // 👈 Cambiá esto
private static final String PASSWORD = "tu_contraseña";   // 👈 Cambiá esto
```

| Campo | Descripción | Ejemplo |
|---|---|---|
| `URL` | Host, puerto y nombre de la base de datos | `localhost:3306/sistema_biblioteca` |
| `USUARIO` | Usuario MySQL con permisos sobre la BD | `root` |
| `PASSWORD` | Contraseña del usuario MySQL | `miContraseña123` |

> ⚠️ **Seguridad:** Si vas a subir cambios al repositorio, nunca incluyas tus credenciales reales en este archivo. Considerá usar variables de entorno o un archivo de configuración excluido en `.gitignore`.

---

## ▶️ Ejecución

### Desde NetBeans:

1. Abrí el proyecto como **Maven Project**.
2. Maven descargará las dependencias automáticamente.
3. Hacé clic en **Run Project** (▶️) o presioná `F6`.

### Desde la terminal con Maven:

```bash
mvn compile
mvn exec:java
```

---

## 🐛 Problemas Comunes

| Error | Causa probable | Solución |
|---|---|---|
| `Communications link failure` | El servicio MySQL no está activo | Iniciá MySQL (`net start mysql` / `sudo service mysql start`) |
| `Access denied for user '...'` | Credenciales incorrectas en `Conn.java` | Verificá usuario y contraseña |
| `Unknown database 'sistema_biblioteca'` | El script SQL no fue ejecutado | Importá el archivo `BSDSistemaBiblioteca.sql` |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Falta la dependencia del conector | Ejecutá `mvn install` para descargar dependencias |
| La GUI no abre | Error en la clase principal | Verificá que `GUI/Principal.java` sea el punto de entrada |

---

## 👥 Autores

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Salmon5g">
        <img src="https://github.com/Salmon5g.png" width="80px;" alt="Juan Pablo S"/><br/>
        <sub><b>Juan Pablo S.</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/eddthestaffel">
        <img src="https://github.com/eddthestaffel.png" width="80px;" alt="Edd"/><br/>
        <sub><b>Edd</b></sub>
      </a>
    </td>
  </tr>
</table>

---

<p align="center">
  Hecho con ☕ Java y mucho 📚 amor por los libros.
</p>
