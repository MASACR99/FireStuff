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
public abstract class inmueble {
    private static int last_id = 0;
    private int metros2;    //Metros cuadrados del inmueble
    private int precio;     //Precio del inmueble
    private String poblacion; //Ubicacion del inmueble
    private int id;      //Valor único identificador del inmueble

    /**
     * Constructor con todas las variables, excepto id que se asigna automaticamente
     * @param metros2
     * @param precio
     * @param poblacion
     * @param id 
     */
    public inmueble(int metros2, int precio, String poblacion) {
        this.metros2 = metros2;
        this.setPrecio(precio); //se usa setPrecio con el objetivo de evitr repetir el check
        this.poblacion = poblacion;
        this.setId(++last_id);
    }
    
    /**
     * Constructor vacio
     */
    public inmueble() {
    }
    
    public inmueble(inmueble inm_copia) {
        this.metros2 = inm_copia.getMetros2();
        this.precio = inm_copia.getPrecio();
        this.poblacion = inm_copia.getPoblacion();
        this.id = inm_copia.getId();
    }

    public int getMetros2() {
        return metros2;
    }

    public void setMetros2(int metros2) {
        this.metros2 = metros2;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        if (precio > 0){
            this.precio = precio;
        }else{
            System.out.println("El precio no puede ser 0 o inferior");
        }
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public int getId() {
        return id;
    }

    /**
     * Pone el valor automáticamente a id, usando el valor anterior
     */
    public void setAutoId(){
        this.setId(++last_id);
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public abstract inmueble solicitarDatos();
    
    public abstract float precioCompraventa();

    /**
     * Devuelve el string con todas las variables locales
     * @return 
     */
    @Override
    public String toString() {
        return "inmueble{" + "metros2=" + metros2 + ", precio=" + precio + ", poblacion=" + poblacion + ", id=" + id + '}';
    }
}
