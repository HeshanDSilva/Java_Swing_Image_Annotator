package imageanotator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import imageanotator.AnnotateImage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Annotate_Symbol extends JComponent{
    private String label = null;
    private int x;
    private int y;
    //Get a copy of the Jpanel inorder to remove the unneccesary annotations
    private AnnotateImage annoataion_class;
    //Kep a copy of this class to manage remove annotions
    private Annotate_Symbol self = this;
    //getting the data calss
    Data_Holder data = Data_Holder.get_data_holder();

    // Getter for the variable label
    public String getLabel() {
        return label;
    }

    // Getter for the variable x
    public int getX() {
        return x;
    }

    // Getter for the variable y
    public int getY() {
        return y;
    }
    
    //Constructor for the class
    public Annotate_Symbol(String label, int x, int y, AnnotateImage annoataion_class) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.annoataion_class = annoataion_class;
        this.setOnMouseClickListener();
    }

    //Call paint component to graw the string and circle
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.YELLOW);
        g.drawOval(0, 0, 20, 20);
        g.setColor(Color.YELLOW);
        g.drawString(this.label, 20 , 30);
    }
    
    public void setOnMouseClickListener(){
        // Set mouse click listner on each annotaion to remove when request
        this.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                //Open the Confirmation diaglog
                int input = JOptionPane.showConfirmDialog(null, "Remove annotaion?");
                //Remove the annotaion if yes
                if (input == 0){
                    annoataion_class.image_pane.remove(self); // remove this object from panel
                    annoataion_class.image_pane.repaint();
                    annoataion_class.myMainWindow.repaint();
                    data.remove(self); // remove this object from attayList
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
    }
        
}
