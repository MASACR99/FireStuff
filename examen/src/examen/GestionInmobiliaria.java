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
    private static final Scanner sc = new Scanner(System.in);
    private static ArrayList<inmobiliaria> listaInmobiliaria = new ArrayList<>();

    public static void main(String[] args) {
        boolean found = false;
        System.out.println("Escribe el CIF de la inmobiliaria con la que quieras trabajar:");
        String inp = sc.nextLine();
        while(!found){
            for(int i = 0; (i<listaInmobiliaria.size())&&(found==false);i++){
                if(listaInmobiliaria.get(i).getCIF().equals(inp)){
                    found = true;
                }
            }
            System.out.println("No encontramos la inmobiliaria con CIF: "+ inp +". Prueba otra vez o escribe exit para salir");
            inp = sc.nextLine();
            if(inp.equals("exit")){
                System.out.println("Adios!");
                exit(0);    //Deberia apagar completamente el programa
            }
        }
        //Reutilizar variable booleana para el menu
        while(found){
            System.out.println("Que quieres hacer?");
            System.out.println("1- Añadir terreno");
            System.out.println("2- Añadir vivienda");
            System.out.println("3- Buscar vivienda");
            System.out.println("4- Buscar terreno");
            System.out.println("5- Salir");
            int input = sc.nextInt();
            sc.nextLine();
            switch(input){
                case 1:
                    //Llamar a terreno y añadirlo a la inmobiliaria
                    break;
                case 2:
                    //Llamar a vivienda y añadirlo a la inmobiliaria
                    break;
                case 3:
                    //Buscar un vivienda en especifico
                    break;
                case 4:
                    //Buscar un terreno en especifico
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
