package visitor;
import java.util.*;

public class TempData
{
    int tempId;
    HashMap<String, BlockData> useNodes, defNodes;

    int liveRangeStart, liveRangeEnd;
    boolean unused;
    boolean lessFourArg;
    boolean greaterFourArg;


    String registerName;

    public TempData(int tempId)
    {
        this.tempId = tempId;
        useNodes = new HashMap<String, BlockData>();
        defNodes = new HashMap<String, BlockData>();
        unused = true;
        lessFourArg = false;
        greaterFourArg = false;
        liveRangeStart = 0;
        liveRangeEnd = 0;
    }
}

class IncrStartComp implements Comparator<TempData>
{
    @Override
    public int compare(TempData t1, TempData t2)
    {
        return t1.liveRangeStart - t2.liveRangeStart;
    }
}

class IncrEndComp implements Comparator<TempData>
{
    @Override
    public int compare(TempData t1, TempData t2)
    {
        return t1.liveRangeEnd - t2.liveRangeEnd;
    }
}

class IncrTempIdComp implements Comparator<TempData>
{
    @Override
    public int compare(TempData t1, TempData t2)
    {
        return t1.tempId - t2.tempId;
    }
}