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
public class MiniRAGen<R,A> implements GJVisitor<R,A> 
{
   
   //============= Data Structures for storing the information ============//



   AllProcedureData allProcedureData;
   String currentProcedureName;
   String finalCode;
   int currentLine;
   HashMap<String, String> extraMap;

   public MiniRAGen(AllProcedureData allProcedureData)
   {
      this.allProcedureData = allProcedureData;
      finalCode = "";
      this.extraMap = new HashMap<String, String>();
      this.currentLine = 1;
   }

   //===================== Auxillary Helper Functions =====================//

   public void displayCode()
   {
      System.out.println(finalCode);
   }

   String addTail(ProcedureData procData)
   {
      return "END\n" + spillComments(procData) + "\n";
   }

   String spillComments(ProcedureData procData)
   {
      if(procData.isSpilled) return "// SPILLED\n";
      else return "// NOTSPILLED\n";
   }

   String callerSaveStore(int stackPointer)
   {
      int tempPointer = stackPointer;
      String code = "";
      for(int i = 0; i < 10; i++)
      {
         code += "ASTORE SPILLEDARG " + tempPointer + " " + "t" + i + "\n";
         tempPointer++;
      }
      return code;
   }

   String callerSaveLoad(int stackPointer)
   {
      int tempPointer = stackPointer;
      String code = "";
      for(int i = 0; i < 10; i++)
      {
         code += "ALOAD t" + i + " SPILLEDARG " + tempPointer + "\n";
         tempPointer++;
      }
      return code;
   }

   String calleeSaveStore(int stackPointer)
   {
      int tempPointer = stackPointer;
      String code = "";
      for(int i = 0; i < 8; i++)
      {
         code += "ASTORE SPILLEDARG " + tempPointer + " " + "s" + i + "\n";
         tempPointer++;
      }
      return code;
   }

   String calleeSaveLoad(int stackPointer)
   {
      int tempPointer = stackPointer;
      String code = "";
      for(int i = 0; i < 8; i++)
      {
         code += "ALOAD s" + i + " SPILLEDARG " + tempPointer + "\n";
         tempPointer++;
      }
      return code;
   }

   String callArgs(NodeListOptional args)
   {
      ArrayList<Integer> arguments = new ArrayList<Integer>();
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      String code = "";
      //Get all arguments
      if(args.present())
      {
         for(Node arg : args.nodes)
         {
            Temp temp = (Temp)arg;
            int tempId = Integer.parseInt(temp.f1.f0.tokenImage);
            arguments.add(tempId);
         }
      }
      int stackPointer = 1;

      for(int argIter = 0; argIter<arguments.size(); argIter++)
      {
         int arg = arguments.get(argIter);
         String regArg = procData.tempToReg.get(Integer.toString(arg));
         if(argIter < 4)
         {
            if(procData.spillLocation.containsKey(arg))
            {
               int spillLoc = procData.spillLocation.get(arg);
               code += "ALOAD v0 SPILLEDARG " + spillLoc + '\n';
               code += "MOVE a" + argIter + " v0\n";
            }
            else if(arg>=4 && arg<procData.numArgs)
            {
               code += "ALOAD v0 SPILLEDARG " + Integer.toString(arg-4) + '\n';
               code += "MOVE a" + argIter + " v0\n";
            }
            else
            {
               code += "MOVE a" + argIter + " " + regArg + '\n';
            }
            
         }
         else
         {
            if(procData.spillLocation.containsKey(arg))
            {
               int spillLoc = procData.spillLocation.get(arg);
               code += "ALOAD v0 SPILLEDARG " + spillLoc + '\n';
               code += "PASSARG " + stackPointer + " v0\n";
            }
            else if(arg>=4 && arg<procData.numArgs)
            {
               code += "ALOAD v0 SPILLEDARG " + Integer.toString(arg-4) + '\n';
               code += "PASSARG " + stackPointer + "v0\n";
            }
            else
            {
               code += "PASSARG " + stackPointer + " " + regArg + '\n';
            }
           
            stackPointer++;
         }
      }

      return code;
   }

   String preCall(NodeListOptional args)
   {
      String code = "";
      code += callerSaveStore(getcallerPointer());
      code += callArgs(args);

      return code;
   }

