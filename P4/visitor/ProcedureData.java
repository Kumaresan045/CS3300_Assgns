package visitor;
import java.util.*;

//  Enums used:
//  spill,greaterFourArg,unused

public class ProcedureData
{
    String procedureName;
    boolean isLeaf;
    boolean isSpilled;

    int numArgs;
    int spillSlots;
    int maxChildArgs;

    HashMap<String, BlockData> allBlockData;
    HashMap<String, BlockData> liveNessAllBlockData;
    HashMap<String, TempData> allTempData;

    ArrayList<TempData> active;
    ArrayList<String> freeRegs;
    ArrayList<TempData> toBeSpilled;
    ArrayList<TempData> liveIntervals;

    HashMap<String, String> tempToReg;
    HashMap<Integer,Integer> spillLocation;

    public ProcedureData(String procedureName, boolean isLeaf, int spillSlots, int maxChildArgs ,int numArgs)
    {
        this.procedureName = procedureName;
        this.isLeaf = isLeaf;
        this.numArgs = numArgs;
        this.spillSlots = spillSlots + Math.max(0, numArgs-4);
        this.maxChildArgs = maxChildArgs;
        this.isSpilled = false;
        allBlockData = new HashMap<String, BlockData>();
        allTempData = new HashMap<String, TempData>();
        tempToReg = new HashMap<String, String>();
        active = new ArrayList<TempData>();
        freeRegs = new ArrayList<String>();
        toBeSpilled = new ArrayList<TempData>();
        liveIntervals = new ArrayList<TempData>();
        spillLocation = new HashMap<Integer,Integer>();
        liveNessAllBlockData = new HashMap<String, BlockData>();
    }

    public void addBlockData(String blockName, BlockData blockData)
    {
        allBlockData.put(blockName, blockData);
    }

    public BlockData getBlockData(String blockName)
    {
        return allBlockData.get(blockName);
    }

    public void allblockDisplay()
    {
        for (String blockName : allBlockData.keySet()) 
        {
            BlockData blockData = allBlockData.get(blockName);
            blockData.display();
        }
    }

    public void linearScanRegAlloc()
    {
        freeRegInit();
        liveIntervalsInit();   
        int totalRegs = freeRegs.size();

        for(TempData interval : liveIntervals)
        {
            if(interval.unused)
            {
                tempToReg.put(interval.tempId + "", "v0");
                continue;
            }
            if(interval.greaterFourArg)
            {
                tempToReg.put(interval.tempId + "", "greaterFourArg");
                continue;
            }
            expireOldIntervals(interval);
            if(active.size() == totalRegs) spillAtInterval(interval);
            else
            {
                String reg = freeRegs.get(0);
                freeRegs.remove(0);
                tempToReg.put(interval.tempId + "", reg);
                activeAdd(interval);
            }
        }
    }

    void activeAdd(TempData tempI)
    {
        active.add(tempI);
        Collections.sort(active, new IncrEndComp());
    }

    void expireOldIntervals(TempData tempI)
    {
        ArrayList<TempData> toRemove = new ArrayList<TempData>();
        for(int j=0; j<active.size(); j++)
        {
            TempData tempJ = active.get(j);
            if(tempJ.liveRangeEnd >= tempI.liveRangeStart) break;
            toRemove.add(tempJ);
        }
        for(TempData tempJ : toRemove)
        {
            active.remove(tempJ);
            freeRegs.add(tempToReg.get(tempJ.tempId + ""));
        }

    }

    void spillAtInterval(TempData tempI)
    {
        TempData spill = active.get(active.size()-1);
        this.spillSlots++;
        this.isSpilled = true;
        if(spill.liveRangeEnd > tempI.liveRangeEnd)
        {
            tempToReg.put(tempI.tempId + "", tempToReg.get(spill.tempId + ""));
            tempToReg.put(spill.tempId + "", "spill");
            toBeSpilled.add(spill);
            active.remove(spill);
            activeAdd(tempI);
        }
        else
        {
            tempToReg.put(tempI.tempId + "", "spill");
            toBeSpilled.add(tempI);
        }
    }

    void liveNessAllBlockDataInit()
    {
        for (String blockName : allBlockData.keySet()) 
        {
            BlockData blockData = allBlockData.get(blockName);
            liveNessAllBlockData.put(blockName, blockData);
        }
        BlockData firstBlock = new BlockData(0, procedureName, allTempData, new HashMap<>());
        firstBlock.succ.add("1");

        for(int i=0; i< numArgs; i++)
        {
            TempData temp = allTempData.get(i + "");
            if(temp == null) continue;
            if(i<4) temp.lessFourArg = true;
            else temp.greaterFourArg = true;
            firstBlock.defTemps.put(temp.tempId + "", temp);
        }
        liveNessAllBlockData.put("0", firstBlock);
    }

