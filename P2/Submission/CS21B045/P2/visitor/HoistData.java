package visitor;
import java.util.*;

public class HoistData 
{
    Map<String,String> parentMap;
    Vector<String> classOrder;
    Vector<Map<String,String>> fieldList;
    Vector<Map<String,MethodData>> methodList;
    Map<String,String> tempfield;
    Map<String,MethodData> tempmethod;
    String purpose = "";
    String domainclass = "";
    String domainmethod = "";

    public HoistData()
    {
        parentMap = new HashMap<String,String>();
        classOrder = new Vector<String>();
        fieldList = new Vector<Map<String,String>>();
        methodList = new Vector<Map<String,MethodData>>();
    }

    boolean exists(String className)
    {
        return classOrder.contains(className);
    }
}
