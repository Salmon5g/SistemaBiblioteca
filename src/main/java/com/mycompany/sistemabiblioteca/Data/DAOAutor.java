/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Data;
  
  import com.mycompany.sistemaBiblioteca.Logica.Autor;
  
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.ArrayList;
  
  /**
   * DAO encargado de gestionar las operaciones CRUD sobre la entidad
   * {@link Autor} en la base de datos.
   *
   * Proporciona métodos para listar, buscar, crear, actualizar y eliminar
   * registros de autores utilizando JDBC y el patrón DAO.
   *
   * Patrón: <b>DAO (Data Access Object)</b>
   *
   * @author SistemaBiblioteca
   */
  public class DAOAutor {
  
      // ---------------------------------------------------
      // LISTAR TODOS LOS AUTORES
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista completa de autores registrados en la base de datos.
       *
       * @return ArrayList con todos los objetos {@link Autor}
       */
      public ArrayList<Autor> listarTodos() {
          ArrayList<Autor> lista = new ArrayList<>();
          String sql = "SELECT idAutor, nombreCompleto, nacionalidad FROM Autor ORDER BY nombreCompleto";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery()) {
  
              while (rs.next()) {
                  Autor a = new Autor();
                  a.setIdAutor(rs.getInt("idAutor"));
                  a.setNombreCompleto(rs.getString("nombreCompleto"));
                  a.setNacionalidad(rs.getString("nacionalidad"));
                  lista.add(a);
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // BUSCAR AUTOR POR ID
      // ---------------------------------------------------
  
      /**
       * Busca un autor según su identificador único.
       *
       * @param id identificador del autor
       * @return objeto {@link Autor} encontrado, o {@code null} si no existe
       */
      public Autor buscarPorId(Integer id) {
          Autor a = null;
          String sql = "SELECT idAutor, nombreCompleto, nacionalidad FROM Autor WHERE idAutor = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              ResultSet rs = pstmt.executeQuery();
  
              if (rs.next()) {
                  a = new Autor();
                  a.setIdAutor(rs.getInt("idAutor"));
                  a.setNombreCompleto(rs.getString("nombreCompleto"));
                  a.setNacionalidad(rs.getString("nacionalidad"));
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return a;
      }
  
      // ---------------------------------------------------
      // CREAR AUTOR
      // ---------------------------------------------------
  
      /**
       * Inserta un nuevo autor en la base de datos.
       *
       * @param a objeto {@link Autor} con los datos a registrar
       */
      public void create(Autor a) {
          String sql = "INSERT INTO Autor (nombreCompleto, nacionalidad) VALUES (?, ?)";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, a.getNombreCompleto());
              pstmt.setString(2, a.getNacionalidad());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ACTUALIZAR AUTOR
      // ---------------------------------------------------
  
      /**
       * Actualiza los datos de un autor existente.
       *
       * @param a objeto {@link Autor} con los datos actualizados
       */
      public void update(Autor a) {
          String sql = "UPDATE Autor SET nombreCompleto = ?, nacionalidad = ? WHERE idAutor = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, a.getNombreCompleto());
              pstmt.setString(2, a.getNacionalidad());
              pstmt.setInt(3, a.getIdAutor());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ELIMINAR AUTOR
      // ---------------------------------------------------
  
      /**
       * Elimina un autor de la base de datos según su ID.
       *
       * @param id identificador del autor a eliminar
       */
      public void delete(Integer id) {
          String sql = "DELETE FROM Autor WHERE idAutor = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }