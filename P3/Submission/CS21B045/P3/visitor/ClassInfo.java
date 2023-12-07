package visitor;
import java.util.*;

public class ClassInfo 
{
    public Map<String, ClassData> classMap;

    public ClassInfo()
    {
        classMap = new HashMap<String, ClassData>();
    }

    public void addClass(String className, ClassData classData)
    {
        classMap.put(className, classData);
    }

    public void display()
    {
        for (Map.Entry<String,ClassData> entry : classMap.entrySet())
        {
            ClassData classData = entry.getValue();
            classData.display();
            System.out.println("============================================================");
        }
    }
}