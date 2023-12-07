import syntaxtree.*;
import visitor.*;

@SuppressWarnings("unchecked")
public class P3 {
   public static void main(String [] args) 
   {
      try 
      {
         Node root = new MiniJavaParser(System.in).Goal();

         ClassCollect classCollectVisitor = new ClassCollect<>();
         root.accept(classCollectVisitor,null); // First pass to collect class names

         MicroGen microGenerator = new MicroGen(classCollectVisitor.classinfo);
         root.accept(microGenerator,null); // Second pass to generate microjava code

         microGenerator.getcode();
      }
      catch (ParseException e) 
      {
         System.out.println(e.toString());
      }
   }
} 