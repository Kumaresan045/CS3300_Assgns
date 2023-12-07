import syntaxtree.*;
import visitor.*;

@SuppressWarnings("unchecked")
public class P4 {
   public static void main(String [] args) 
   {
      try 
      {
         Node root = new microIRParser(System.in).Goal();
         DataCollectVisitor dataCollectVisitor = new DataCollectVisitor<>();
         root.accept(dataCollectVisitor,null);

         MiniRAGen miniRAGen = new MiniRAGen(dataCollectVisitor.allProcedureData);
         root.accept(miniRAGen,null);
         miniRAGen.displayCode();
        
      }
      catch (ParseException e) 
      {
         System.out.println(e.toString());
      }
   }
} 