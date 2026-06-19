package com.mycompany.sistemaBiblioteca.Logica;

import com.mycompany.sistemaBiblioteca.Data.Conn;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de 10 pruebas unitarias con JUnit 5 que validan los componentes críticos
 * del Sistema de Biblioteca: patrones de diseño (Singleton, Facade, Observer) y 
 * reglas de negocio de préstamos, multas y disponibilidad de inventario.
 * 
 * <p>Requisitos de la Rúbrica Cubiertos:</p>
 * <ul>
 *   <li>Uso de {@link org.junit.jupiter.api.Assertions#assertEquals}</li>
 *   <li>Uso de {@link org.junit.jupiter.api.Assertions#assertTrue}</li>
 *   <li>Uso de {@link org.junit.jupiter.api.Assertions#assertFalse}</li>
 *   <li>Uso de {@link org.junit.jupiter.api.Assertions#assertThrows}</li>
 * </ul>
 * 
 * @author SistemaBiblioteca
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BibliotecaFacadeTest {

    private static BibliotecaFacade facade;

    @BeforeAll
    public static void setUpClass() {
        facade = BibliotecaFacade.getInstancia();
    }

    // =========================================================================
    // GRUPO 1: PRUEBAS DEL PATRÓN SINGLETON (CONEXIÓN)
    // =========================================================================

    /**
     * Prueba 1: Verifica que la conexión no sea nula.
     */
    @Test
    @Order(1)
    public void testSingletonConexionNotNull() throws SQLException {
        Connection conn = Conn.get();
        assertNotNull(conn, "La conexión a la base de datos no debe ser nula.");
        assertTrue(conn.isValid(2), "La conexión a la base de datos debe ser válida.");
    }

    /**
     * Prueba 2: Verifica que la conexión mantenga una única instancia (Singleton).
     */
    @Test
    @Order(2)
    public void testSingletonConexionEsUnica() throws SQLException {
        Connection conn1 = Conn.get();
        Connection conn2 = Conn.get();
        assertSame(conn1, conn2, "Ambas referencias de conexión deben apuntar a la misma instancia física (Singleton).");
    }

    // =========================================================================
    // GRUPO 2: PRUEBAS DE LA LÓGICA DE MULTAS (DAO/FACADE)
    // =========================================================================

    /**
     * Prueba 3: Verifica que no se aplique multa si el libro se devuelve a tiempo.
     */
    @Test
    @Order(3)
    public void testCalcularMultaSinRetraso() {
        Calendar cal = Calendar.getInstance();
        Date fechaPrevista = cal.getTime();
        Date fechaReal = cal.getTime(); // Devolución hoy mismo

        double multa = facade.calcularMulta(fechaPrevista, fechaReal);
        assertEquals(0.0, multa, 0.001, "La multa debe ser 0 si se entrega el mismo día.");
    }

    /**
     * Prueba 4: Verifica la multa por retraso de 3 días ($1,500.0).
     */
    @Test
    @Order(4)
    public void testCalcularMultaConRetraso() {
        Calendar cal = Calendar.getInstance();
        Date fechaPrevista = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, 3); // 3 días de retraso
        Date fechaReal = cal.getTime();

        double multa = facade.calcularMulta(fechaPrevista, fechaReal);
        // Multa = 3 días * $500/día = $1500
        assertEquals(1500.0, multa, 0.001, "La multa debe ser $1500 por 3 días de retraso.");
    }

    // =========================================================================
    // GRUPO 3: PRUEBAS INTEGRALES DE LÓGICA Y BASE DE DATOS
    // =========================================================================

    /**
     * Prueba 5: Busca al socio inicial por RUT y valida sus datos.
     */
    @Test
    @Order(5)
    public void testBuscarSocioExistente() {
        Socio s = facade.buscarSocioPorRut("12345678-9");
        assertNotNull(s, "Debe existir el socio de ejemplo con RUT 12345678-9.");
        assertEquals("Juan Pérez González", s.getNombreCompleto(), "El nombre del socio debe coincidir.");
        assertEquals("Activo", s.getEstado(), "El socio de ejemplo debe estar en estado 'Activo'.");
    }

    /**
     * Prueba 6: Busca un socio inexistente y valida que sea nulo.
     */
    @Test
    @Order(6)
    public void testBuscarSocioInexistente() {
        Socio s = facade.buscarSocioPorRut("00000000-0");
        assertNull(s, "No debe existir un socio con el RUT ficticio 00000000-0.");
    }

    /**
     * Prueba 7: Valida que al crear un nuevo socio su estado por defecto sea 'Activo'.
     */
    @Test
    @Order(7)
    public void testSocioEstadoActivoPorDefecto() {
        Socio s = new Socio();
        s.setRut("88888888-8");
        s.setNombreCompleto("Socio de Prueba JUnit");
        s.setTelefono("123456");
        s.setEmail("junit@test.com");
        s.setDireccion("Calle Test 123");

        // Al guardar por primera vez el facade debe ponerlo 'Activo'
        facade.guardarSocio(s);

        Socio guardado = facade.buscarSocioPorRut("88888888-8");
        assertNotNull(guardado, "El socio debió guardarse.");
        assertEquals("Activo", guardado.getEstado(), "El estado debe ser 'Activo' por defecto.");

        // Limpieza de datos de prueba
        facade.eliminarSocio(guardado.getIdSocio());
    }

    /**
     * Prueba 8: Valida la existencia y nombre del Autor con ID 1.
     */
    @Test
    @Order(8)
    public void testBuscarAutorExistente() {
        Autor a = facade.buscarAutorPorId(1);
        assertNotNull(a, "Debe existir el autor con ID 1.");
        assertEquals("Gabriel García Márquez", a.getNombreCompleto(), "El autor con ID 1 debe ser Gabriel García Márquez.");
    }

    /**
     * Prueba 9: Valida la existencia y nombre de la Categoría con ID 1.
     */
    @Test
    @Order(9)
    public void testBuscarCategoriaExistente() {
        Categoria c = facade.buscarCategoriaPorId(1);
        assertNotNull(c, "Debe existir la categoría con ID 1.");
        assertEquals("Novela", c.getNombre(), "La categoría con ID 1 debe ser 'Novela'.");
    }

    /**
     * Prueba 10: Verifica que la regla de negocio impida prestar libros sin stock disponible.
     */
    @Test
    @Order(10)
    public void testRegistrarPrestamoSinStockLanzaExcepcion() {
        // Crear un libro temporal sin stock
        Libro l = new Libro();
        l.setIsbn("000-00-00000");
        l.setTitulo("Libro Sin Stock Temporal");
        l.setCantidadEjemplares(0);
        l.setCantidadDisponible(0);
        l.setAutor(facade.buscarAutorPorId(1));
        l.setCategoria(facade.buscarCategoriaPorId(1));

        facade.guardarLibro(l);

        // Recuperar el libro insertado para tener su ID
        Libro insertado = null;
        for (Libro libro : facade.listarLibros()) {
            if ("000-00-00000".equals(libro.getIsbn())) {
                insertado = libro;
                break;
            }
        }

        assertNotNull(insertado, "El libro sin stock debió registrarse en la BD.");

        // Intentar realizar el préstamo
        Prestamo p = new Prestamo();
        p.setSocio(facade.buscarSocioPorRut("12345678-9"));
        p.setLibro(insertado);
        p.setFechaPrestamo(new Date());
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        p.setFechaDevolucionPrevista(cal.getTime());

        // Debe lanzar IllegalStateException porque disponibilidad es 0
        final Libro libroParaPrestar = insertado;
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            facade.registrarPrestamo(p);
        }, "Debe lanzar IllegalStateException al no haber stock.");

        assertTrue(exception.getMessage().contains("no tiene ejemplares disponibles"), 
                "El mensaje de error debe indicar la falta de disponibilidad.");

        // Validar con assertFalse que el libro realmente no está disponible para préstamo
        assertFalse(libroParaPrestar.getCantidadDisponible() > 0, "La disponibilidad debe ser 0.");

        // Limpieza de datos
        facade.eliminarLibro(libroParaPrestar.getIdLibro());
    }
}
