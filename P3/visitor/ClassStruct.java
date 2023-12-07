package visitor;
import java.util.*;

public class ClassStruct 
{
    String name = "";
    Vector<String> entries = new Vector<String>();
    Vector<String> vtable = new Vector<String>();
    Vector<String> typesOfEntries = new Vector<String>();

    public void display()
    {
        System.out.println("Display ClassStruct: " + name + " :");
        System.out.println("Entries :\nvtable\n");
        for(int i=1; i<entries.size(); i++)
        {
            System.out.println(i + ". ( " + entries.get(i) + " , " + typesOfEntries.get(i) + " )");
        }
        System.out.println("Display ClassStruct A Ends");
    }
}