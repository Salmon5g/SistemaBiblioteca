/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 package com.mycompany.sistemaBiblioteca.Data;
  
  import com.mycompany.sistemaBiblioteca.Logica.Autor;
  import com.mycompany.sistemaBiblioteca.Logica.Categoria;
  import com.mycompany.sistemaBiblioteca.Logica.Libro;
  import com.mycompany.sistemaBiblioteca.Logica.Prestamo;
  import com.mycompany.sistemaBiblioteca.Logica.Socio;
  
  
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.ArrayList;
  import java.util.Date;
  import java.util.concurrent.TimeUnit;
  
  /**
   * DAO encargado de gestionar las operaciones sobre la entidad {@link Prestamo}.
   *
   * Incluye: listado completo, préstamos activos, búsqueda por ID,
   * registro de nuevo préstamo (con actualización de disponibilidad del libro),
   * registro de devolución (con cálculo de multa por días de atraso),
   * y eliminación de registros.
   *
   * Multa: $500 por cada día de atraso en la devolución.
   *
   * Patrón: <b>DAO (Data Access Object)</b>
   *
   * @author SistemaBiblioteca
   */
  public class DAOPrestamo {
  
      /** Valor de multa por día de atraso (en pesos). */
      private static final double MULTA_POR_DIA = 500.0;
  
      // ---------------------------------------------------
      // MAPEO ResultSet → Prestamo
      // ---------------------------------------------------
  
      private Prestamo mapear(ResultSet rs) throws SQLException {
          Prestamo p = new Prestamo();
          p.setIdPrestamo(rs.getInt("idPrestamo"));
          p.setFechaPrestamo(rs.getDate("fechaPrestamo"));
          p.setFechaDevolucionPrevista(rs.getDate("fechaDevolucionPrevista"));
          p.setFechaDevolucionReal(rs.getDate("fechaDevolucionReal"));
          p.setEstado(rs.getString("estado"));
          p.setMulta(rs.getDouble("multa"));
  
          Socio s = new Socio();
          s.setIdSocio(rs.getInt("idSocio"));
          s.setNombreCompleto(rs.getString("nombreSocio"));
          s.setRut(rs.getString("rutSocio"));
          p.setSocio(s);
  
          Libro l = new Libro();
          l.setIdLibro(rs.getInt("idLibro"));
          l.setTitulo(rs.getString("tituloLibro"));
          l.setIsbn(rs.getString("isbnLibro"));
  
          Autor a = new Autor();
          a.setNombreCompleto(rs.getString("nombreAutor"));
          l.setAutor(a);
  
          Categoria c = new Categoria();
          c.setNombre(rs.getString("nombreCategoria"));
          l.setCategoria(c);
  
          p.setLibro(l);
          return p;
      }
  
      private static final String SQL_SELECT = """
          SELECT p.idPrestamo, p.fechaPrestamo, p.fechaDevolucionPrevista,
                 p.fechaDevolucionReal, p.estado, p.multa,
                 s.idSocio, s.nombreCompleto AS nombreSocio, s.rut AS rutSocio,
                 l.idLibro, l.titulo AS tituloLibro, l.isbn AS isbnLibro,
                 a.nombreCompleto AS nombreAutor,
                 c.nombre AS nombreCategoria
          FROM Prestamo p
          INNER JOIN Socio s ON p.idSocio = s.idSocio
          INNER JOIN Libro l ON p.idLibro = l.idLibro
          LEFT  JOIN Autor     a ON l.idAutor     = a.idAutor
          LEFT  JOIN Categoria c ON l.idCategoria = c.idCategoria
          """;
  
      // ---------------------------------------------------
      // LISTAR TODOS LOS PRÉSTAMOS
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista completa de préstamos con información del socio y el libro.
       *
       * @return ArrayList con todos los objetos {@link Prestamo}
       */
      public ArrayList<Prestamo> listarTodos() {
          ArrayList<Prestamo> lista = new ArrayList<>();
          String sql = SQL_SELECT + " ORDER BY p.fechaPrestamo DESC";
  
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
      // LISTAR PRÉSTAMOS ACTIVOS
      // ---------------------------------------------------
  
      /**
       * Obtiene los préstamos cuyo estado es 'Activo' (no devueltos aún).
       *
       * @return ArrayList de {@link Prestamo} activos
       */
      public ArrayList<Prestamo> listarActivos() {
          ArrayList<Prestamo> lista = new ArrayList<>();
          String sql = SQL_SELECT + " WHERE p.estado = 'Activo' ORDER BY p.fechaDevolucionPrevista";
  
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
      // BUSCAR PRÉSTAMO POR ID
      // ---------------------------------------------------
  
      /**
       * Busca un préstamo según su identificador único.
       *
       * @param id identificador del préstamo
       * @return objeto {@link Prestamo}, o {@code null} si no existe
       */
      public Prestamo buscarPorId(Integer id) {
          String sql = SQL_SELECT + " WHERE p.idPrestamo = ?";
  
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
      // CREAR PRÉSTAMO
      // ---------------------------------------------------
  
      /**
       * Registra un nuevo préstamo en la base de datos y descuenta un ejemplar
       * disponible del libro correspondiente.
       *
       * @param p objeto {@link Prestamo} con los datos del préstamo
       */
      public void create(Prestamo p) {
          String sql = """
              INSERT INTO Prestamo
                (fechaPrestamo, fechaDevolucionPrevista, estado, multa, idSocio, idLibro)
              VALUES (?, ?, 'Activo', 0.0, ?, ?)
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setDate(1, new java.sql.Date(p.getFechaPrestamo().getTime()));
              pstmt.setDate(2, new java.sql.Date(p.getFechaDevolucionPrevista().getTime()));
              pstmt.setInt(3, p.getSocio().getIdSocio());
              pstmt.setInt(4, p.getLibro().getIdLibro());
              pstmt.executeUpdate();
  
              // Descuentar disponibilidad del libro
              new DAOLibro().actualizarDisponibilidad(p.getLibro().getIdLibro(), -1);
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // REGISTRAR DEVOLUCIÓN
      // ---------------------------------------------------
  
      /**
       * Registra la devolución de un préstamo activo.
       * Calcula automáticamente la multa si la devolución es tardía
       * (MULTA_POR_DIA pesos × días de atraso) y actualiza la disponibilidad
       * del libro.
       *
       * @param idPrestamo         ID del préstamo a devolver
       * @param fechaDevolucionReal fecha real de devolución
       */
      public void registrarDevolucion(Integer idPrestamo, Date fechaDevolucionReal) {
          // Obtener el préstamo para calcular multa y conseguir el idLibro
          Prestamo p = buscarPorId(idPrestamo);
          if (p == null) return;
  
          double multa = calcularMulta(p.getFechaDevolucionPrevista(), fechaDevolucionReal);
  
          String sql = """
              UPDATE Prestamo
              SET fechaDevolucionReal = ?, estado = 'Devuelto', multa = ?
              WHERE idPrestamo = ?
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setDate(1, new java.sql.Date(fechaDevolucionReal.getTime()));
              pstmt.setDouble(2, multa);
              pstmt.setInt(3, idPrestamo);
              pstmt.executeUpdate();
  
              // Reintegrar ejemplar al libro
              new DAOLibro().actualizarDisponibilidad(p.getLibro().getIdLibro(), +1);
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // CALCULAR MULTA
      // ---------------------------------------------------
  
      /**
       * Calcula la multa por devolución tardía.
       * Si la fecha real es posterior a la prevista, aplica MULTA_POR_DIA
       * por cada día de diferencia; de lo contrario retorna 0.
       *
       * @param prevista fecha prevista de devolución
       * @param real     fecha real de devolución
       * @return monto total de la multa
       */
      public double calcularMulta(Date prevista, Date real) {
          if (real == null || !real.after(prevista)) return 0.0;
  
          long diff = real.getTime() - prevista.getTime();
          long dias = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
          return dias * MULTA_POR_DIA;
      }
  
      // ---------------------------------------------------
      // ELIMINAR PRÉSTAMO
      // ---------------------------------------------------
  
      /**
       * Elimina un préstamo de la base de datos según su ID.
       *
       * @param id identificador del préstamo a eliminar
       */
      public void delete(Integer id) {
          String sql = "DELETE FROM Prestamo WHERE idPrestamo = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }