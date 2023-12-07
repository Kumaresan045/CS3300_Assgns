//
// Generated by Kums
//

package visitor;
import syntaxtree.*;

import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
@SuppressWarnings("unchecked")
public class DataCollectVisitor<R,A> implements GJVisitor<R,A> 
{
   
   //============= Data Structures for storing the information ============//

   public AllProcedureData allProcedureData;

   //===================== Auxillary Helper Functions =====================//

   public DataCollectVisitor()
   {
      allProcedureData = new AllProcedureData();      
   }

   void addUse(Temp temp, BlockData block)
   {
      String tempId = temp.f1.f0.tokenImage;
      HashMap<String, TempData> allTempData = block.allTempData;
      TempData tempData = allTempData.get(tempId);
      if(tempData==null)
      {
         tempData = new TempData(Integer.parseInt(tempId));
         allTempData.put(tempId, tempData);
      }
      tempData.useNodes.put(Integer.toString(block.lineNum), block);
      block.addUseTemp(tempId, tempData);
   }

   void addDef(Temp temp, BlockData block)
   {
      String tempId = temp.f1.f0.tokenImage;
      HashMap<String, TempData> allTempData = block.allTempData;
      TempData tempData = allTempData.get(tempId);
      if(tempData==null)
      {
         tempData = new TempData(Integer.parseInt(tempId));
         allTempData.put(tempId, tempData);
      }
      tempData.defNodes.put(Integer.toString(block.lineNum), block);
      block.addDefTemp(tempId, tempData);
   }


   //======================================================================// 
   //============== Visitor methods for the syntax tree nodes =============//
   //======================================================================// 


   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n, A argu) 
   {
      R _ret=null;

      StmtList stmtlst = n.f1;
      String procedureName = "MAIN";

      HashMap<String, TempData> allTempData = new HashMap<String, TempData>();
      HashMap<String, String> labelLine = new HashMap<String, String>();
      Vector<BlockData> blockDataList = new Vector<BlockData>();
      CallInfo callInfo = new CallInfo();

      //Collect all Labels
      if(stmtlst.f0.present())
      {
         int lineCnt = 1;
         for(Node node : stmtlst.f0.nodes)
         {
            NodeSequence seq = (NodeSequence)node;
            NodeOptional label = (NodeOptional) seq.elementAt(0);
            if(label.present())
            {
               String labelName = ((Label)label.node).f0.tokenImage;
               labelLine.put(labelName, Integer.toString(lineCnt));
            }
            lineCnt++;
         }
      }
      
      //Collect all the blocks
      if(stmtlst.f0.present())
      {
         int lineCnt = 1;
         for(Node node : stmtlst.f0.nodes)
         {
            NodeSequence seq = (NodeSequence)node;
            Stmt stmt = (Stmt) seq.elementAt(1);
            stmt.accept(this, (A)callInfo);

            BlockData blockData = new BlockData(lineCnt, procedureName, allTempData, labelLine);
            blockData.succ.add(Integer.toString(lineCnt+1));
            blockDataList.add(blockData);

            stmt.accept(this, (A)blockData);
            lineCnt++;
         }
      }
      // Remove the last block's successor
      if(blockDataList.size()>0) blockDataList.lastElement().succ.removeElementAt(0);

      //Main always has 10 spill slots
      int spillSlots = 10;
      if(callInfo.isleaf) spillSlots = 0;

      //Initialize the procedure data
      ProcedureData procData = new ProcedureData(procedureName, callInfo.isleaf, spillSlots, callInfo.maxChildArgs, 0);
      for(BlockData blockData : blockDataList)
      {
         blockData.procedureData = procData;
         procData.addBlockData(Integer.toString(blockData.lineNum), blockData);
      }
      procData.allTempData = allTempData;
      allProcedureData.addProcedure(procData.procedureName, procData);
      
      procData.findInOut();
      procData.findLiveRange();
      procData.linearScanRegAlloc();
      //procData.liveRangeDisplay();
      
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);

      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n, A argu) 
   {
      R _ret=null;
      String procedureName = n.f0.f0.tokenImage;
      String numArgs = n.f2.f0.tokenImage;
      
      StmtList stmtlst = n.f4.f1;
      SimpleExp retExpr = n.f4.f3;
      CallInfo callInfo = new CallInfo();
      int spillSlots = 8;
      Vector<BlockData> blockDataList = new Vector<BlockData>();
      HashMap<String, TempData> allTempData = new HashMap<String, TempData>();
      HashMap<String, String> labelLine = new HashMap<String, String>();
   

      //Collect all Labels
      if(stmtlst.f0.present())
      {
         int lineCnt = 1;
         for(Node node : stmtlst.f0.nodes)
         {
            NodeSequence seq = (NodeSequence)node;
            NodeOptional label = (NodeOptional) seq.elementAt(0);
            if(label.present())
            {
               String labelName = ((Label)label.node).f0.tokenImage;
               labelLine.put(labelName, Integer.toString(lineCnt));
            }
            lineCnt++;
         }
      }

      //Collect all blocks
      int lineCnt = 1;
      if(stmtlst.f0.present())
      {
         for(Node node : stmtlst.f0.nodes)
         {
            NodeSequence seq = (NodeSequence)node;
            Stmt stmt = (Stmt) seq.elementAt(1);
            stmt.accept(this, (A)callInfo);

            BlockData blockData = new BlockData(lineCnt, procedureName, allTempData, labelLine);
            blockData.succ.add(Integer.toString(lineCnt+1));
            blockDataList.add(blockData);

            stmt.accept(this, (A)blockData);
            lineCnt++;
         }
      }
      //Add the last block (return block)
      BlockData retBlock = new BlockData(lineCnt, procedureName, allTempData, labelLine);
      FindFurtherUse findFurtherUse = new FindFurtherUse(retBlock);
      blockDataList.add(retBlock);
      retExpr.accept(this, (A)findFurtherUse);

      //If non-leaf then need 10 more spill slots
      if(callInfo.isleaf == false) spillSlots = 18;

      //Initialize the procedure data
      ProcedureData procData = new ProcedureData(procedureName,callInfo.isleaf, spillSlots, callInfo.maxChildArgs,Integer.parseInt(numArgs));
      for(BlockData blockData : blockDataList)
      {
         blockData.procedureData = procData;
         procData.addBlockData(Integer.toString(blockData.lineNum), blockData);
      }
      procData.allTempData = allTempData;
      allProcedureData.addProcedure(procData.procedureName, procData);
      
      procData.findInOut();
      procData.findLiveRange();
      procData.linearScanRegAlloc();
      //procData.liveRangeDisplay();

      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;

      if(argu instanceof BlockData) 
      {
         BlockData block = (BlockData)argu;
         block.succ.add(block.labelLine.get(n.f2.f0.tokenImage));
         addUse(n.f1, (BlockData)argu);
      }

      if(argu instanceof LoopCollect) 
      {
         LoopCollect loopCollect = (LoopCollect)argu;
         int loopLoc = Integer.parseInt(loopCollect.labelLine.get(n.f2.f0.tokenImage));
         int currentLine = loopCollect.currentLine;
         if(loopLoc < currentLine) loopCollect.addLoop(loopLoc, currentLine);;
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) 
   {
      if(argu instanceof BlockData) 
      {
         BlockData block = (BlockData)argu;
         block.succ.removeAllElements();
         block.succ.add(block.labelLine.get(n.f1.f0.tokenImage));
      }

      if(argu instanceof LoopCollect) 
      {
         LoopCollect loopCollect = (LoopCollect)argu;
         int loopLoc = Integer.parseInt(loopCollect.labelLine.get(n.f1.f0.tokenImage));
         int currentLine = loopCollect.currentLine;
         if(loopLoc < currentLine) loopCollect.addLoop(loopLoc, currentLine);;
      }

      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;

      if(argu instanceof BlockData) 
      {
         addUse(n.f1, (BlockData)argu);
         addUse(n.f3, (BlockData)argu);
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;

      if(argu instanceof BlockData)
      {
         addDef(n.f1, (BlockData)argu);
         addUse(n.f2, (BlockData)argu);
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;

      if(argu instanceof BlockData)
      {
         BlockData blockData = (BlockData)argu;
         addDef(n.f1, blockData);
         FindFurtherUse findUse = new FindFurtherUse(blockData);
         n.f2.accept(this, (A)findUse);
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;

      if(argu instanceof BlockData)
      {
         BlockData blockData = (BlockData)argu;
         FindFurtherUse findUse = new FindFurtherUse(blockData);
         n.f1.accept(this, (A)findUse);
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   public R visit(StmtExp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n, A argu) 
   {
      R _ret=null;

      if(argu instanceof CallInfo)
      {
         CallInfo callInfo = (CallInfo)argu;
         callInfo.isleaf = false;
         callInfo.maxChildArgs = Math.max(callInfo.maxChildArgs, n.f3.size());
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) 
   {
      R _ret=null;

      if(argu instanceof FindFurtherUse)
      {
         FindFurtherUse findUse = (FindFurtherUse)argu;
         addUse(n, findUse.blockData);
      }

      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

//==========================================================================================================//

   public R visit(NodeList n, A argu) {
      R _ret=null;
      //int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         //_count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         //int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            //_count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      //int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
        //_count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return null; }

}

//=============================Custom Classes==================================//

class CallInfo 
{
   boolean isleaf = true;
   int maxChildArgs = 0;
}

class FindFurtherUse
{
    BlockData blockData;

    public FindFurtherUse(BlockData blockData)
    {
        this.blockData = blockData;
    }
}

class Pair
{
   int first;
   int second;

   Pair(int first, int second)
   {
      this.first = first;
      this.second = second;
   }
}

class LoopCollect
{
   ArrayList<Pair> loops;
   HashMap<String, String> labelLine;
   int currentLine;

   LoopCollect(HashMap<String, String> labelLine)
   {
      loops = new ArrayList<Pair>();
      this.labelLine = labelLine;
      currentLine = 0;
   }

   void addLoop(int start, int end)
   {
      loops.add(new Pair(start, end));
   }
}