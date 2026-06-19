/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Logica;

/**
 * Representa la categoría o género literario de un libro.
 *
 * Cada categoría agrupa libros relacionados (e.g., Novela, Poesía, Tecnología).
 * Es utilizada en los formularios de gestión de libros y en consultas filtrando
 * por género.
 *
 * Patrón aplicado: MVC - esta clase forma parte de la capa Modelo (Logica).
 *
 * @author SistemaBiblioteca
 */
public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;

    /**
     * Constructor vacío requerido para instanciación desde el DAO.
     */
    public Categoria() {
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Representación textual utilizada en los JComboBox de la interfaz.
     *
     * @return nombre de la categoría
     */
    @Override
    public String toString() {
        return nombre;
    }
}
