//
// Generated by JTB 1.3.2
//

package syntaxtree;

/**
 * Grammar production:
 * f0 -> "CJUMP"
 * f1 -> Temp()
 * f2 -> Label()
 */
public class CJumpStmt implements Node {
   public NodeToken f0;
   public Temp f1;
   public Label f2;

   public CJumpStmt(NodeToken n0, Temp n1, Label n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
   }

   public CJumpStmt(Temp n0, Label n1) {
      f0 = new NodeToken("CJUMP");
      f1 = n0;
      f2 = n1;
   }

   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
}

