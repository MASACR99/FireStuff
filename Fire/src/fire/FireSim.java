/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import static fire.Fire.MAXTEMPERATURE;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * TODO:
 * - Add X axis bound for hot and cold
 * - Add wind (bruh)
 * @author masa
 */
public class FireSim extends BufferedImage implements Runnable {
    
    private int fuelBoundsMin = 1; //Height of the posible fuel positions
    private int maxFuel; //Maximmum ammount of hot spots
    private int coldSpotBounds = 1; //Height of the posible fuel spots
    private int maxCold; //Maximmum ammount of cold spots
    private float delta = 0.1f; //Ammount of heat lost each exchange
    private float divVal = 2f; //Division value
    private float wind = 0f; //Bias to the divVal to simulate wind
    private int[][] temps;
    private float[][] weights = new float[3][2]; //Weights of pixels for each calculation
    private FireColor color;
    
    public FireSim(int width, int height, FireColor color){
        //Call buffered Image constructor with empty values
        super(width,height,TYPE_INT_ARGB);
        //Define tempertures matrix and initialize to 0
        temps = new int[this.getWidth()][this.getHeight()];
        for(int i = 0; i < temps.length; i++){
            for(int j = 0; j < temps[0].length; j++){
                temps[i][j] = 0;
            }
        }
        maxFuel = 50;
        maxCold = (int)maxFuel;
        this.color = color;
        weights[0][0] = 0.2f;
        weights[2][0] = 0.2f;
        weights[1][0] = 0.5f;
        weights[0][1] = 0.3f;
        weights[1][1] = 0.5f;
        weights[2][1] = 0.3f;
    }
    
    /**
     * Does everything related to simulating and painting the fire
     */
    public void simulate(){
        //1- Choose random fire starting positions
        //2- Choose random cold spots
        //3- Calculate all fire
        //4- Get color for each temperature
        //5- Paint
        int fuel;
        int cold;
        Random ran = new Random();
        fuel = ran.nextInt(maxFuel);
        cold = ran.nextInt(maxCold);
        for (int i = 0; i<fuel; i++){
            temps[ran.nextInt(temps.length)][temps[0].length - calcFuel()] = MAXTEMPERATURE-1;
        }
        for (int i = 0; i<cold; i++){
            temps[ran.nextInt(temps.length)][temps[0].length - calcCold()] = 0;
        }
        for(int i = 0; i<temps.length;i++){
            for(int j = 0; j<temps[0].length; j++){
                calcCoord(i,j);
            }
        }
        for(int i = 0; i<temps.length;i++){
            for(int j = 0; j<temps[0].length; j++){
                paint(i,j);
            }
        }
    }
    
    private int calcFuel(){
        int returnVal;
        Random ran = new Random();
        if(fuelBoundsMin >= 2){
            returnVal = (ran.nextInt(fuelBoundsMin));
            while (temps[0].length - returnVal < 0 || temps[0].length - returnVal >= temps[0].length){
                returnVal = (ran.nextInt(fuelBoundsMin));
            } 
        }else{
            returnVal = 1;
        }
        return returnVal;
    }
    
    private int calcCold(){
        int returnVal;
        Random ran = new Random();
        if(coldSpotBounds >= 2){
            returnVal = (ran.nextInt(coldSpotBounds));
            while (temps[0].length - returnVal < 0 || temps[0].length - returnVal >= temps[0].length){
                returnVal = (ran.nextInt(coldSpotBounds));
            } 
        }else{
            returnVal = 1;
        }   
        return returnVal;
    }
    
    /**
     * Based on some values it calculates the actual value of one pixel
     * @param i
     * @param j 
     */
    private void calcCoord(int i, int j){
        if (j < temps[0].length-1){
                if (i == 0){
                    temps[i][j] = (int)(((temps[i][j+1]*weights[1][1] + temps[i+1][j+1]*weights[2][1] + temps[i][j]*weights[1][0] + temps[i+1][j]*weights[2][0])/(divVal))+delta);
                }else if (i == temps.length-1){
                    temps[i][j] = (int)(((temps[i][j+1]*weights[1][1] + temps[i-1][j+1]*weights[0][1] + temps[i][j]*weights[1][0] + temps[i-1][j]*weights[0][0])/(divVal))+delta);
                }else{
                    temps[i][j] = (int)(((temps[i][j+1]*weights[1][1] + temps[i+1][j+1]*weights[2][1] + temps[i-1][j+1]*weights[0][1] + temps[i][j]*weights[1][0] + temps[i+1][j]*weights[2][0] + temps[i-1][j]*weights[0][0])/divVal)+delta);
                }
                if (temps[i][j] < 0){
                    temps[i][j] = 0;
                }
                if (temps[i][j] >= MAXTEMPERATURE){
                    temps[i][j] = MAXTEMPERATURE-1;
                }
        }else{
            temps[i][j] = temps[i][j-1]; //Just make the last line of pixels the same temperature as the above
        }
    }
    
    private void paint(int i, int j){
        try{
            this.setRGB(i, j, color.getARGB(temps[i][j]));
        }catch(Exception ex){
            System.out.println("Error putting a pixel on it's place");
        }
    }
    
    public FireSim resize(int x, int y){
        FireSim resized = new FireSim(x,y,color);
        int minX = x < temps.length ? x : temps.length;
        int minY = y < temps[0].length ? y : temps[0].length;
        for(int i = 0; i<minX;i++){
            for(int j = 0; j<minY;j++){
                resized.setCoord(i,j,temps[i][j]);
            }
        }
        resized.setDelta(delta);
        resized.setMaxCold(maxCold);
        resized.setMaxFuel(maxFuel);
        return resized;
    }

    //Keeping run and simulate separated gives me the ability to choose between threaded and non-threaded exections.
    @Override
    public void run() {
        simulate();
    }

    public void setFuelBoundsMin(int fuelBoundsMin) {
        this.fuelBoundsMin = fuelBoundsMin;
    }

    public void setMaxFuel(int maxFuel) {
        this.maxFuel = maxFuel;
    }

    public void setColdSpotBounds(int coldSpotBounds) {
        this.coldSpotBounds = coldSpotBounds;
    }

    public void setMaxCold(int maxCold) {
        this.maxCold = maxCold;
    }
    
    public void setDelta(float delta){
        this.delta = delta;
    }

    public int getMaxFuel() {
        return maxFuel;
    }

    public int getMaxCold() {
        return maxCold;
    }

    public float getDelta() {
        return delta;
    }

    public void setCoord(int i, int j, int val) {
        temps[i][j] = val;
    }

    public float getDivVal() {
        return divVal;
    }

    public void setDivVal(float divVal) {
        this.divVal = divVal;
    }

    public float[][] getWeights() {
        return weights;
    }

    public void setWeights(float[][] weights) {
        this.weights = weights;
    }
    
    public void updateWind(float wind){
        this.wind = wind;
        weights[0][0] = 0.2f + wind;
        weights[0][1] = 0.3f + wind;
        weights[2][0] = 0.2f - wind;
        weights[2][1] = 0.3f - wind;
    }
    
    public float getWind(){
        return this.wind;
    }
    
}
