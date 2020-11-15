package imageanotator;
import imageanotator.Annotate_Symbol;
import java.util.ArrayList;


/*The class holds the array list that has all the living annotations*/
public class Data_Holder {
    
    //private constructor
    //Make this class a singleton object
    private Data_Holder(){
        
    }
    
    private static Data_Holder object = new Data_Holder();
    
    public static Data_Holder get_data_holder(){
        return object;
    }
    
    //The array list to hold the annoations
    public static ArrayList<Annotate_Symbol> annotations = new ArrayList<>();
    
    //Add new annotation to the list
    public void add(Annotate_Symbol symbol){
        this.annotations.add(symbol);
    }
    
    //Remove exsiting annotation from the list
    public void remove(Annotate_Symbol symbol){
        this.annotations.remove(symbol);
    }  
}
