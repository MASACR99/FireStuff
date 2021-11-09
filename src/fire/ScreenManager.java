/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * TODO:
 * -Add wind and change max Height
 * @author masa
 */
public class ScreenManager extends JPanel {
    
    private FireSim fire;
    private JPanel panel1;
    private Canvas panel2;
    private Image background;
    private BufferStrategy bs;
    private FireColor color;
    private Thread fireThread;
    private boolean run = true;
    private int screenX = 10;
    private int screenY = 10;
    private String imageURL = "https://p4.wallpaperbetter.com/wallpaper/765/406/516/landscape-4k-bliss-windows-xp-wallpaper-preview.jpg";
    
    public ScreenManager(){
        super();
        try{
            background = Toolkit.getDefaultToolkit().createImage(new URL(imageURL));
        }catch(Exception ex){
            System.out.println("Couldn't load background image :(");
        }
        this.color = new FireColor();
        this.color.interpolate();
        //Initialize the fire with at least size 5x5 (no special need for this, fire won't crash)
        if(this.getWidth() <= 0 || this.getHeight() <= 0){
            fire = new FireSim(5,5,color);
        }else{
            fire = new FireSim(this.getWidth(),this.getHeight(),color);
        }
        //Will probably need 2 JPanels inside here with gridbaglayout, panel 1
        //with everything for stats and control for the fire and panel 2
        //with the fire, will also probably run threaded if it works correctly
        //may also need to implement the double buffering solution for that
        //Define panels
        panel1 = new JPanel();
        panel2 = new Canvas();
        panel1.setSize(100,500);
        panel2.setSize(500,500);
        
        panel1.setLayout(new GridBagLayout());
        
        //Define panel1 components
        JLabel hotLabel = new JLabel("Number of hot spots");
        JSpinner hotSpots = new JSpinner(new SpinnerNumberModel(1,1,500,1));
        hotSpots.setValue(fire.getMaxFuel());
        JLabel coldLabel = new JLabel("Number of cold spots");
        JSpinner coldSpots = new JSpinner(new SpinnerNumberModel(1,1,500,1));
        coldSpots.setValue(fire.getMaxCold());
        JLabel deltaLabel = new JLabel("Delta:");
        JSpinner delta = new JSpinner(new SpinnerNumberModel(0.01f,-10,10,0.001f));
        delta.setValue(fire.getDelta());
        JLabel divLabel = new JLabel("Division value:");
        JSpinner div = new JSpinner(new SpinnerNumberModel(0.01f,0.01f,10,0.001f));
        div.setValue(fire.getDivVal());
        JLabel windLabel = new JLabel("Wind value:");
        JSpinner wind = new JSpinner(new SpinnerNumberModel(0.01f,-1f,1f,0.001f));
        wind.setValue(fire.getWind());
        JToggleButton backgroundColor = new JToggleButton("Cycle background transp.");
        JLabel backgroundImage = new JLabel("Load local or online image");
        JTextField imagePath = new JTextField();
        JButton loadButton = new JButton("...");
        JPanel imageLoaderPanel = new JPanel();
        imageLoaderPanel.setLayout(new GridBagLayout());
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        JButton start = new JButton("Start");
        JButton stop = new JButton("Stop");
        JButton restart = new JButton("Restart");
        
        //Add all needed listeners
        hotSpots.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setMaxFuel((int) hotSpots.getValue());
            }
        });
        coldSpots.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setMaxCold((int) coldSpots.getValue());
            }
        });
        delta.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setDelta(Float.valueOf(String.valueOf(delta.getValue())));
            }
        });
        div.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setDivVal(Float.valueOf(String.valueOf(div.getValue())));
            }
        });
        wind.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.updateWind(Float.valueOf(String.valueOf(wind.getValue())));
            }
        });
        backgroundColor.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                //Call change background, if the fire is being updated then pause it
                //change the color and finally resume
                try{
                    if(fireThread.isAlive()){
                        fire.wait();
                        color.changeBackground();
                        fireThread.notify();
                    }else{
                        color.changeBackground();
                    }
                }catch(Exception ex){
                    
                }
            }
        });
        imagePath.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    URL url = new URL(imagePath.getText());
                    background = Toolkit.getDefaultToolkit().createImage(url);
                } catch (MalformedURLException ex) {
                    try{
                        background = ImageIO.read(new File(imagePath.getText()));
                    }catch(Exception err){
                        JOptionPane.showMessageDialog(panel2, "Error while loading image", "File warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    File selectedFile;
                    String extension = null;
                    int returnValue = chooser.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedFile = chooser.getSelectedFile();
                        int i = selectedFile.getName().lastIndexOf('.');
                        if (i > 0) {
                            extension = selectedFile.getName().substring(i+1);
                        }
                        if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("webp")){
                            imagePath.setText(selectedFile.getAbsolutePath());
                        }else{
                            JOptionPane.showMessageDialog(panel2, "Image not supported or not an image", "File warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });
        start.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a){
                run = true;
            }
        });
        stop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a){
                run = false;
            }
        });
        restart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent a){
                fire = new FireSim(screenX,screenY,color);
                hotSpots.setValue(fire.getMaxFuel());
                coldSpots.setValue(fire.getMaxCold());
                delta.setValue(fire.getDelta());
                div.setValue(fire.getDivVal());
                wind.setValue(fire.getWind());
            }
        });
        //add resize listener to panel2 to resize fire as the panel resizes
        panel2.addComponentListener(new ComponentListener(){
            @Override
            public void componentHidden(ComponentEvent e){
                //Do nothing
            }
            @Override
            public void componentMoved(ComponentEvent e){
                //Do nothing
            }
            @Override
            public void componentResized(ComponentEvent e){
                //Change to fire.resize to avoid having problems with other components/variables
                screenX = e.getComponent().getWidth();
                screenY = e.getComponent().getHeight();
                fire = fire.resize(screenX, screenY);
                startBuffer();
            }
            @Override
            public void componentShown(ComponentEvent e){
                //Do nothing
            }
        });
        //Define gridbagcontraints
        GridBagConstraints c = new GridBagConstraints();
        //Add GridBagLayout
        this.setLayout(new GridBagLayout());
        
        //Add everything to each panel
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 0;
        panel1.add(hotLabel,c);
        c.ipadx = 15;
        c.gridy = 2;
        panel1.add(hotSpots,c);
        c.ipadx = 0;
        c.gridy = 3;
        panel1.add(coldLabel,c);
        c.ipadx = 15;
        c.gridy = 4;
        panel1.add(coldSpots,c);
        c.ipadx = 0;
        c.gridy = 5;
        panel1.add(deltaLabel,c);
        c.ipadx = 15;
        c.gridy = 6;
        panel1.add(delta,c);
        c.gridy = 7;
        c.ipadx = 0;
        panel1.add(divLabel,c);
        c.ipadx = 15;
        c.gridy = 8;
        panel1.add(div,c);
        c.gridy = 9;
        c.ipadx = 0;
        panel1.add(windLabel,c);
        c.gridy = 10;
        c.ipadx = 15;
        panel1.add(wind,c);
        c.gridy = 11;
        c.ipadx = 0;
        panel1.add(backgroundColor,c);
        c.gridy = 12;
        panel1.add(backgroundImage,c);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        imageLoaderPanel.add(imagePath,c);
        c.weightx = 0;
        c.gridx = 1;
        imageLoaderPanel.add(loadButton,c);        
        c.gridx = 0;
        c.gridy = 13;
        panel1.add(imageLoaderPanel,c);        
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 5;
        buttons.add(start,c);
        c.gridx = 1;
        buttons.add(stop,c);
        c.gridx = 2;
        buttons.add(restart,c);        
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy = 14;
        panel1.add(buttons,c);
        
        //Add both panels
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 6;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.VERTICAL;
        this.add(panel1,c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 6;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(panel2,c);
    }
    
    /**
     * Starts the buffer on the panel
     */
    public void startBuffer(){
        panel2.createBufferStrategy(2);
        bs = panel2.getBufferStrategy();
    }
    
    
    @Override
    public void paint(Graphics g){
        Graphics canvasG = bs.getDrawGraphics();
        try{
            if(run){
                canvasG.clearRect(0, 0, panel2.getWidth(), panel2.getHeight());
                fireThread = new Thread(fire);
                fireThread.start(); //Try and run threaded
                //fire.simulate(); //Run without a thread
            }
            panel1.repaint();
            canvasG.drawImage(background, 0 ,0 , panel2.getSize().width, panel2.getSize().height, this);
            canvasG.drawImage(fire, 0, 0, this);
            while(fireThread.isAlive()){
                //wait for thread to finish
            }
            bs.show();
        }catch(Exception ex){
             System.out.println("Error loading 1 frame");
        }
    }
}
