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
public class vivienda extends inmueble{
    private int habitaciones;   //Numero de habitaciones del inmueble
    private String descripcion; //Descripcion de la vivienda

    /**
     * Constructor con todas las variables
     * @param habitaciones
     * @param descripcion
     * @param metros2
     * @param precio
     * @param poblacion 
     */
    public vivienda(int habitaciones, String descripcion, int metros2, int precio, String poblacion) {
        super(metros2, precio, poblacion);
        this.setHabitaciones(habitaciones);
        this.descripcion = descripcion;
    }
    
    /**
     * Constructor vacio
     */
    public vivienda(){
        
    }
    
    /**
     * Constructor copia
     * @param viv 
     */
    public vivienda(vivienda viv){
        this.descripcion = viv.getDescripcion();
        this.habitaciones = viv.getHabitaciones();
        super.setId(viv.getId());
        super.setMetros2(viv.getMetros2());
        super.setPoblacion(viv.getPoblacion());
        super.setPrecio(viv.getPrecio());
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        if (habitaciones > 0){
            this.habitaciones = habitaciones;
        }else{
            System.out.println("Debe haber almenos una habitacion");
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
