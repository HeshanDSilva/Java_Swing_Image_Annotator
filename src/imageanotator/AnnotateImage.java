package imageanotator;

import java.awt.Color;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

public class AnnotateImage{
    JFrame myMainWindow = new JFrame("Image Annotator");
    public JPanel image_pane = new JPanel();
    //get to center the jframe
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    // Loaded image will be stored in this variable
    File img_file;
    //The Image that we are loading store in this variable
    BufferedImage  img_buffer;
    //The Image showing in the window is stored here
    ImageIcon img;
    //getting the data calss
    Data_Holder data = Data_Holder.get_data_holder();
    //Components
    JLabel image_view = new JLabel();
    JButton brows_button = new JButton();
    JButton save_button = new JButton();
    
    //Kep a copy of this class to manage remove annotions
    private AnnotateImage self = this;

    public void runGUI(){
        
        myMainWindow.setLocation((dim.width/2 - 500), (dim.height/2 - 500));
        myMainWindow.setSize(1000, 1000);// defin initial point of the window
        myMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Call reset pane method
        resize_pane(null);
        myMainWindow.getContentPane().add(image_pane);
        
        brows_button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int res = chooser.showOpenDialog(null);
                // We have an image!
                try {
                    if (res == JFileChooser.APPROVE_OPTION) {
                        img_file = chooser.getSelectedFile();
                        //Clear the pane before change the image
                        for( Annotate_Symbol annotation : data.annotations){
                            image_pane.remove(annotation);
                        }  
                        image_pane.repaint();
                        myMainWindow.repaint();
                        data.annotations.clear();
                        resize_pane(img_file);
                    } else {
                        JOptionPane.showMessageDialog(null,
                        "You must select one image to be the reference.", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception iOException) {}                
            }
            
        });
        
        save_button.addActionListener((ActionEvent e) -> {
            try {
                
                Graphics graphics = img_buffer.getGraphics();
                // Draw remaining annotaions on the image
                for( Annotate_Symbol annotation : data.annotations){
                    graphics.setColor(Color.YELLOW);
                    //Draw the circle
                    graphics.drawOval(annotation.getX(), annotation.getY(), 20, 20);
                    // Draw the string
                    graphics.drawString(annotation.getLabel(), annotation.getX() + 20, annotation.getY() + 30);
                    image_pane.remove(annotation);
                }  
                // Repaint the frame and panel to show annotations
                image_pane.repaint();
                myMainWindow.repaint();
                data.annotations.clear();
                ImageIO.write(img_buffer,"jpeg", new File("Annotated//" + img_file.getName()));
                JOptionPane.showMessageDialog(null,
                        "Image saved successfully", "Completed",
                        JOptionPane.INFORMATION_MESSAGE);
                // Call reset pane method
                resize_pane(null);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null,
                        "Couldn't save try again", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
            }            
        });
        
        // Add the mouse click listner to the image in order to get the location
        image_pane.addMouseListener(new MouseListener(){
            // get the x, y location of the mouse click
            @Override
            public void mouseClicked(MouseEvent e) {
                
                // Open the JOptionPane to get user inputs
                String m = JOptionPane.showInputDialog("Enter annotaion text", "Might be of interest");      
                //Check whether user given some input or not if yes set the anotation
                if(m != null){                    
                    int x = e.getX();
                    int y = e.getY();
                    Annotate_Symbol point = new Annotate_Symbol(m, x, y, self);// create new annotation object
                    // Set the bout for the point
                    point.setBounds(x, y, 150, 50);
                    image_pane.add(point);
                    data.add(point);
                    // Repaint the frame and panel to show annotations
                    image_pane.repaint();
                    myMainWindow.repaint();

                    //Set the bachground image again since repaiting
                    image_view.setLocation(0,0);
                    image_view.setSize(img_buffer.getWidth(), img_buffer.getHeight());
                    image_view.setIcon(img);
                    image_pane.add(image_view);
                }
            }
            
            //Ignore the method
            @Override
            public void mousePressed(MouseEvent e) {}
            //Ignore the method
            @Override
            public void mouseReleased(MouseEvent e) {}
            //Ignore the method
            @Override
            public void mouseEntered(MouseEvent e) {}
            //Ignore the method
            @Override
            public void mouseExited(MouseEvent e) {}        
        });
        
        // Make main window visible
        myMainWindow.setVisible(true); 
        
    }

    // Resize the image based on the selected image
    public void resize_pane(File file){
        
        int image_hight = 0;
        int image_width = 0;
        if ( file == null){
            image_hight = 0;
            image_width = 400;
        } else {
            try {
                
                //Check whether the chose image is image or not
                String mimetype= new MimetypesFileTypeMap().getContentType(file);
                String type = mimetype.split("/")[0];
                if(type.equals("image")){
                    // only jpg and jpeg images are accepted
                    img_buffer = ImageIO.read(file);
                    img = new ImageIcon(img_buffer);
                    image_hight = img.getIconHeight();
                    image_width = img.getIconWidth();
                }
                else {
                    JOptionPane.showMessageDialog(null,
                        "You must select jpg or jpeg image", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
                    return ;
                }   
            } catch (IOException ex) {}
        }
        
        //Add the image_view into the pane
        image_pane.setLayout(null);                
        image_view.setLocation(0,0);
        image_view.setSize(image_width, image_hight);
        image_view.setIcon(img);
        image_pane.add(image_view);    

        //Add the Brows button into the pane
        brows_button.setLocation(0, image_hight);
        brows_button.setSize(image_width/2, 30);
        brows_button.setText("Open");
        image_pane.add(brows_button);

        //Add the save button into the pane
        save_button.setLocation(image_width/2, image_hight);
        save_button.setSize(image_width/2, 30);
        save_button.setText("Save");
        image_pane.add(save_button);
        // rearrange the window to center
        myMainWindow.setLocation((dim.width/2 - image_width/2), (dim.height/2 - image_hight/2));
        myMainWindow.setSize(image_width, image_hight + 70);// define initial point of the window
        
        
    }
     
    // Main method to start the compilation
    public static void main(String[] args){        
        AnnotateImage rt = new AnnotateImage();
        rt.runGUI();       
    }
    
}
