/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 *
 * @author masa
 */
public class Fire extends JFrame{

    public static final int MAXTEMPERATURE = 256;
    private final static int MAXFPS = 60;
    
    public Fire(String uwu){
        super(uwu);
        float mspf = 1000/MAXFPS;
        
        ScreenManager man = new ScreenManager();
        this.setLayout(new BorderLayout());
        
        this.setSize(500,500);
        this.add(man);
        this.setIconImage(new ImageIcon("/home/masa/Downloads/index.png").getImage());
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        long time1 = 0;
        long time2 = 0;
        man.startBuffer();
        while(true){
            time1 = System.currentTimeMillis();
            while(time2-time1 < mspf){
                time2 = System.currentTimeMillis();
            }
            man.paint(this.getGraphics());
            time2 = System.currentTimeMillis();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Fire fire = new Fire("Firey stuff!");
    }
    
}
