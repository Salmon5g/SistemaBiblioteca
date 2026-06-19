/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 package com.mycompany.sistemaBiblioteca.Data;
  
  import com.mycompany.sistemaBiblioteca.Logica.Autor;
  import com.mycompany.sistemaBiblioteca.Logica.Categoria;
  import com.mycompany.sistemaBiblioteca.Logica.Libro;
  
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.ArrayList;
  
  /**
   * DAO encargado de gestionar las operaciones CRUD sobre la entidad
   * {@link Libro} en la base de datos.
   *
   * Incluye métodos de búsqueda por título, listado de libros disponibles,
   * y actualización de la cantidad de ejemplares disponibles cuando se
   * realiza o devuelve un préstamo.
   *
   * Patrón: <b>DAO (Data Access Object)</b>
   *
   * @author SistemaBiblioteca
   */
  public class DAOLibro {
  
      // SQL base con JOINs para obtener autor y categoría
      private static final String SQL_SELECT = """
          SELECT l.idLibro, l.isbn, l.titulo, l.anioPublicacion, l.editorial,
                 l.cantidadEjemplares, l.cantidadDisponible,
                 a.idAutor, a.nombreCompleto AS nombreAutor, a.nacionalidad,
                 c.idCategoria, c.nombre AS nombreCategoria, c.descripcion AS descCategoria
          FROM Libro l
          LEFT JOIN Autor     a ON l.idAutor     = a.idAutor
          LEFT JOIN Categoria c ON l.idCategoria = c.idCategoria
          """;
  
      /** Mapea un ResultSet a un objeto {@link Libro} con sus relaciones. */
      private Libro mapear(ResultSet rs) throws SQLException {
          Libro l = new Libro();
          l.setIdLibro(rs.getInt("idLibro"));
          l.setIsbn(rs.getString("isbn"));
          l.setTitulo(rs.getString("titulo"));
          l.setAnioPublicacion(rs.getInt("anioPublicacion"));
          l.setEditorial(rs.getString("editorial"));
          l.setCantidadEjemplares(rs.getInt("cantidadEjemplares"));
          l.setCantidadDisponible(rs.getInt("cantidadDisponible"));
  
          Autor a = new Autor();
          a.setIdAutor(rs.getInt("idAutor"));
          a.setNombreCompleto(rs.getString("nombreAutor"));
          a.setNacionalidad(rs.getString("nacionalidad"));
          l.setAutor(a);
  
          Categoria c = new Categoria();
          c.setIdCategoria(rs.getInt("idCategoria"));
          c.setNombre(rs.getString("nombreCategoria"));
          c.setDescripcion(rs.getString("descCategoria"));
          l.setCategoria(c);
  
          return l;
      }
  
      // ---------------------------------------------------
      // LISTAR TODOS LOS LIBROS
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista completa de libros con su autor y categoría.
       *
       * @return ArrayList con todos los objetos {@link Libro}
       */
      public ArrayList<Libro> listarTodos() {
          ArrayList<Libro> lista = new ArrayList<>();
          String sql = SQL_SELECT + " ORDER BY l.titulo";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery()) {
  
              while (rs.next()) lista.add(mapear(rs));
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // LISTAR LIBROS DISPONIBLES
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista de libros que tienen al menos un ejemplar disponible.
       *
       * @return ArrayList de {@link Libro} con cantidadDisponible > 0
       */
      public ArrayList<Libro> listarDisponibles() {
          ArrayList<Libro> lista = new ArrayList<>();
          String sql = SQL_SELECT + " WHERE l.cantidadDisponible > 0 ORDER BY l.titulo";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery()) {
  
              while (rs.next()) lista.add(mapear(rs));
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // BUSCAR POR ID
      // ---------------------------------------------------
  
      /**
       * Busca un libro por su identificador único.
       *
       * @param id identificador del libro
       * @return objeto {@link Libro}, o {@code null} si no existe
       */
      public Libro buscarPorId(Integer id) {
          String sql = SQL_SELECT + " WHERE l.idLibro = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              ResultSet rs = pstmt.executeQuery();
              if (rs.next()) return mapear(rs);
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return null;
      }
  
      // ---------------------------------------------------
      // BUSCAR POR TÍTULO (búsqueda parcial)
      // ---------------------------------------------------
  
      /**
       * Busca libros cuyo título contenga el texto indicado (búsqueda parcial).
       *
       * @param texto texto a buscar en el título
       * @return ArrayList de {@link Libro} que coinciden
       */
      public ArrayList<Libro> buscarPorTitulo(String texto) {
          ArrayList<Libro> lista = new ArrayList<>();
          String sql = SQL_SELECT + " WHERE l.titulo LIKE ? ORDER BY l.titulo";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, "%" + texto + "%");
              ResultSet rs = pstmt.executeQuery();
              while (rs.next()) lista.add(mapear(rs));
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // VERIFICAR EXISTENCIA POR ISBN
      // ---------------------------------------------------
  
      /**
       * Verifica si ya existe un libro con el ISBN dado.
       *
       * @param isbn ISBN a verificar
       * @return {@code true} si el ISBN ya está registrado
       */
      public boolean existeIsbn(String isbn) {
          String sql = "SELECT COUNT(*) FROM Libro WHERE isbn = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, isbn);
              ResultSet rs = pstmt.executeQuery();
              if (rs.next()) return rs.getInt(1) > 0;
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return false;
      }
  
      // ---------------------------------------------------
      // CREAR LIBRO
      // ---------------------------------------------------
  
      /**
       * Inserta un nuevo libro en la base de datos.
       *
       * @param l objeto {@link Libro} con los datos a registrar
       */
      public void create(Libro l) {
          String sql = """
              INSERT INTO Libro (isbn, titulo, anioPublicacion, editorial,
                                 cantidadEjemplares, cantidadDisponible,
                                 idAutor, idCategoria)
              VALUES (?, ?, ?, ?, ?, ?, ?, ?)
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, l.getIsbn());
              pstmt.setString(2, l.getTitulo());
              pstmt.setInt(3, l.getAnioPublicacion() != null ? l.getAnioPublicacion() : 0);
              pstmt.setString(4, l.getEditorial());
              pstmt.setInt(5, l.getCantidadEjemplares());
              pstmt.setInt(6, l.getCantidadDisponible());
              pstmt.setInt(7, l.getAutor().getIdAutor());
              pstmt.setInt(8, l.getCategoria().getIdCategoria());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ACTUALIZAR LIBRO
      // ---------------------------------------------------
  
      /**
       * Actualiza los datos de un libro existente.
       *
       * @param l objeto {@link Libro} con los datos actualizados
       */
      public void update(Libro l) {
          String sql = """
              UPDATE Libro
              SET isbn = ?, titulo = ?, anioPublicacion = ?, editorial = ?,
                  cantidadEjemplares = ?, cantidadDisponible = ?,
                  idAutor = ?, idCategoria = ?
              WHERE idLibro = ?
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, l.getIsbn());
              pstmt.setString(2, l.getTitulo());
              pstmt.setInt(3, l.getAnioPublicacion() != null ? l.getAnioPublicacion() : 0);
              pstmt.setString(4, l.getEditorial());
              pstmt.setInt(5, l.getCantidadEjemplares());
              pstmt.setInt(6, l.getCantidadDisponible());
              pstmt.setInt(7, l.getAutor().getIdAutor());
              pstmt.setInt(8, l.getCategoria().getIdCategoria());
              pstmt.setInt(9, l.getIdLibro());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ACTUALIZAR DISPONIBILIDAD
      // ---------------------------------------------------
  
      /**
       * Decrementa o incrementa la cantidad de ejemplares disponibles de un libro.
       * Se invoca desde el DAO de préstamos al crear o devolver un préstamo.
       *
       * @param idLibro    identificador del libro
       * @param delta      valor a sumar (+1 devolución) o restar (-1 préstamo)
       */
      public void actualizarDisponibilidad(Integer idLibro, int delta) {
          String sql = "UPDATE Libro SET cantidadDisponible = cantidadDisponible + ? WHERE idLibro = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, delta);
              pstmt.setInt(2, idLibro);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ELIMINAR LIBRO
      // ---------------------------------------------------
  
      /**
       * Elimina un libro de la base de datos según su ID.
       *
       * @param id identificador del libro a eliminar
       */
      public void delete(Integer id) {
          String sql = "DELETE FROM Libro WHERE idLibro = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }
  