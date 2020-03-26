import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// My Java adapted pickle class
//
//
// Example:
//      // Create the object
//      ImageData myImageData = new ImageData();
//      myImageData.caption = "VB";
//      myImageData.timeStamp = "2020";
//
//      // Save the object
//      Pickle.save(myImageData, "myData.ser");
//
//      // Recover the object
//      ImageData myImageData_ = null;
//      myImageData_ = (ImageData) Pickle.load(myImageData_, "myData.ser");
//
public class Pickle {

    // Save the object to the memory
    // args:    object: the object instance
    //          path:   the exact path and file name of where it will be stored    
    static public void save(Object object, String path){
        try {

            // Create a file and a stream object
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            // Write to the file
            out.writeObject(object); 
            
            // Close the file and the stream
            out.close();
            fileOut.close();
            
            System.out.println("Serialized data is saved in: " + path);
         } catch (IOException i) {
            i.printStackTrace();

            System.out.println("Unable to save the file. ");
         }

    }

    // Load the object from the memory
    // args:    object: the empty object instance
    //          path:   the exact path and file name of where it is be stored 
    // returns: object: the recovered object instance            
    static public Object load(Object object, String path){
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            object = (Object) in.readObject();
            in.close();
            fileIn.close();

            System.out.println("Serialized data is read from: " + path);
        } 
        catch (IOException i) {
            System.out.println("File not found");
            i.printStackTrace();
        } 
        catch (ClassNotFoundException c) {
            System.out.println("class not found");
            c.printStackTrace();
        }

        return object;
    }
}


