/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Data;
  
  import com.mycompany.sistemaBiblioteca.Logica.Socio;
  
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.ArrayList;
  
  /**
   * DAO encargado de gestionar las operaciones CRUD sobre la entidad
   * {@link Socio} en la base de datos.
   *
   * Proporciona métodos para listar, buscar, verificar duplicados, crear,
   * actualizar y eliminar registros de socios utilizando JDBC y el patrón DAO.
   *
   * Patrón: <b>DAO (Data Access Object)</b>
   *
   * @author SistemaBiblioteca
   */
  public class DAOSocio {
  
      // ---------------------------------------------------
      // LISTAR TODOS LOS SOCIOS
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista completa de socios registrados en la base de datos.
       *
       * @return ArrayList con todos los objetos {@link Socio}
       */
      public ArrayList<Socio> listarTodos() {
          ArrayList<Socio> lista = new ArrayList<>();
          String sql = """
              SELECT idSocio, rut, nombreCompleto, telefono,
                     email, direccion, fechaRegistro, estado
              FROM Socio
              ORDER BY nombreCompleto
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery()) {
  
              while (rs.next()) {
                  Socio s = new Socio();
                  s.setIdSocio(rs.getInt("idSocio"));
                  s.setRut(rs.getString("rut"));
                  s.setNombreCompleto(rs.getString("nombreCompleto"));
                  s.setTelefono(rs.getString("telefono"));
                  s.setEmail(rs.getString("email"));
                  s.setDireccion(rs.getString("direccion"));
                  s.setFechaRegistro(rs.getDate("fechaRegistro"));
                  s.setEstado(rs.getString("estado"));
                  lista.add(s);
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // BUSCAR SOCIO POR ID
      // ---------------------------------------------------
  
      /**
       * Busca un socio según su identificador único.
       *
       * @param id identificador del socio
       * @return objeto {@link Socio} encontrado, o {@code null} si no existe
       */
      public Socio buscarPorId(Integer id) {
          Socio s = null;
          String sql = """
              SELECT idSocio, rut, nombreCompleto, telefono,
                     email, direccion, fechaRegistro, estado
              FROM Socio WHERE idSocio = ?
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              ResultSet rs = pstmt.executeQuery();
  
              if (rs.next()) {
                  s = new Socio();
                  s.setIdSocio(rs.getInt("idSocio"));
                  s.setRut(rs.getString("rut"));
                  s.setNombreCompleto(rs.getString("nombreCompleto"));
                  s.setTelefono(rs.getString("telefono"));
                  s.setEmail(rs.getString("email"));
                  s.setDireccion(rs.getString("direccion"));
                  s.setFechaRegistro(rs.getDate("fechaRegistro"));
                  s.setEstado(rs.getString("estado"));
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return s;
      }
      
      
          // ---------------------------------------------------
    // BUSCAR SOCIO POR RUT
    // ---------------------------------------------------

    /**
     * Busca un socio según su RUT único.
     *
     * @param rut RUT del socio a buscar
     * @return objeto {@link Socio} encontrado, o {@code null} si no existe
     */
    public Socio buscarPorRut(String rut) {
        Socio s = null;
        String sql = """
            SELECT idSocio, rut, nombreCompleto, telefono,
                   email, direccion, fechaRegistro, estado
            FROM Socio WHERE rut = ?
            """;

        try (Connection conn = Conn.get();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rut);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                s = new Socio();
                s.setIdSocio(rs.getInt("idSocio"));
                s.setRut(rs.getString("rut"));
                s.setNombreCompleto(rs.getString("nombreCompleto"));
                s.setTelefono(rs.getString("telefono"));
                s.setEmail(rs.getString("email"));
                s.setDireccion(rs.getString("direccion"));
                s.setFechaRegistro(rs.getDate("fechaRegistro"));
                s.setEstado(rs.getString("estado"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
  
      // ---------------------------------------------------
      // VERIFICAR EXISTENCIA POR RUT
      // ---------------------------------------------------
  
      /**
       * Verifica si ya existe un socio registrado con el RUT dado.
       *
       * @param rut RUT a verificar
       * @return {@code true} si el RUT ya está registrado
       */
      public boolean existeRut(String rut) {
          String sql = "SELECT COUNT(*) FROM Socio WHERE rut = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, rut);
              ResultSet rs = pstmt.executeQuery();
              if (rs.next()) return rs.getInt(1) > 0;
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return false;
      }
  
      // ---------------------------------------------------
      // CREAR SOCIO
      // ---------------------------------------------------
  
      /**
       * Inserta un nuevo socio en la base de datos.
       *
       * @param s objeto {@link Socio} con los datos a registrar
       */
      public void create(Socio s) {
          String sql = """
              INSERT INTO Socio (rut, nombreCompleto, telefono,
                                 email, direccion, fechaRegistro, estado)
              VALUES (?, ?, ?, ?, ?, CURDATE(), ?)
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, s.getRut());
              pstmt.setString(2, s.getNombreCompleto());
              pstmt.setString(3, s.getTelefono());
              pstmt.setString(4, s.getEmail());
              pstmt.setString(5, s.getDireccion());
              pstmt.setString(6, s.getEstado());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ACTUALIZAR SOCIO
      // ---------------------------------------------------
  
      /**
       * Actualiza los datos de un socio existente.
       *
       * @param s objeto {@link Socio} con los datos actualizados
       */
      public void update(Socio s) {
          String sql = """
              UPDATE Socio
              SET rut = ?, nombreCompleto = ?, telefono = ?,
                  email = ?, direccion = ?, estado = ?
              WHERE idSocio = ?
              """;
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, s.getRut());
              pstmt.setString(2, s.getNombreCompleto());
              pstmt.setString(3, s.getTelefono());
              pstmt.setString(4, s.getEmail());
              pstmt.setString(5, s.getDireccion());
              pstmt.setString(6, s.getEstado());
              pstmt.setInt(7, s.getIdSocio());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ELIMINAR SOCIO
      // ---------------------------------------------------
  
      /**
       * Elimina un socio de la base de datos según su ID.
       *
       * @param id identificador del socio a eliminar
       */
      public void delete(Integer id) {
          String sql = "DELETE FROM Socio WHERE idSocio = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }