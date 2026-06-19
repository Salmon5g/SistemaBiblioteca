package com.mycompany.sistemaBiblioteca.Logica;

import com.mycompany.sistemaBiblioteca.Data.DAOAutor;
import com.mycompany.sistemaBiblioteca.Data.DAOCategoria;
import com.mycompany.sistemaBiblioteca.Data.DAOLibro;
import com.mycompany.sistemaBiblioteca.Data.DAOPrestamo;
import com.mycompany.sistemaBiblioteca.Data.DAOSocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fachada única que centraliza el acceso a la lógica de negocio y base de datos.
 * 
 * <p>Implementa el patrón de diseño <b>Facade</b>: desacopla la capa de
 * presentación (GUI) de los múltiples DAOs y reglas del negocio.</p>
 * 
 * <p>También actúa como el sujeto observable en el patrón <b>Observer</b>,
 * notificando a las vistas registradas cuando se realizan préstamos o devoluciones.</p>
 * 
 * Patrones: <b>Facade</b> y <b>Observer</b>
 * 
 * @author SistemaBiblioteca
 */
public class BibliotecaFacade {

    // Instancia única (Singleton opcional para la fachada)
    private static BibliotecaFacade instancia;

    // DAOs encapsulados
    private final DAOSocio daoSocio;
    private final DAOAutor daoAutor;
    private final DAOCategoria daoCategoria;
    private final DAOLibro daoLibro;
    private final DAOPrestamo daoPrestamo;

    // Lista de observadores para préstamos
    private final List<PrestamoObserver> observadores;

    /**
     * Constructor privado de la Fachada.
     */
    private BibliotecaFacade() {
        this.daoSocio = new DAOSocio();
        this.daoAutor = new DAOAutor();
        this.daoCategoria = new DAOCategoria();
        this.daoLibro = new DAOLibro();
        this.daoPrestamo = new DAOPrestamo();
        this.observadores = new ArrayList<>();
    }

    /**
     * Devuelve la instancia única de la fachada (patrón Singleton).
     * 
     * @return instancia de BibliotecaFacade
     */
    public static synchronized BibliotecaFacade getInstancia() {
        if (instancia == null) {
            instancia = new BibliotecaFacade();
        }
        return instancia;
    }

    // -------------------------------------------------------------------------
    // Gestión del Patrón Observer
    // -------------------------------------------------------------------------

