/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Data;
  
  import java.sql.Connection;
  import java.sql.DriverManager;
  import java.sql.SQLException;
  
  /**
   * Clase de conexión a la base de datos MySQL del Sistema de Biblioteca.
   *
   * <p>Implementa el patrón de diseño <b>Singleton</b>: garantiza que solo
   * exista una instancia activa de conexión en toda la aplicación, evitando
   * conexiones redundantes y reduciendo el consumo de recursos.</p>
   *
   * <p>El método estático {@link #get()} devuelve siempre la misma instancia
   * de {@link Connection}; si aún no se ha creado o si fue cerrada, la
   * recrea automáticamente.</p>
   *
   * Patrón: <b>Singleton</b>
   *
   * @author SistemaBiblioteca
   */
  public class Conn {
  
      /** URL de conexión a la base de datos MySQL. */
      private static final String URL      = "jdbc:mysql://localhost:3306/sistema_biblioteca";
  
      /** Usuario de la base de datos. */
      private static final String USUARIO  = "root";
  
      /** Contraseña del usuario de la base de datos. */
      private static final String PASSWORD = "Metraka20..";
  
      /** Instancia única de la conexión (patrón Singleton). */
      private static Connection instancia = null;
  
      /**
       * Constructor privado para impedir la creación externa de instancias
       * (requisito del patrón Singleton).
       */
      private Conn() {}
  
      /**
       * Devuelve la instancia única de {@link Connection} a la base de datos.
       * Si la conexión no existe o fue cerrada, la crea automáticamente.
       *
       * @return instancia única de conexión a MySQL
       * @throws SQLException si ocurre un error al establecer la conexión
       */
      public static Connection get() throws SQLException {
          if (instancia == null || instancia.isClosed()) {
              instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
          }
          return instancia;
      }
  }