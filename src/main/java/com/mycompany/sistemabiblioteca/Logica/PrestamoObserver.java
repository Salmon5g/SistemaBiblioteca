package com.mycompany.sistemaBiblioteca.Logica;

/**
 * Interfaz para el patrón de diseño <b>Observer</b> (Comportamiento).
 * 
 * <p>Define los métodos que deben implementar los observadores interesados
 * en los eventos de préstamos y devoluciones en el sistema.</p>
 * 
 * Patrón: <b>Observer</b> (GOF)
 * 
 * @author SistemaBiblioteca
 */
public interface PrestamoObserver {

    /**
     * Se dispara cuando un nuevo préstamo es registrado en el sistema.
     * 
     * @param p el préstamo registrado
     */
    void onPrestamoRegistrado(Prestamo p);

    /**
     * Se dispara cuando se registra la devolución de un préstamo.
     * 
     * @param p el préstamo devuelto
     */
    void onPrestamoDevuelto(Prestamo p);
}