    public void agregarObservador(PrestamoObserver obs) {
        if (!observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    public void removerObservador(PrestamoObserver obs) {
        observadores.remove(obs);
    }

    private void notificarPrestamoRegistrado(Prestamo p) {
        for (PrestamoObserver obs : observadores) {
            obs.onPrestamoRegistrado(p);
        }
    }

    private void notificarPrestamoDevuelto(Prestamo p) {
        for (PrestamoObserver obs : observadores) {
            obs.onPrestamoDevuelto(p);
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD: SOCIO
    // -------------------------------------------------------------------------

    public ArrayList<Socio> listarSocios() {
        return daoSocio.listarTodos();
    }

    public Socio buscarSocioPorId(Integer id) {
        return daoSocio.buscarPorId(id);
    }

     public Socio buscarSocioPorRut(String rut) {
        return daoSocio.buscarPorRut(rut);
    }

    public void guardarSocio(Socio s) {
        if (s.getIdSocio() == null) {
            s.setEstado("Activo"); // Estado inicial por defecto
            daoSocio.create(s);
        } else {
            daoSocio.update(s);
        }
    }

    public void eliminarSocio(Integer id) {
        daoSocio.delete(id);
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD: AUTOR
    // -------------------------------------------------------------------------

    public ArrayList<Autor> listarAutores() {
        return daoAutor.listarTodos();
    }

    public Autor buscarAutorPorId(Integer id) {
        return daoAutor.buscarPorId(id);
    }

    public void guardarAutor(Autor a) {
        if (a.getIdAutor() == null) {
            daoAutor.create(a);
        } else {
            daoAutor.update(a);
        }
    }

    public void eliminarAutor(Integer id) {
        daoAutor.delete(id);
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD: CATEGORIA
    // -------------------------------------------------------------------------

    public ArrayList<Categoria> listarCategorias() {
        return daoCategoria.listarTodos();
    }

    public Categoria buscarCategoriaPorId(Integer id) {
        return daoCategoria.buscarPorId(id);
    }

    public void guardarCategoria(Categoria c) {
        if (c.getIdCategoria() == null) {
            daoCategoria.create(c);
        } else {
            daoCategoria.update(c);
        }
    }

    public void eliminarCategoria(Integer id) {
        daoCategoria.delete(id);
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD: LIBRO
    // -------------------------------------------------------------------------

    public ArrayList<Libro> listarLibros() {
        return daoLibro.listarTodos();
    }

    public ArrayList<Libro> listarLibrosDisponibles() {
        return daoLibro.listarDisponibles();
    }

    public Libro buscarLibroPorId(Integer id) {
        return daoLibro.buscarPorId(id);
    }

    public ArrayList<Libro> buscarLibrosPorTitulo(String titulo) {
        return daoLibro.buscarPorTitulo(titulo);
    }

    public void guardarLibro(Libro l) {
        if (l.getIdLibro() == null) {
            l.setCantidadDisponible(l.getCantidadEjemplares());
            daoLibro.create(l);
        } else {
            // Ajustar la disponibilidad al actualizar ejemplares
            Libro original = daoLibro.buscarPorId(l.getIdLibro());
            if (original != null) {
                int prestados = original.getCantidadEjemplares() - original.getCantidadDisponible();
                l.setCantidadDisponible(l.getCantidadEjemplares() - prestados);
            }
            daoLibro.update(l);
        }
    }

    public void eliminarLibro(Integer id) {
        daoLibro.delete(id);
    }

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD/PROCESOS: PRESTAMO
    // -------------------------------------------------------------------------

    public ArrayList<Prestamo> listarPrestamos() {
        return daoPrestamo.listarTodos();
    }

    public ArrayList<Prestamo> listarPrestamosActivos() {
        return daoPrestamo.listarActivos();
    }

    public Prestamo buscarPrestamoPorId(Integer id) {
        return daoPrestamo.buscarPorId(id);
    }

    /**
     * Registra un nuevo préstamo en el sistema si se cumplen las validaciones.
     * 
     * @param p objeto préstamo a registrar
     * @throws IllegalStateException si el libro seleccionado no tiene stock disponible
     */
    public void registrarPrestamo(Prestamo p) {
        // Validación crítica del negocio: stock disponible
        Libro libro = daoLibro.buscarPorId(p.getLibro().getIdLibro());
        if (libro == null || libro.getCantidadDisponible() <= 0) {
            throw new IllegalStateException("El libro '" + (libro != null ? libro.getTitulo() : "Desconocido") 
                    + "' no tiene ejemplares disponibles para préstamo.");
        }

        // Validación de estado del socio
        Socio socio = daoSocio.buscarPorId(p.getSocio().getIdSocio());
        if (socio == null || !"Activo".equalsIgnoreCase(socio.getEstado())) {
            throw new IllegalStateException("El socio seleccionado no se encuentra Activo.");
        }

        daoPrestamo.create(p);
        
        // Cargar préstamo completo para notificar a los observadores
        Prestamo guardado = daoPrestamo.buscarPorId(p.getIdPrestamo());
        if (guardado == null) {
            guardado = p; // fall back si no autoincrementó en la consulta actual
        }
        notificarPrestamoRegistrado(guardado);
    }

    /**
     * Registra la devolución de un libro y calcula posibles multas.
     * 
     * @param idPrestamo ID del préstamo
     * @param fechaDevolucionReal fecha en que se devuelve
     */
    public void registrarDevolucion(Integer idPrestamo, Date fechaDevolucionReal) {
        Prestamo p = daoPrestamo.buscarPorId(idPrestamo);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró el préstamo con ID: " + idPrestamo);
        }

        daoPrestamo.registrarDevolucion(idPrestamo, fechaDevolucionReal);
        
        // Recuperar el préstamo actualizado para notificar
        Prestamo actualizado = daoPrestamo.buscarPorId(idPrestamo);
        notificarPrestamoDevuelto(actualizado);
    }

    public double calcularMulta(Date prevista, Date real) {
        return daoPrestamo.calcularMulta(prevista, real);
    }

    public void eliminarPrestamo(Integer id) {
        daoPrestamo.delete(id);
    }
}
