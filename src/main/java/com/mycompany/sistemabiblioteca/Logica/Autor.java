/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Logica;

/**
 * Representa al autor de uno o varios libros dentro del sistema de biblioteca.
 *
 * Almacena el nombre completo y la nacionalidad del autor. Se asocia a la
 * entidad {@link Libro} para identificar al escritor de la obra.
 *
 * Patrón aplicado: MVC - esta clase forma parte de la capa Modelo (Logica).
 *
 * @author SistemaBiblioteca
 */
public class Autor {

    private Integer idAutor;
    private String nombreCompleto;
    private String nacionalidad;

    /**
     * Constructor vacío requerido para instanciación desde el DAO.
     */
    public Autor() {
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Integer getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Representación textual utilizada en los JComboBox de la interfaz.
     *
     * @return nombre completo del autor
     */
    @Override
    public String toString() {
        return nombreCompleto;
    }
}
