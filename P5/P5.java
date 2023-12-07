import syntaxtree.*;
import visitor.*;

@SuppressWarnings("unchecked")
public class P5 {
   public static void main(String [] args) 
   {
      try 
      {
         Node root = new MiniRAParser(System.in).Goal();
         MipsGen mipsGen = new MipsGen<>();
         root.accept(mipsGen,null);

         System.out.println(mipsGen.displayFinalCode());
        
      }
      catch (ParseException e) 
      {
         System.out.println(e.toString());
      }
   }
} 