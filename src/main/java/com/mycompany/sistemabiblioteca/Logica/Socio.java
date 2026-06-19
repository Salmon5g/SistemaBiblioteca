/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaBiblioteca.Logica;

import java.util.Date;

/**
 * Representa a un socio (lector registrado) del sistema de biblioteca.
 *
 * Almacena los datos personales y de contacto del socio, su estado
 * (Activo/Inactivo) y la fecha en que fue registrado en el sistema. Un socio
 * puede realizar múltiples préstamos a lo largo del tiempo.
 *
 * Patrón aplicado: MVC - esta clase forma parte de la capa Modelo (Logica).
 *
 * @author SistemaBiblioteca
 */
public class Socio {

    private Integer idSocio;
    private String rut;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private String direccion;
    private Date fechaRegistro;
    private String estado;

    /**
     * Constructor vacío requerido para instanciación desde el DAO.
     */
    public Socio() {
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Representación textual utilizada en los JComboBox de la interfaz.
     *
     * @return nombre completo y RUT del socio
     */
    @Override
    public String toString() {
        return nombreCompleto + " - " + rut;
    }
}
