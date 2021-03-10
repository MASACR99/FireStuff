//Joan Gil Rigo - examen 1º DAM
package examen;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author masa
 */
public class GestionInmobiliaria {
    
    //Conjunto de prueba inicial, quitar comentarios si se quiere usar
    /*
    public static void iniciarInmobiliaria(ArrayList<inmobiliaria> listaInmobiliaria) {
        inmobiliaria inmoTemp;
        terreno terrenoTemp;
        vivienda viviendaTemp;
        for (int i=0;i<3;i++){
            inmoTemp=new inmobiliaria();
            inmoTemp.setCIF("Inmo0"+i);
            inmoTemp.setNombre("nombreInmo"+1);
            for (int j=0;j<3;j++){
                terrenoTemp = new terreno();
                terrenoTemp.setId(i+j);
                terrenoTemp.setMetros2(j*80);
                terrenoTemp.setPoblacion("poblac"+j);
                terrenoTemp.setPrecio(30000*(i+1));
                terrenoTemp.setCalificacion(terreno.Calificacion.Rústico);inmoTemp.getListaInmuebles().add(terrenoTemp);
                viviendaTemp = new vivienda();
                viviendaTemp.setId(i+j);
                viviendaTemp.setMetros2(j*80);
                viviendaTemp.setPoblacion("poblac"+j);
                viviendaTemp.setPrecio(30000*(i+1));
                viviendaTemp.setDescripcion("descripcion vivienda "+i);
                viviendaTemp.setHabitaciones(i+j+1);
                inmoTemp.getListaInmuebles().add(viviendaTemp);
            }
        listaInmobiliaria.add(inmoTemp);
        }
    }*/
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<inmobiliaria> listaInmobiliaria = new ArrayList<>();

    public static void main(String[] args) {
        //iniciarInmobiliaria(listaInmobiliaria); //Descomentar si se quiere usar el inicializador
        boolean found = false;
        inmobiliaria inmo_actual = null;
        System.out.println("Escribe el CIF de la inmobiliaria con la que quieras trabajar:");
        String inp = sc.nextLine();
        //Bucle para conseguir si o si un CIF valido o cerrar el programa
        while(!found){
            for(int i = 0; (i<listaInmobiliaria.size())&&(found==false);i++){
                if(listaInmobiliaria.get(i).getCIF().equals(inp)){
                    inmo_actual = listaInmobiliaria.get(i);
                    found = true;
                }
            }
            if(!found){
                System.out.println("No encontramos la inmobiliaria con CIF: "+ inp +". Prueba otra vez o escribe exit para salir");
                inp = sc.nextLine();
                if(inp.equals("exit")){
                    System.out.println("Adios!");
                    exit(0);    //Apaga completamende el programa, asi puedo reutilizar found para el proximo while
                }
            }
        }
        //Reutilizar variable booleana para el menu
        //Bucle para el siguiente menu
        while(found){
            System.out.println("Que quieres hacer?");
            System.out.println("1- Añadir terreno");
            System.out.println("2- Añadir vivienda");
            System.out.println("3- Buscar vivienda");
            System.out.println("4- Buscar terreno");
            System.out.println("5- Salir");
            int input = sc.nextInt();
            sc.nextLine();
            int newinput = 0;   //Se usa en las funciones 3 y 4
            boolean search = false; //Se usa en las funciones 3 y 4
            switch(input){
                case 1:
                    //Crear terreno y añadir a lista de la inmobiliaria
                    terreno terr = new terreno();
                    terr.solicitarDatos();
                    ArrayList<inmueble>listainmu = inmo_actual.getListaInmuebles();
                    listainmu.add(terr);
                    inmo_actual.setListaInmuebles(listainmu);
                    break;
                case 2:
                    //Crear vivienda y añadir a la lista de la inmobiliaria
                    vivienda viv = new vivienda();
                    viv.solicitarDatos();
                    ArrayList<inmueble>listainmo = inmo_actual.getListaInmuebles();
                    listainmo.add(viv);
                    inmo_actual.setListaInmuebles(listainmo);
                    break;
                case 3:
                    //Buscar un vivienda en especifico
                    System.out.println("Escribe el id de la vivienda");
                    newinput = sc.nextInt();
                    search = false;
                    sc.nextLine();
                    for(int i = 0; (i< inmo_actual.getListaInmuebles().size()) && (!search);i++){
                        if(inmo_actual.getListaInmuebles().get(i).getId()==newinput){
                            if(inmo_actual.getListaInmuebles().get(i) instanceof vivienda){ //No me dejo el IDE usar instaceof vivienda viv asi que se repite el get(i)
                                search = true;
                                System.out.println(inmo_actual.getListaInmuebles().get(i).toString());
                            }
                        }
                    }
                    if(!search){
                        System.out.println("No se encontró ninguna vivienda con id: " + newinput);
                    }
                    break;
                case 4:
                    //Buscar un terreno en especifico
                    System.out.println("Escribe el id del terreno");
                    newinput = sc.nextInt();
                    search = false;
                    sc.nextLine();
                    for(int i = 0; (i< inmo_actual.getListaInmuebles().size()) && (!search);i++){
                        if(inmo_actual.getListaInmuebles().get(i).getId()==newinput){
                            if(inmo_actual.getListaInmuebles().get(i) instanceof terreno){ //No me dejo el IDE usar instaceof terreno terr asi que se repite el get(i)
                                search = true;
                                System.out.println(inmo_actual.getListaInmuebles().get(i).toString());
                            }
                        }
                    }
                    if(!search){
                        System.out.println("No se encontró ningún terreno con id: " + newinput);
                    }
                    break;
                case 5:
                    found = false;
                    System.out.println("Adios!");
                    break;
                default:
                    System.out.println("Input no valido, prueba otra vez");
                    break;
            }
        }
    }
    
}