    public void findInOut()
    {
        liveNessAllBlockDataInit();
        do
        {
            for (String blockName : liveNessAllBlockData.keySet()) 
            {
                BlockData blockData = liveNessAllBlockData.get(blockName);
                blockData.inDash = new HashMap<String, TempData>(blockData.inTemps);
                blockData.outDash = new HashMap<String, TempData>(blockData.outTemps);
                updateInTemps(blockData);
                updateOutTemps(blockData);
            }
        }
        while(!isInOutSame());
    }

    boolean isInOutSame()
    {
        for (String blockName : liveNessAllBlockData.keySet()) 
        {
            BlockData blockData = liveNessAllBlockData.get(blockName);
            if(!(isSameMap(blockData.inTemps, blockData.inDash) && isSameMap(blockData.outDash, blockData.outTemps)))
                return false;
        }
        return true;
    }

    boolean isSameMap(HashMap<String, TempData> map1, HashMap<String, TempData> map2)
    {
        if(map1.size() != map2.size())
            return false;
        for (String tempName : map1.keySet()) 
        {
            if(!map2.containsKey(tempName))
                return false;
        }
        return true;
    }

    void updateInTemps(BlockData blockData)
    {
        HashMap<String, TempData> tempMap1 = new HashMap<String, TempData>(blockData.useTemps);
        HashMap<String, TempData> tempMap2 = new HashMap<String, TempData>(blockData.outTemps);
        for(String tempName : blockData.defTemps.keySet()) tempMap2.remove(tempName);
        for(String tempName : tempMap2.keySet()) tempMap1.put(tempName, tempMap2.get(tempName));
        blockData.inTemps = tempMap1;
    }

    void updateOutTemps(BlockData blockData)
    {
        HashMap<String, TempData> tempMap1 = new HashMap<String, TempData>();
        for(String blockid : blockData.succ)
        {
            BlockData succBlockData = liveNessAllBlockData.get(blockid);
            for(String tempName : succBlockData.inTemps.keySet())
            {
                if(!tempMap1.containsKey(tempName))
                    tempMap1.put(tempName, succBlockData.inTemps.get(tempName));
            }
        }
        blockData.outTemps = tempMap1;
    }

    void findLiveRange()
    {
        ArrayList<BlockData> blocksOrder = new ArrayList<BlockData>();
        for(BlockData blockData : liveNessAllBlockData.values()) blocksOrder.add(blockData);
        Collections.sort(blocksOrder, new IncrLineComp());

        for(TempData temp : allTempData.values())
        {
            for(BlockData blockData : blocksOrder)
            {
                if(blockData.outTemps.containsKey(temp.tempId + ""))
                {
                    temp.liveRangeStart = blockData.lineNum;
                    if(blockData.lineNum == 0) temp.liveRangeStart = 1;
                    break;
                }
            }
            for(int i=blocksOrder.size()-1; i>=0; i--)
            {
                BlockData blockData = blocksOrder.get(i);
                if(blockData.inTemps.containsKey(temp.tempId + ""))
                {
                    temp.liveRangeEnd = blockData.lineNum;
                    break;
                }
            }

            if(temp.liveRangeStart == 0 || temp.liveRangeEnd == 0) temp.unused = true;
            else temp.unused = false;
        }
    }


    //====Helper Functions====//

    void freeRegInit()
    {
        for(int i=0; i<8; i++) freeRegs.add("s" + i);
        for(int i=0; i<10; i++) freeRegs.add("t" + i);
    }

    void liveIntervalsInit()
    {
        for(TempData temps : allTempData.values()) liveIntervals.add(temps);
        Collections.sort(liveIntervals, new IncrStartComp()); 
    }

    
    //====Display====//
    public void display()
    {
        System.out.println("Procedure Name: " + procedureName);
        System.out.println("Is Leaf: " + isLeaf);
        System.out.println("Num Args: " + numArgs);
        System.out.println("Spill Slots: " + spillSlots);
        System.out.println("Max Child Args: " + maxChildArgs);
        System.out.println("====================================");
    }

    public void tempToRegDisplay()
    {
        System.out.println(this.procedureName + ":");
        for(String tempId : tempToReg.keySet())
        {
            System.out.println(tempId + " " + tempToReg.get(tempId));
        }
        System.out.println("====================================");
    }

    public void liveRangeDisplay()
    {
        System.out.println(this.procedureName + ":");
        //sort by tempId
        ArrayList<TempData> tempDataList = new ArrayList<TempData>(allTempData.values());
        Collections.sort(tempDataList, new IncrTempIdComp());
        for(TempData temp : tempDataList)
        {
            System.out.println("TEMP "+ temp.tempId + " (" + temp.liveRangeStart + "," + temp.liveRangeEnd + ")");
        }
        System.out.println("====================================");
    }
}

