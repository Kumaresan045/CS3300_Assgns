package visitor;
import java.util.*;

public class BlockData
{
    int lineNum;
    public HashMap<String, TempData> useTemps,defTemps;
    String procedureName;
    ProcedureData procedureData;
    HashMap< String, TempData> allTempData, inDash, outDash, inTemps, outTemps;
    HashMap<String, String> labelLine;
    Vector<String> succ;

    public BlockData(int lineNum, String procedureName, HashMap< String, TempData> allTempData, HashMap<String, String> labelLine)
    {
        this.lineNum = lineNum;
        this.procedureName = procedureName;
        this.allTempData = allTempData;
        this.labelLine = labelLine;
        useTemps = new HashMap<String, TempData>();
        defTemps = new HashMap<String, TempData>();
        inTemps = new HashMap<String, TempData>();
        outTemps = new HashMap<String, TempData>();
        inDash = new HashMap<String, TempData>();
        outDash = new HashMap<String, TempData>();
        succ = new Vector<String>();
    }

    public void addUseTemp(String tempName, TempData tempData)
    {
        useTemps.put(tempName, tempData);
    }

    public void addDefTemp(String tempName, TempData tempData)
    {
        defTemps.put(tempName, tempData);
    }

    public void display()
    {
        System.out.println("Block Data:");
        System.out.println("Line Num: " + lineNum);
        System.out.println("\nUse Temps: ");
        for (String tempName : useTemps.keySet()) 
            System.out.print(tempName + ", ");
        System.out.println("\nDef Temps: ");
        for (String tempName : defTemps.keySet()) 
            System.out.print(tempName + ", ");
        System.out.println("\nBlock Data End");
    }
}

class IncrLineComp implements Comparator<BlockData>
{
    public int compare(BlockData b1, BlockData b2)
    {
        return b1.lineNum - b2.lineNum;
    }
}