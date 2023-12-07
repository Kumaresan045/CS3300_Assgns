package visitor;
import java.util.*;

public class AllProcedureData
{
    HashMap<String, ProcedureData> allProcedures;

    public AllProcedureData()
    {
        allProcedures = new HashMap<String, ProcedureData>();
    }

    public void addProcedure(String procedureName, ProcedureData procedureData)
    {
        allProcedures.put(procedureName, procedureData);
    }
   
    public ProcedureData getProcedureData(String procedureName)
    {
        return allProcedures.get(procedureName);
    }

    public void display()
    {
        System.out.println("All Procedure Data:");
        for (String procedureName : allProcedures.keySet()) 
            allProcedures.get(procedureName).allblockDisplay();
        System.out.println("All Procedure Data End");
    }
}