   int getSpillPointer()
   {
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      int numArgs = procData.numArgs;
      int stackPointer = 0;
      if(!currentProcedureName.equals("MAIN")) stackPointer = 8;
      stackPointer += Integer.max(numArgs-4,0);
      return stackPointer;
   }

   int getcallerPointer()
   {
      int stackPointer = 0;
      if(!currentProcedureName.equals("MAIN")) stackPointer = 8;
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      int numArgs = procData.numArgs;
      stackPointer += Integer.max(numArgs-4,0);
      stackPointer += procData.toBeSpilled.size();
      return stackPointer;
   }

   int getcalleePointer()
   {
      int stackPointer = 0;
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      int numArgs = procData.numArgs;
      stackPointer += Integer.max(numArgs-4,0);
      return stackPointer;
   }

   void storeSpill(ProcedureData procData)
   {
      int stackPointer = getcallerPointer() - procData.toBeSpilled.size();
      for(TempData temp: procData.toBeSpilled)
      {
         int tempId = temp.tempId;
         procData.spillLocation.put(tempId,stackPointer);
         stackPointer++;
      }
   }

   String callPrologue(ProcedureData procData)
   {
      String code = "";
      if(!procData.procedureName.equals("MAIN")) code += calleeSaveStore(getcalleePointer());
      int numArgs = procData.numArgs;
      storeSpill(procData);
      for(int i=0; i<numArgs; i++)
      {
         if(i<4)
         {
            String reg = procData.tempToReg.get(Integer.toString(i));
            if(!procData.tempToReg.containsKey(Integer.toString(i)))
            {
               code += "MOVE v0 a" + i + "\n";
            }
            else if(procData.spillLocation.containsKey(i))
            {
               code += "MOVE v0 a" + i + "\n";
               code += "ASTORE SPILLEDARG " + procData.spillLocation.get(i) + " v0\n";
            }
            else
            {
               code += "MOVE " + reg + " a" + i + "\n";
            }      
         }
      }
      return code;
   }

   String epilogue(ProcedureData procData)
   {
      String code = "";
      if(!procData.procedureName.equals("MAIN")) code += calleeSaveLoad(getcalleePointer());
      return code;
   }

   String preCode(int lineNumber)
   {
      extraMap.clear();
      String code = "";
      ProcedureData ProcData = allProcedureData.getProcedureData(currentProcedureName);
      HashMap<String,BlockData> allBlockData = ProcData.allBlockData;
      int numArgs = ProcData.numArgs;
      
      BlockData currentBlock = allBlockData.get(Integer.toString(lineNumber));
      for(TempData temp : currentBlock.defTemps.values()) extraMap.put(Integer.toString(temp.tempId), "v0"); 
      if(currentBlock.useTemps.size()>2) return "";
      int i=0;
      for(TempData temp : currentBlock.useTemps.values())
      {
         int tempId = temp.tempId;
         if(tempId >= 4 && tempId < numArgs) 
         {
            code += "ALOAD v" + i + " SPILLEDARG " + Integer.toString(tempId-4) + "\n";
            extraMap.put(Integer.toString(tempId), "v" + i);
            i++;
         }
         if(ProcData.toBeSpilled.contains(temp))
         {
            code += "ALOAD v" + i + " SPILLEDARG " + Integer.toString(ProcData.spillLocation.get(tempId)) + "\n";
            extraMap.put(Integer.toString(tempId), "v" + i);
            i++;
         }
         
      }
      return code;
   }

   String postCode(int lineNumber)
   {
      String code = "";
      ProcedureData ProcData = allProcedureData.getProcedureData(currentProcedureName);
      HashMap<String,BlockData> allBlockData = ProcData.allBlockData;
      int numArgs = ProcData.numArgs;
      
      BlockData currentBlock = allBlockData.get(Integer.toString(lineNumber));
      for(TempData temp : currentBlock.defTemps.values())
      {
         int tempId = temp.tempId;
         if(tempId >= 4 && tempId < numArgs) code += "ASTORE SPILLEDARG " + Integer.toString(tempId-4) + " " + extraMap.get(Integer.toString(tempId)) + '\n'; 
         if(ProcData.toBeSpilled.contains(temp)) code += "ASTORE SPILLEDARG " + Integer.toString(ProcData.spillLocation.get(tempId)) + " " + extraMap.get(Integer.toString(tempId)) + '\n';
      }
      return code;
   }

