/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examen;

import java.util.Scanner;

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
    
    /**
     * Solicita los datos necesarios al usuario para devolver una clase con todos los datos necesarios
     * @return 
     */
    @Override
    public inmueble solicitarDatos() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Que calificacion tiene el terreno?\n" + "1- Rústico\n" + "2- Urbano\n" + "3- Urbanizable");
        int input = sc.nextInt();
        sc.nextLine();
        Calificacion calif = Calificacion.values()[input-1]; //Deberia ser el valor correcto
        this.setCalificacion(calif);
        System.out.println("Escribe los metros cuadrados del terreno");
        this.setMetros2(sc.nextInt());
        sc.nextLine();
        System.out.println("Escribe la poblacion del terreno");
        this.setPoblacion(sc.nextLine());
        System.out.println("Escribe el precio del terreno");
        this.setPrecio(sc.nextInt());
        sc.nextLine();
        this.setAutoId();
        return this;
    }
    
    /**
     * Método que calcula el precio del terreno y lo devuelve, se usa unicamente en el metodo toString
     * @return 
     */
    @Override
    public float precioCompraventa() {
        float total = 0;
        int IVA = 0; //Se debe inicializar para evitar warnings
        //Intente realizar la siguiente linea:
        //Calificacion.valueOf(this.getCalificacion);
        //Con el objetivo de conseguir el valor int de la posicion en el enum
        //Pero no funciona, por ende termina siendo un switch como el que hay ahora
        switch(this.getCalificacion()){
            case Rústico:
                IVA = 4;
                break;
            case Urbano:
                IVA = 6;
                break;
            case Urbanizable:
                IVA = 8;
                break;
        }
        //El switch no necesita un default ya que al ser un enum no hay mas valores posibles
        total = this.getPrecio() + (this.getPrecio()*IVA/100);
        return total;
    }

    public Calificacion getCalificacion() {
        return cali;
    }

    public void setCalificacion(Calificacion clase) {
        this.cali = clase;
    }

    @Override
    public String toString() {
        return super.toString()+ " calificacion: "+ this.getCalificacion() + " precio compra-venta: "+this.precioCompraventa();
    }
}