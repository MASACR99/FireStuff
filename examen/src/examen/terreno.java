/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen;

/**
 *
 * @author masa
 */
public class terreno extends inmueble{
    public enum Calificacion {
        Rústico, Urbano, Urbanizable
    }
    Calificacion cali;
    
    public terreno() {
    }

    public Calificacion getClase() {
        return cali;
    }

    public void setClase(Calificacion clase) {
        this.cali = clase;
    }

    @Override
    public String toString() {
        return "terreno{" + "cali=" + cali + '}';
    }
}
