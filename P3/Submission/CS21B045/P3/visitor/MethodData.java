package visitor;
import java.util.*;

public class MethodData 
{
    String methodname = "";
    String returntype = "";
    Vector<String> argtypes = new Vector<String>();
    Vector<String> argnames = new Vector<String>();
    Map<String,String> methodDecls = new HashMap<String,String>();

    public void addMethodDecl(String name, String type)
    {
        methodDecls.put(name,type);
    }
}
