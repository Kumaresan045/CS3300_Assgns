package visitor;

public class ExpOut 
{
    public String factorial = 
"      MAIN [0] [10] [2]\n"+
"      MOVE v1 4\n"+
"      MOVE s0 v1\n"+
"      MOVE s0 HALLOCATE s0\n"+
"      MOVE s0 s0\n"+
"      MOVE v1 4\n"+
"      MOVE s1 v1\n"+
"      MOVE s1 HALLOCATE s1\n"+
"      MOVE s1 s1\n"+
"      MOVE s2 Fac_ComputeFac\n"+
"      HSTORE s0 0 s2\n"+
"      HSTORE s1 0 s0\n"+
"      MOVE s1 s1\n"+
"      HLOAD s0 s1 0\n"+
"      HLOAD s0 s0 0\n"+
"      MOVE v1 10\n"+
"      MOVE s2 v1\n"+
"      ASTORE SPILLEDARG 0 t0\n"+
"      ASTORE SPILLEDARG 1 t1\n"+
"      ASTORE SPILLEDARG 2 t2\n"+
"      ASTORE SPILLEDARG 3 t3\n"+
"      ASTORE SPILLEDARG 4 t4\n"+
"      ASTORE SPILLEDARG 5 t5\n"+
"      ASTORE SPILLEDARG 6 t6\n"+
"      ASTORE SPILLEDARG 7 t7\n"+
"      ASTORE SPILLEDARG 8 t8\n"+
"      ASTORE SPILLEDARG 9 t9\n"+
"      MOVE a0 s1\n"+
"      MOVE a1 s2\n"+
"      CALL s0\n"+
"      ALOAD t0 SPILLEDARG 0\n"+
"      ALOAD t1 SPILLEDARG 1\n"+
"      ALOAD t2 SPILLEDARG 2\n"+
"      ALOAD t3 SPILLEDARG 3\n"+
"      ALOAD t4 SPILLEDARG 4\n"+
"      ALOAD t5 SPILLEDARG 5\n"+
"      ALOAD t6 SPILLEDARG 6\n"+
"      ALOAD t7 SPILLEDARG 7\n"+
"      ALOAD t8 SPILLEDARG 8\n"+
"      ALOAD t9 SPILLEDARG 9\n"+
"      MOVE s2 v0 \n"+
"      PRINT s2\n"+
"  END\n"+
"  // NOTSPILLED\n"+
"  Fac_ComputeFac [2] [18] [2]\n"+
"      ASTORE SPILLEDARG 0 s0\n"+
"      ASTORE SPILLEDARG 1 s1\n"+
"      ASTORE SPILLEDARG 2 s2\n"+
"      ASTORE SPILLEDARG 3 s3\n"+
"      ASTORE SPILLEDARG 4 s4\n"+
"      ASTORE SPILLEDARG 5 s5\n"+
"      ASTORE SPILLEDARG 6 s6\n"+
"      ASTORE SPILLEDARG 7 s7\n"+
"  MOVE s0 a0\n"+
"  MOVE s1 a1\n"+
"      MOVE v1 0\n"+
"      MOVE s2 v1\n"+
"      MOVE s2 LE s1 s2\n"+
"      CJUMP s2 L2\n"+
"      MOVE v1 1\n"+
"      MOVE s0 v1\n"+
"      MOVE s0 s0\n"+
"      JUMP L3\n"+
"  L2\n"+
"      MOVE s2 s0\n"+
"      HLOAD s3 s2 0\n"+
"      HLOAD s3 s3 0\n"+
"      MOVE v1 1\n"+
"      MOVE s4 v1\n"+
"      MOVE s4 MINUS s1 s4\n"+
"      ASTORE SPILLEDARG 8 t0\n"+
"      ASTORE SPILLEDARG 9 t1\n"+
"      ASTORE SPILLEDARG 10 t2\n"+
"      ASTORE SPILLEDARG 11 t3\n"+
"      ASTORE SPILLEDARG 12 t4\n"+
"      ASTORE SPILLEDARG 13 t5\n"+
"      ASTORE SPILLEDARG 14 t6\n"+
"      ASTORE SPILLEDARG 15 t7\n"+
"      ASTORE SPILLEDARG 16 t8\n"+
"      ASTORE SPILLEDARG 17 t9\n"+
"      MOVE a0 s2\n"+
"      MOVE a1 s4\n"+
"      CALL s3\n"+
"      ALOAD t0 SPILLEDARG 8\n"+
"      ALOAD t1 SPILLEDARG 9\n"+
"      ALOAD t2 SPILLEDARG 10\n"+
"      ALOAD t3 SPILLEDARG 11\n"+
"      ALOAD t4 SPILLEDARG 12\n"+
"      ALOAD t5 SPILLEDARG 13\n"+
"      ALOAD t6 SPILLEDARG 14\n"+
"      ALOAD t7 SPILLEDARG 15\n"+
"      ALOAD t8 SPILLEDARG 16\n"+
"      ALOAD t9 SPILLEDARG 17\n"+
"      MOVE s4 v0 \n"+
"      MOVE s4 TIMES s1 s4\n"+
"      MOVE s0 s4\n"+
"  L3\n"+
"      NOOP\n"+
"      MOVE v0 s0\n"+
"      ALOAD s0 SPILLEDARG 0\n"+
"      ALOAD s1 SPILLEDARG 1\n"+
"      ALOAD s2 SPILLEDARG 2\n"+
"      ALOAD s3 SPILLEDARG 3\n"+
"      ALOAD s4 SPILLEDARG 4\n"+
"      ALOAD s5 SPILLEDARG 5\n"+
"      ALOAD s6 SPILLEDARG 6\n"+
"      ALOAD s7 SPILLEDARG 7\n"+
"  END\n"+
"  // NOTSPILLED"
;    
}
