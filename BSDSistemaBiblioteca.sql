 -- ============================================================
  --  SISTEMA DE BIBLIOTECA  -  Script SQL
  --  Base de datos: sistema_biblioteca
  --  Motor: MySQL 8+
  --
  --  Patrones de diseño aplicados:
  --   1. Singleton  -> Conn.java (conexión única a la BD)
  --   2. DAO        -> DAOSocio, DAOAutor, DAOCategoria,
  --                    DAOLibro, DAOPrestamo
  --   3. MVC        -> Logica (Model) / Data (DAO) / GUI (View)
  --   4. Observer   -> Swing ActionListeners / MouseListeners
  -- ============================================================
  
  CREATE DATABASE IF NOT EXISTS sistema_biblioteca
      CHARACTER SET utf8mb4
      COLLATE utf8mb4_spanish_ci;
  
  USE sistema_biblioteca;
  
  -- -----------------------------------------------------------
  -- Tabla: Categoria
  -- -----------------------------------------------------------
  CREATE TABLE IF NOT EXISTS Categoria (
      idCategoria   INT AUTO_INCREMENT PRIMARY KEY,
      nombre        VARCHAR(100) NOT NULL,
      descripcion   VARCHAR(255)
  );
  
  -- -----------------------------------------------------------
  -- Tabla: Autor
  -- -----------------------------------------------------------
  CREATE TABLE IF NOT EXISTS Autor (
      idAutor        INT AUTO_INCREMENT PRIMARY KEY,
      nombreCompleto VARCHAR(150) NOT NULL,
      nacionalidad   VARCHAR(100)
  );
  
  -- -----------------------------------------------------------
  -- Tabla: Libro
  -- -----------------------------------------------------------
  CREATE TABLE IF NOT EXISTS Libro (
      idLibro              INT AUTO_INCREMENT PRIMARY KEY,
      isbn                 VARCHAR(20)  UNIQUE NOT NULL,
      titulo               VARCHAR(200) NOT NULL,
      anioPublicacion      INT,
      editorial            VARCHAR(100),
      cantidadEjemplares   INT DEFAULT 1,
      cantidadDisponible   INT DEFAULT 1,
      idAutor              INT,
      idCategoria          INT,
      FOREIGN KEY (idAutor)     REFERENCES Autor(idAutor)         ON DELETE SET NULL,
      FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria) ON DELETE SET NULL
  );
  
  -- -----------------------------------------------------------
  -- Tabla: Socio
  -- -----------------------------------------------------------
  CREATE TABLE IF NOT EXISTS Socio (
      idSocio        INT AUTO_INCREMENT PRIMARY KEY,
      rut            VARCHAR(12)  UNIQUE NOT NULL,
      nombreCompleto VARCHAR(150) NOT NULL,
      telefono       VARCHAR(20),
      email          VARCHAR(100),
      direccion      VARCHAR(200),
      fechaRegistro  DATE         DEFAULT (CURDATE()),
      estado         VARCHAR(20)  DEFAULT 'Activo'
  );
  
  -- -----------------------------------------------------------
  -- Tabla: Prestamo
  -- -----------------------------------------------------------
  CREATE TABLE IF NOT EXISTS Prestamo (
      idPrestamo              INT AUTO_INCREMENT PRIMARY KEY,
      fechaPrestamo           DATE   NOT NULL,
      fechaDevolucionPrevista DATE   NOT NULL,
      fechaDevolucionReal     DATE,
      estado                  VARCHAR(20)  DEFAULT 'Activo',
      multa                   DOUBLE       DEFAULT 0.0,
      idSocio                 INT NOT NULL,
      idLibro                 INT NOT NULL,
      FOREIGN KEY (idSocio) REFERENCES Socio(idSocio),
      FOREIGN KEY (idLibro) REFERENCES Libro(idLibro)
  );
  
  -- -----------------------------------------------------------
  -- Datos de ejemplo
  -- -----------------------------------------------------------
  INSERT INTO Categoria (nombre, descripcion) VALUES
  ('Novela',          'Obras de ficción narrativa extensa'),
  ('Ciencia Ficción', 'Literatura de anticipación científica y tecnológica'),
  ('Historia',        'Obras sobre hechos y personajes históricos'),
  ('Poesía',          'Obras líricas y escritas en verso'),
  ('Tecnología',      'Libros de programación, software y tecnología');
  
  INSERT INTO Autor (nombreCompleto, nacionalidad) VALUES
  ('Gabriel García Márquez', 'Colombiana'),
  ('Isabel Allende',          'Chilena'),
  ('Isaac Asimov',            'Estadounidense'),
  ('Mario Benedetti',         'Uruguaya'),
  ('Robert C. Martin',        'Estadounidense');
  
  INSERT INTO Libro (isbn, titulo, anioPublicacion, editorial, cantidadEjemplares, cantidadDisponible, idAutor, idCategoria) VALUES
  ('978-84-376-0494-7', 'Cien años de soledad',              1967, 'Sudamericana',  3, 3, 1, 1),
  ('978-84-08-04745-3', 'La casa de los espíritus',          1982, 'Plaza y Janés', 2, 2, 2, 1),
  ('978-84-450-7288-6', 'Fundación',                         1951, 'Gnome Press',   2, 2, 3, 2),
  ('978-84-204-8208-3', 'El amor en los tiempos del cólera', 1985, 'Oveja Negra',   1, 1, 1, 1),
  ('978-0-13-235088-4', 'Clean Code',                        2008, 'Prentice Hall', 2, 2, 5, 5);
  
  INSERT INTO Socio (rut, nombreCompleto, telefono, email, direccion, fechaRegistro, estado) VALUES
  ('12345678-9', 'Juan Pérez González',  '+56912345678', 'juan.perez@email.com',  'Av. Siempre Viva 123', CURDATE(), 'Activo'),
  ('98765432-1', 'María López Soto',     '+56987654321', 'maria.lopez@email.com', 'Calle Principal 456',  CURDATE(), 'Activo');