   boolean isExpired(String tempid)
   {
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      TempData temp = procData.allTempData.get(tempid);
      int start = temp.liveRangeStart;
      int end = temp.liveRangeEnd;
      if(start <= currentLine && end >= currentLine) return false;
      return true;
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
      ProcedureData procData = allProcedureData.getProcedureData("MAIN");
      String code = "MAIN [0] [" + procData.spillSlots +"] [" + procData.maxChildArgs + "]\n";
      currentProcedureName = "MAIN";
      code += callPrologue(procData);
      code += n.f1.accept(this, argu);
      code += epilogue(procData);
      code += addTail(procData);
      
      NodeListOptional procedures = n.f3;
      if(procedures.present())
      {
         for(Node node : procedures.nodes)
         {
            Procedure proc = (Procedure)node;
            currentProcedureName = proc.f0.f0.tokenImage;
            code += proc.accept(this, argu);
            code += addTail(allProcedureData.getProcedureData(currentProcedureName));
         }
      }
      finalCode = code;
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      String code = "";

      if(n.f0.present())
      {
         int lineCnt = 1;
         for(Node node : n.f0.nodes)
         {
            NodeSequence seq = (NodeSequence)node;
            NodeOptional label = (NodeOptional) seq.elementAt(0);
            if(label.present())
            {
               String labelName = ((Label)label.node).f0.tokenImage;
               labelName += ("_" + currentProcedureName);
               code += labelName + "\n";
            }
            currentLine = lineCnt;
            Stmt stmt = (Stmt) seq.elementAt(1);
            code += preCode(lineCnt);
            code += stmt.accept(this,argu);
            code += postCode(lineCnt);
            lineCnt++;
         }
      }
      _ret = (R)code;
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
      currentLine = 1;
      R _ret=null;
      String code = "";
      ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
      code += currentProcedureName + " [" + procData.numArgs + "] [" + procData.spillSlots + "] [" + procData.maxChildArgs + "]\n";      
      code += callPrologue(procData);
      code += n.f4.accept(this, argu);
      code += epilogue(procData);
      _ret = (R)code;
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
      String code = "";
      code += n.f0.accept(this, argu);
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      String code = "NOOP\n";
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      String code = "ERROR\n";
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      String code = "CJUMP ";
      code += n.f1.accept(this, argu);
      code += n.f2.f0.tokenImage;
      code += ("_" + currentProcedureName);
      code += '\n';
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      String code = "JUMP ";
      code += n.f1.f0.tokenImage;
      code += ("_" + currentProcedureName);
      code += '\n';
      _ret = (R)code;
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
      String code = "HSTORE ";
      code += n.f1.accept(this, argu);
      code += n.f2.accept(this, argu);
      code += n.f3.accept(this, argu);
      code += '\n';
      _ret = (R)code;
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
      String code = "HLOAD ";
      code += n.f1.accept(this, argu);
      code += n.f2.accept(this, argu);
      code += n.f3.accept(this, argu);
      code += '\n';
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) 
   {
      R _ret=null;
      String code = "";
      
      NodeChoice nChoice = n.f2.f0;
      if(nChoice.which == 0) // A function call
      {
         code += n.f2.accept(this, argu);
         code += "MOVE ";
         code += n.f1.accept(this, argu);
         code += "v0\n";
         return (R)code;
      }
      
      code += "MOVE ";
      code += n.f1.accept(this, argu);
      code += n.f2.accept(this, argu);
      code += '\n';
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      String code = "PRINT ";
      code += n.f1.accept(this, argu);
      code += '\n';
      _ret = (R)code;
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
      _ret = n.f0.accept(this, argu);
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
      String code = "";
      code += n.f1.accept(this, argu);
      RegNotAlloc regNotAlloc = new RegNotAlloc();
      n.f3.accept(this,(A)regNotAlloc);
      if(regNotAlloc.flag)
      {
         ProcedureData procData = allProcedureData.getProcedureData(currentProcedureName);
         int tempId = regNotAlloc.tempId;

         if(tempId >=4 && tempId < procData.numArgs)
         {
            code += "ALOAD v0 SPILLEDARG " + Integer.toString(tempId-4) + "\n";
            code += "MOVE v0 v0\n";
         }
         else if(procData.spillLocation.containsKey(tempId))
         {
            code += "ALOAD v0 SPILLEDARG " + Integer.toString(procData.spillLocation.get(tempId)) + "\n";
            code += "MOVE v0 v0\n";
         }
         else 
         {
            code += "=============PANIC====================\n";
         }

      }
      else
      {
         code += "MOVE v0 ";
         code += n.f3.accept(this, argu);
         code += '\n';
      }
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   public R visit(Call n, A argu) {
      R _ret=null;
      String code = "";
      code += preCall(n.f3);
      
      //Case where function ptr is spilled
      if(n.f1.f0.which == 0)
      {
         Node tempNode = n.f1.f0.choice;
         String tempid = ((Temp)tempNode).f1.f0.tokenImage;
         ProcedureData ProcData = allProcedureData.getProcedureData(currentProcedureName);
         TempData temp = ProcData.allTempData.get(tempid);
         int numArgs = ProcData.numArgs;

         if(ProcData.spillLocation.containsKey(Integer.parseInt(tempid)))
         {
            int tempId = temp.tempId;
            if(tempId >= 4 && tempId < numArgs) 
            {
               code += "ALOAD v0 SPILLEDARG " + Integer.toString(tempId-4) + "\n";
               extraMap.put(Integer.toString(tempId), "v0");
            }
            if(ProcData.toBeSpilled.contains(temp))
            {
               code += "ALOAD v0" + " SPILLEDARG " + Integer.toString(ProcData.spillLocation.get(tempId)) + "\n";
               extraMap.put(Integer.toString(tempId), "v0");
            }
         }
      }

      code += "CALL ";
      code += n.f1.accept(this, argu);
      code += '\n';
      code += callerSaveLoad(getcallerPointer());
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      String code = "HALLOCATE ";
      code += n.f1.accept(this, argu);
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      String code = "";
      code += n.f0.accept(this, argu);
      code += n.f1.accept(this, argu);
      code += n.f2.accept(this, argu);
      _ret = (R)code;
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
      String code = "";
      NodeChoice choice = (NodeChoice)n.f0;
      if (choice.which == 0) {
         code += "LE ";
      }
      else if (choice.which == 1) {
         code += "NE ";
      }
      else if (choice.which == 2) {
         code += "PLUS ";
      }
      else if (choice.which == 3) {
         code += "MINUS ";
      }
      else if (choice.which == 4) {
         code += "TIMES ";
      }
      else if (choice.which == 5) {
         code += "DIV ";
      }
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) {
      R _ret=null;
      String tempid = n.f1.f0.tokenImage;
      ProcedureData procedureData = allProcedureData.getProcedureData(currentProcedureName);
   
      if (procedureData.tempToReg.containsKey(tempid)) 
      {
         if(isExpired(tempid)) 
         {
            tempid = "v0 ";
            return (R)tempid;
         }
         String regAlloc = procedureData.tempToReg.get(tempid);
         if(regAlloc.equals("spill") || regAlloc.equals("greaterFourArg"))
         {
            if(!extraMap.containsKey(tempid))
            {
               if(argu instanceof RegNotAlloc)
               {
                  RegNotAlloc regNotAlloc = (RegNotAlloc)argu;
                  regNotAlloc.flag = true;
                  regNotAlloc.tempId = Integer.parseInt(tempid);
               }
               tempid = "extraMapMiss ";          
            }
            else
            {
               tempid = extraMap.get(tempid);
               tempid += " ";
            }
         }
         else
         {
            tempid = procedureData.tempToReg.get(tempid);
            tempid += " ";
         }
      }
      else 
      {
         if(argu instanceof RegNotAlloc)
         {
            RegNotAlloc regNotAlloc = (RegNotAlloc)argu;
            regNotAlloc.flag = true;
            regNotAlloc.tempId = Integer.parseInt(tempid);
         }
         tempid = "somethingWrong ";
      }
      _ret = (R)tempid;
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      String code = n.f0.tokenImage;
      code += ' ';
      _ret = (R)code;
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      String code = n.f0.tokenImage;
      code += ' ';
      _ret = (R)code;
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

//===============CUSTOM CLASSES===================//

class RegNotAlloc
{
   boolean flag;
   int tempId;
   RegNotAlloc()
   {
      flag = false;
      tempId = -1;
   }
}
