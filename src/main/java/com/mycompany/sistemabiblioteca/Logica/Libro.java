/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Logica;

/**
 * Representa un libro disponible en la biblioteca.
 *
 * Contiene información bibliográfica completa: ISBN, título, año de
 * publicación, editorial, cantidad de ejemplares totales y disponibles. Se
 * relaciona con un {@link Autor} y una {@link Categoria}.
 *
 * La cantidad de ejemplares disponibles se actualiza automáticamente cada vez
 * que se realiza o devuelve un préstamo.
 *
 * Patrón aplicado: MVC - esta clase forma parte de la capa Modelo (Logica).
 *
 * @author SistemaBiblioteca
 */
public class Libro {

    private Integer idLibro;
    private String isbn;
    private String titulo;
    private Integer anioPublicacion;
    private String editorial;
    private Integer cantidadEjemplares;
    private Integer cantidadDisponible;
    private Autor autor;
    private Categoria categoria;

    /**
     * Constructor vacío requerido para instanciación desde el DAO.
     */
    public Libro() {
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getCantidadEjemplares() {
        return cantidadEjemplares;
    }

    public void setCantidadEjemplares(Integer cantidadEjemplares) {
        this.cantidadEjemplares = cantidadEjemplares;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Representación textual utilizada en los JComboBox de la interfaz.
     *
     * @return título e ISBN del libro
     */
    @Override
    public String toString() {
        return titulo + " (ISBN: " + isbn + ")";
    }
}
