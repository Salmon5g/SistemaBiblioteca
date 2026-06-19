/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Logica;

import java.util.Date;

/**
 * Representa un préstamo de libro realizado por un socio de la biblioteca.
 *
 * Registra las fechas de préstamo, devolución prevista y devolución real, el
 * estado del préstamo (Activo / Devuelto) y la multa aplicada en caso de
 * devolución tardía ($ por día de atraso).
 *
 * Se asocia a un {@link Socio} y a un {@link Libro}.
 *
 * Patrón aplicado: MVC - esta clase forma parte de la capa Modelo (Logica).
 *
 * @author SistemaBiblioteca
 */
public class Prestamo {

    private Integer idPrestamo;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private Date fechaDevolucionReal;
    private String estado;
    private Double multa;
    private Socio socio;
    private Libro libro;

    /**
     * Constructor vacío requerido para instanciación desde el DAO.
     */
    public Prestamo() {
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(Date fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
