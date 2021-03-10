/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen;

import java.util.ArrayList;

/**
 *
 * @author masa
 */
public class inmobiliaria {
    private ArrayList<inmueble> listaInmuebles = new ArrayList<>(); //Lista inmuebles
    private String nombre; //Nombre de la inmobiliaria
    private String CIF; //Identificador de la inmobiliaria
    
    /**
     * Constructor vacio
     */
    public inmobiliaria() {
        
    }

    public ArrayList<inmueble> getListaInmuebles() {
        return listaInmuebles;
    }

    public void setListaInmuebles(ArrayList<inmueble> listaInmuebles) {
        this.listaInmuebles = listaInmuebles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    @Override
    public String toString() {
        return "inmobiliaria{" + "listaInmuebles=" + listaInmuebles + ", nombre=" + nombre + ", CIF=" + CIF + '}';
    }
    
}
