/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import static fire.Fire.MAXTEMPERATURE;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO: Change color to receive any given amount via parameters (addColor()), keep in mind if there's no colors or there's no start/end point defaults shall be used.
 * @author masa
 */
public class FireColor {
    
    private Color[] colors = new Color[MAXTEMPERATURE];
    //Default colors
    private Map<Integer, Color> preMadeColors = new HashMap<Integer,Color>();
    private final Color STARTCOLOR = new Color(255,255,255,255);
    private final Color ENDCOLOR = new Color(255,255,255,0);
    private final Color MIDCOLOR = new Color(255,76,10,255);
    private final Color LOWMIDCOLOR = new Color(0,0,0,125); //White background (smoke will be black)
    private final Color HIGHMIDCOLOR = new Color(210,99,1,128);
    
    public FireColor(){
        preMadeColors.put(0,ENDCOLOR);
        preMadeColors.put((int)MAXTEMPERATURE/3,LOWMIDCOLOR);
        preMadeColors.put((int)MAXTEMPERATURE/2,MIDCOLOR);
        preMadeColors.put((int)MAXTEMPERATURE*2/3,HIGHMIDCOLOR);
        preMadeColors.put(MAXTEMPERATURE-1,STARTCOLOR);
    }
    
    /**
     * Puts a new value into the premade values, it can overwrite other values
     * @param position
     * @param color 
     */
    public void insert(int position, Color color){
        //First insert color
        if(position < MAXTEMPERATURE && position >= 0){
            preMadeColors.put(position, color);
        }
        interpolate();
    }
    
    public void deletePresetColor(int position){
        if(preMadeColors.containsKey(position) && position != 0 && position != MAXTEMPERATURE - 1){
            preMadeColors.remove(position);
        }
        interpolate();
    }
    
    public Object[][] getPresetColorData(){
        Object[][] returnVals = new Object[preMadeColors.size()][2];
        int count = 0;
        //Get the values in an ordered manner
        SortedSet<Integer> keys = new TreeSet<>(preMadeColors.keySet());
        for(Integer key : keys){
            returnVals[count][0] = key;
            returnVals[count][1] = preMadeColors.get(key).getAlpha() + "," + preMadeColors.get(key).getRed() + ", " + preMadeColors.get(key).getGreen() + ", " + preMadeColors.get(key).getBlue();
            count++;
        }
        return returnVals;
    }
    
    /**
     * Will calculate all the values of color doing a smooth effect for each temperature.
     */
    public void interpolate(){
        int[] starts = new int[preMadeColors.size()-1];
        int[] ends = new int[preMadeColors.size()-1];
        //Clear all colors
        for(int i=0;i<MAXTEMPERATURE;i++){
            colors[i] = null;
        }
        int counter = 0;
        //Place all values
        SortedSet<Integer> keys = new TreeSet<>(preMadeColors.keySet());
        for(Integer key : keys){
            colors[key] = preMadeColors.get(key);
            if(counter == 0){
                starts[counter] = key;
            }else if(counter == preMadeColors.size() - 1){
                ends[counter - 1] = key;
            }else{
                starts[counter] = key;
                ends[counter - 1] = key;
            }
            counter++;
        }
        //Get the positions to calculate between
        for(int i = 0; i < starts.length; i++){
            interpolateBetween(starts[i],ends[i]);
        }
    }
    
    /**
     * Returns the color corresponding to the temperature X
     * @param x
     * @return
     * @throws Exception 
     */
    public int getARGB(int x) throws Exception{
        if(x < 0 || x >= colors.length){
            throw new Exception("Out of bounds");
        }else{
            int returner = 0;
            returner = colors[x].getAlpha() << 24 | colors[x].getRed() << 16 | colors[x].getGreen() << 8 | colors[x].getBlue();
            return returner;
        }
    }
    
    /**
     * Interpolates all colors between 2 values.
     * Implemented using floats to have better precision and get smoother interpolations
     * @param start
     * @param end 
     */
    private void interpolateBetween(int start, int end){
        float red;
        float green;
        float blue;
        float alpha;
        float last_red;
        float last_green;
        float last_blue;
        float last_alpha;
        red = (colors[end].getRed()-colors[start].getRed())/(end-start);
        green = (colors[end].getGreen()-colors[start].getGreen())/(end-start);
        blue = (colors[end].getBlue()-colors[start].getBlue())/(end-start);
        alpha = (colors[end].getAlpha()-colors[start].getAlpha())/(end-start);
        last_red = colors[start].getRed();
        last_green = colors[start].getGreen();
        last_blue = colors[start].getBlue();
        last_alpha = colors[start].getAlpha();
        for(int i = start+1; i<end;i++){
            try{
                last_red += red;
                last_green += green;
                last_blue += blue;
                last_alpha += alpha;
                colors[i] = new Color((int)last_red,(int)last_green,(int)last_blue,(int)last_alpha);
            }catch(Exception ex){
                System.out.println("Something went wrong");
                System.out.println("Red: " + (colors[i-1].getRed()-red));
                System.out.println("Green: " + (colors[i-1].getGreen()-green));
                System.out.println("Blue: " + (colors[i-1].getBlue()-blue));
                System.out.println("Alpha: " + (colors[i-1].getAlpha()-alpha));
            }
        }
    }
}
