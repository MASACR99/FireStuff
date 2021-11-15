/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fire;

import static fire.Fire.MAXFPS;
import static fire.Fire.MAXTEMPERATURE;
import java.awt.Canvas;
import java.awt.FlowLayout;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 * TODO: -Stats: - Number of cycles (fire and screen) - Add possibility to
 * change colors from the palette - Possibility to change weights
 *
 * @author masa
 */
public class ScreenManager extends JPanel implements Runnable {

    private FireSim fire;
    private JPanel panel1;
    private Canvas panel2;
    private Image background;
    private BufferStrategy bs;
    private FireColor color;
    private Thread fireThread;
    private String[] columnNames = {"Position", "Color"};
    private boolean run = true;
    private int screenX = 10;
    private int screenY = 10;
    private int shaderMode = 0;
    private String imageURL = "https://p4.wallpaperbetter.com/wallpaper/765/406/516/landscape-4k-bliss-windows-xp-wallpaper-preview.jpg";

    public ScreenManager() {
        super();
        try {
            background = Toolkit.getDefaultToolkit().createImage(new URL(imageURL));
        } catch (Exception ex) {
            System.out.println("Couldn't load background image :(");
        }
        this.color = new FireColor();
        this.color.interpolate();
        //Initialize the fire with at least size 5x5 (no special need for this, fire won't crash)
        if (this.getWidth() <= 0 || this.getHeight() <= 0) {
            fire = new FireSim(5, 5, color);
        } else {
            fire = new FireSim(this.getWidth(), this.getHeight(), color);
        }
        fireThread = new Thread(fire);
        fireThread.start();

        //Will probably need 2 JPanels inside here with gridbaglayout, panel 1
        //with everything for stats and control for the fire and panel 2
        //with the fire, will also probably run threaded if it works correctly
        //may also need to implement the double buffering solution for that
        //all of the above are implemented and working as expected
        //Define panels
        panel1 = new JPanel();
        panel2 = new Canvas();
        panel1.setSize(100, 500);
        panel2.setSize(500, 500);

        panel1.setLayout(new GridBagLayout());

        //Define panel1 components
        JTable table = new JTable(new DefaultTableModel());
        DefaultTableModel data = (DefaultTableModel) table.getModel();
        data.setColumnCount(2);
        data.addColumn(columnNames);
        Object[][] colorData = color.getPresetColorData();
        String[] parsedData = new String[2];
        for (int i = 0; i < colorData.length; i++) {
            parsedData[0] = String.valueOf((int) colorData[i][0]);
            parsedData[1] = String.valueOf(colorData[i][1]);
            data.addRow(new Object[]{parsedData[0], parsedData[1]});
            parsedData = new String[2];
        }
        data.setColumnIdentifiers(columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JPanel smallButtons = new JPanel();
        smallButtons.setLayout(new FlowLayout());
        JButton add = new JButton("Add color");
        JButton del = new JButton("Del color");
        smallButtons.add(add);
        smallButtons.add(del);
        JLabel hotLabel = new JLabel("Number of hot spots");
        JSpinner hotSpots = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        hotSpots.setValue(fire.getMaxFuel());
        JLabel coldLabel = new JLabel("Number of cold spots");
        JSpinner coldSpots = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        coldSpots.setValue(fire.getMaxCold());
        JLabel deltaLabel = new JLabel("Delta:");
        JSpinner delta = new JSpinner(new SpinnerNumberModel(0.01f, -10, 10, 0.001f));
        delta.setValue(fire.getDelta());
        JLabel divLabel = new JLabel("Division value:");
        JSpinner div = new JSpinner(new SpinnerNumberModel(0.01f, 0.01f, 10, 0.001f));
        div.setValue(fire.getDivVal());
        JLabel windLabel = new JLabel("Wind value:");
        JSpinner wind = new JSpinner(new SpinnerNumberModel(0.01f, -1f, 1f, 0.001f));
        wind.setValue(fire.getWind());
        JLabel panelFrames = new JLabel("Windows fps:");
        JSpinner panel = new JSpinner(new SpinnerNumberModel(20, 1, 500, 1));
        panel.setValue(MAXFPS);
        JLabel fireFrames = new JLabel("Game fps:");
        JSpinner fireF = new JSpinner(new SpinnerNumberModel(20, 1, 500, 1));
        fireF.setValue(fire.getFPS());

        JToggleButton backgroundColor = new JToggleButton("Cycle shaders");
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
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Choose a color and the temperature associated");
                JSpinner temperature = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
                JColorChooser chose = new JColorChooser();
                panel.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 2;
                c.gridheight = 1;
                c.fill = GridBagConstraints.BOTH;
                panel.add(label);
                c.gridy = 1;
                c.gridwidth = 1;
                panel.add(temperature);
                c.gridx = 1;
                panel.add(chose);
                int result = JOptionPane.showConfirmDialog(null, panel, "Please add the necessary values", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    color.insert(Integer.valueOf(String.valueOf(temperature.getValue())), chose.getColor());
                    //check if color exists already and update, if it doesn't add it where it has to be
                    int value = Integer.valueOf(String.valueOf(temperature.getValue()));
                    boolean found = false;
                    String colorString = new String(chose.getColor().getAlpha() + ", " + chose.getColor().getRed() + ", " + chose.getColor().getGreen() + ", " + chose.getColor().getBlue());
                    for (int i = 0; i < data.getRowCount() && !found; i++) {
                        System.out.println((String)data.getValueAt(i, 0));
                        if (Integer.parseInt((String) data.getValueAt(i, 0)) == value) {
                            found = true;
                            data.removeRow(i);
                            data.insertRow(i, new Object[]{String.valueOf(value), colorString});
                        } else if (i + 1 < data.getRowCount()) {
                            if (value > Integer.parseInt((String) data.getValueAt(i, 0)) && value < Integer.parseInt((String) data.getValueAt(i + 1, 0))) {
                                data.insertRow(i + 1, new Object[]{String.valueOf(value), colorString});
                                found = true;
                            }
                        }
                    }
                    //update jtable
                }
            }
        });
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Choose the temperature you wanna delete");
                JSpinner temperature = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
                panel.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 2;
                c.gridheight = 1;
                c.fill = GridBagConstraints.BOTH;
                panel.add(label);
                c.gridy = 1;
                c.gridwidth = 1;
                panel.add(temperature);
                int result = JOptionPane.showConfirmDialog(null, panel, "Please add the necessary values", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int value = Integer.valueOf(String.valueOf(temperature.getValue()));
                    color.deletePresetColor(value);
                    if (value != 0 && value != MAXTEMPERATURE) {
                        for (int i = 0; i < data.getRowCount(); i++) {
                            if (Integer.parseInt((String) data.getValueAt(i, 0)) == value) {
                                data.removeRow(i);
                            }
                        }
                    }
                }
            }
        });
        hotSpots.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setMaxFuel((int) hotSpots.getValue());
            }
        });
        coldSpots.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setMaxCold((int) coldSpots.getValue());
            }
        });
        delta.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setDelta(Float.valueOf(String.valueOf(delta.getValue())));
            }
        });
        div.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setDivVal(Float.valueOf(String.valueOf(div.getValue())));
            }
        });
        wind.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.updateWind(Float.valueOf(String.valueOf(wind.getValue())));
            }
        });
        panel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                MAXFPS = Integer.valueOf(String.valueOf(panel.getValue()));
            }
        });
        fireF.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Put new value into fire
                fire.setFPS(Integer.valueOf(String.valueOf(fireF.getValue())));
            }
        });
        backgroundColor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //Call change background, if the fire is being updated then pause it
                //change the color and finally resume
                try {
                    shaderMode++;
                    if (shaderMode > 3) {
                        shaderMode = 0;
                    }
                } catch (Exception ex) {

                }
            }
        });
        imagePath.getDocument().addDocumentListener(new DocumentListener() {
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
                    try {
                        background = ImageIO.read(new File(imagePath.getText()));
                    } catch (Exception err) {
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
                        extension = selectedFile.getName().substring(i + 1);
                    }
                    if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("webp")) {
                        imagePath.setText(selectedFile.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(panel2, "Image not supported or not an image", "File warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                run = true;
                fire.start();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                run = false;
                fire.pause();
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                fireThread.interrupt();
                fire = new FireSim(screenX, screenY, color);
                hotSpots.setValue(fire.getMaxFuel());
                coldSpots.setValue(fire.getMaxCold());
                delta.setValue(fire.getDelta());
                div.setValue(fire.getDivVal());
                wind.setValue(fire.getWind());
                fireF.setValue(fire.getFPS());
                fireThread = new Thread(fire);
                fireThread.start();
            }
        });
        //add resize listener to panel2 to resize fire as the panel resizes
        panel2.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                //Do nothing
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //Do nothing
            }

            @Override
            public void componentResized(ComponentEvent e) {
                //Change to fire.resize to avoid having problems with other components/variables
                screenX = e.getComponent().getWidth();
                screenY = e.getComponent().getHeight();
                fire = fire.resize(screenX, screenY);
                fireThread = new Thread(fire);
                fireThread.start();
            }

            @Override
            public void componentShown(ComponentEvent e) {
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
        c.ipadx = 50;
        c.fill = GridBagConstraints.BOTH;
        panel1.add(table, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        panel1.add(smallButtons, c);
        c.fill = GridBagConstraints.NONE;
        c.gridy = 2;
        panel1.add(hotLabel, c);
        c.ipadx = 15;
        c.gridy = 3;
        panel1.add(hotSpots, c);
        c.ipadx = 0;
        c.gridy = 4;
        panel1.add(coldLabel, c);
        c.ipadx = 15;
        c.gridy = 5;
        panel1.add(coldSpots, c);
        c.ipadx = 0;
        c.gridy = 6;
        panel1.add(deltaLabel, c);
        c.ipadx = 15;
        c.gridy = 7;
        panel1.add(delta, c);
        c.gridy = 8;
        c.ipadx = 0;
        panel1.add(divLabel, c);
        c.ipadx = 15;
        c.gridy = 9;
        panel1.add(div, c);
        c.gridy = 10;
        c.ipadx = 0;
        panel1.add(windLabel, c);
        c.gridy = 11;
        c.ipadx = 15;
        panel1.add(wind, c);
        c.gridy = 12;
        c.ipadx = 0;
        panel1.add(panelFrames, c);
        c.gridy = 13;
        c.ipadx = 15;
        panel1.add(panel, c);
        c.gridy = 14;
        c.ipadx = 0;
        panel1.add(fireFrames, c);
        c.gridy = 15;
        c.ipadx = 15;
        panel1.add(fireF, c);
        c.gridy = 16;
        c.ipadx = 0;
        panel1.add(backgroundColor, c);
        c.gridy = 17;
        panel1.add(backgroundImage, c);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        imageLoaderPanel.add(imagePath, c);
        c.weightx = 0;
        c.gridx = 1;
        imageLoaderPanel.add(loadButton, c);
        c.gridx = 0;
        c.gridy = 18;
        panel1.add(imageLoaderPanel, c);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 5;
        buttons.add(start, c);
        c.gridx = 1;
        buttons.add(stop, c);
        c.gridx = 2;
        buttons.add(restart, c);
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy = 19;
        panel1.add(buttons, c);

        //Add both panels
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 6;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.VERTICAL;
        this.add(panel1, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 6;
        c.gridwidth = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(panel2, c);
    }

    /**
     * Starts the buffer on the panel
     */
    public void startBuffer() {
        panel2.createBufferStrategy(2);
        bs = panel2.getBufferStrategy();
        fireThread = new Thread(fire);
        fireThread.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics canvasG = bs.getDrawGraphics();
        try {
            if (run) {
                canvasG.clearRect(0, 0, panel2.getWidth(), panel2.getHeight());
                //fire.simulate(); //Run fire without a thread
            }
            panel1.repaint();
            canvasG.drawImage(background, 0, 0, panel2.getSize().width, panel2.getSize().height, this);
            switch (shaderMode) {
                case 0:
                    canvasG.drawImage(fire, 0, 0, this);
                    break;
                case 1:
                    BufferedImage grayImage = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                    grayImage.createGraphics().drawImage(fire, 0, 0, this);
                    canvasG.drawImage(grayImage, 0, 0, this);
                    break;
                case 2:
                    BufferedImage blackAndWhite = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
                    blackAndWhite.createGraphics().drawImage(fire, 0, 0, this);
                    canvasG.drawImage(blackAndWhite, 0, 0, this);
                    break;
                case 3:
                    BufferedImage randomish = new BufferedImage(fire.getWidth(), fire.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    randomish.createGraphics().drawImage(fire, 0, 0, this);
                    canvasG.drawImage(randomish, 0, 0, this);
                    break;
            }
            bs.show();
        } catch (Exception ex) {
            System.out.println("Error loading 1 frame");
        }
    }

    @Override
    public void run() {
        paint(bs.getDrawGraphics());
    }
}
