package visitor;
import java.util.*;

public class ClassData 
{
    String parent;
    String name;
    Map<String, String> fields = new HashMap<String, String>();
    Map<String, MethodData> methods = new HashMap<String, MethodData>();

    public void addClassField(String name, String type)
    {
        fields.put(name,type);
    }

    public void addMethod(String name, MethodData methodData)
    {
        methods.put(name,methodData);
    }

    public void display()
    {
        System.out.println("Class Name: " + name);
        System.out.println("Parent Name: " + parent);
        System.out.println("Fields: ");
        String indent = "    ";
        for (Map.Entry<String,String> entry : fields.entrySet())
        {
            System.out.println(indent + "Name: " + entry.getKey() + " Type: " + entry.getValue());
        }
        System.out.println("Methods: ");
        for (Map.Entry<String,MethodData> entry : methods.entrySet())
        {
            System.out.println(indent + "Name: " + entry.getKey());
            MethodData methodData = entry.getValue();
            System.out.println(indent + "Return Type: " + methodData.returntype);
            System.out.println(indent + "Arguments: ");
            for (int i = 0; i < methodData.argtypes.size(); i++)
            {
                System.out.println(indent + "Name: " + methodData.argnames.get(i) + " Type: " + methodData.argtypes.get(i));
            }
            System.out.println(indent + "Method Declarations: ");
            for (Map.Entry<String,String> entry2 : methodData.methodDecls.entrySet())
            {
                System.out.println(indent + indent + "Name: " + entry2.getKey() + " Type: " + entry2.getValue());
            }
            System.out.println("____________________________");
        }
    }
    
}
