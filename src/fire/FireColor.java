/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import static fire.Fire.MAXTEMPERATURE;
import java.awt.Color;

/**
 * TODO: Change color to receive any given amount via parameters (addColor()), keep in mind if there's no colors or there's no start/end point defaults shall be used.
 * @author masa
 */
public class FireColor {
    
    private Color[] colors = new Color[MAXTEMPERATURE];
    //Default colors
    private final Color STARTCOLOR = new Color(255,255,255,255);
    private final Color ENDCOLORWHITE = new Color(255,255,255,0); //White background
    private final Color ENDCOLORBLACK = new Color(0,0,0,255); //Black background
    private final Color MIDCOLOR = new Color(255,76,10,255);
    private final Color LOWMIDCOLORBLACK = new Color(255,231,67,255); //Black background (smoke will be white)
    private final Color LOWMIDCOLORWHITE = new Color(0,0,0,125); //White background (smoke will be black)
    private final Color HIGHMIDCOLOR = new Color(210,99,1,128);
    private boolean background = false; //true when background is black
    
    public FireColor(){
        if(background){
            colors[0] = ENDCOLORBLACK;
            colors[(int)(MAXTEMPERATURE/3)] = LOWMIDCOLORBLACK;
        }else{
            colors[0] = ENDCOLORWHITE;
            colors[(int)(MAXTEMPERATURE/3)] = LOWMIDCOLORWHITE;
        }
        colors[MAXTEMPERATURE-1] = STARTCOLOR;
        colors[(int)(MAXTEMPERATURE/2)] = MIDCOLOR;
        colors[(int)(MAXTEMPERATURE*2/3)] = HIGHMIDCOLOR;
    }
    
    public void insert(int position, Color color){
        if(position < MAXTEMPERATURE && position >= 0){
            colors[position] = color;
        }
    }
    
    public void changeBackground(){
        colors = new Color[MAXTEMPERATURE];
        background = !background;
        if(background){
            colors[0] = ENDCOLORBLACK;
            colors[(int)(MAXTEMPERATURE/3)] = LOWMIDCOLORBLACK;
        }else{
            colors[0] = ENDCOLORWHITE;
            colors[(int)(MAXTEMPERATURE/3)] = LOWMIDCOLORWHITE;
        }
        colors[MAXTEMPERATURE-1] = STARTCOLOR;
        colors[(int)(MAXTEMPERATURE/2)] = MIDCOLOR;
        colors[(int)(MAXTEMPERATURE*2/3)] = HIGHMIDCOLOR;
        interpolate();
    }
    
    /**
     * Will calculate all the values of color doing a smooth effect for each temperature.
     */
    public void interpolate(){
        //Get the positions to calculate between
        int[] starts = {0,(int)(MAXTEMPERATURE/3),(int)(MAXTEMPERATURE/2),(int)(MAXTEMPERATURE*2/3)};
        int[] ends = {(int)(MAXTEMPERATURE/3),(int)(MAXTEMPERATURE/2),(int)(MAXTEMPERATURE*2/3),MAXTEMPERATURE-1};
        for(int i = 0; i < starts.length; i++){
            interpolateBetween(starts[i],ends[i]);
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
    
}
