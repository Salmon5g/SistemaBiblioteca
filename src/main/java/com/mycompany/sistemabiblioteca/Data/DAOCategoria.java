/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Data;
  
  import com.mycompany.sistemaBiblioteca.Logica.Categoria;
  
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.ArrayList;
  
  /**
   * DAO encargado de gestionar las operaciones CRUD sobre la entidad
   * {@link Categoria} en la base de datos.
   *
   * Proporciona métodos para listar, buscar, crear, actualizar y eliminar
   * registros de categorías utilizando JDBC y el patrón DAO.
   *
   * Patrón: <b>DAO (Data Access Object)</b>
   *
   * @author SistemaBiblioteca
   */
  public class DAOCategoria {
  
      // ---------------------------------------------------
      // LISTAR TODAS LAS CATEGORÍAS
      // ---------------------------------------------------
  
      /**
       * Obtiene la lista completa de categorías registradas en la base de datos.
       *
       * @return ArrayList con todos los objetos {@link Categoria}
       */
      public ArrayList<Categoria> listarTodos() {
          ArrayList<Categoria> lista = new ArrayList<>();
          String sql = "SELECT idCategoria, nombre, descripcion FROM Categoria ORDER BY nombre";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql);
               ResultSet rs = pstmt.executeQuery()) {
  
              while (rs.next()) {
                  Categoria c = new Categoria();
                  c.setIdCategoria(rs.getInt("idCategoria"));
                  c.setNombre(rs.getString("nombre"));
                  c.setDescripcion(rs.getString("descripcion"));
                  lista.add(c);
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return lista;
      }
  
      // ---------------------------------------------------
      // BUSCAR CATEGORÍA POR ID
      // ---------------------------------------------------
  
      /**
       * Busca una categoría según su identificador único.
       *
       * @param id identificador de la categoría
       * @return objeto {@link Categoria} encontrado, o {@code null} si no existe
       */
      public Categoria buscarPorId(Integer id) {
          Categoria c = null;
          String sql = "SELECT idCategoria, nombre, descripcion FROM Categoria WHERE idCategoria = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              ResultSet rs = pstmt.executeQuery();
  
              if (rs.next()) {
                  c = new Categoria();
                  c.setIdCategoria(rs.getInt("idCategoria"));
                  c.setNombre(rs.getString("nombre"));
                  c.setDescripcion(rs.getString("descripcion"));
              }
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return c;
      }
  
      // ---------------------------------------------------
      // CREAR CATEGORÍA
      // ---------------------------------------------------
  
      /**
       * Inserta una nueva categoría en la base de datos.
       *
       * @param c objeto {@link Categoria} con los datos a registrar
       */
      public void create(Categoria c) {
          String sql = "INSERT INTO Categoria (nombre, descripcion) VALUES (?, ?)";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, c.getNombre());
              pstmt.setString(2, c.getDescripcion());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ACTUALIZAR CATEGORÍA
      // ---------------------------------------------------
  
      /**
       * Actualiza los datos de una categoría existente.
       *
       * @param c objeto {@link Categoria} con los datos actualizados
       */
      public void update(Categoria c) {
          String sql = "UPDATE Categoria SET nombre = ?, descripcion = ? WHERE idCategoria = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setString(1, c.getNombre());
              pstmt.setString(2, c.getDescripcion());
              pstmt.setInt(3, c.getIdCategoria());
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      // ---------------------------------------------------
      // ELIMINAR CATEGORÍA
      // ---------------------------------------------------
  
      /**
       * Elimina una categoría de la base de datos según su ID.
       *
       * @param id identificador de la categoría a eliminar
       */
      public void delete(Integer id) {
          String sql = "DELETE FROM Categoria WHERE idCategoria = ?";
  
          try (Connection conn = Conn.get();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
  
              pstmt.setInt(1, id);
              pstmt.executeUpdate();
  
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }