import syntaxtree.*;
import visitor.*;

@SuppressWarnings("unchecked")
public class P2 {
    public static void main(String [] args) 
    {
        try 
        {
            Node root = new MiniJavaParser(System.in).Goal();
            DFSHoist g1 = new DFSHoist<>();
            Object value = root.accept(g1,null);

            PassTwo g2 = new PassTwo<>(g1.data);
            value = root.accept(g2,null);


            System.out.println("Program type checked successfully");
        }
        catch (ParseException e) {System.out.println(e.toString());}
    }
}