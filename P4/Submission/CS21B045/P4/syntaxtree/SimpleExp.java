//
// Generated by JTB 1.3.2
//

package syntaxtree;

/**
 * Grammar production:
 * f0 -> Temp()
 *       | IntegerLiteral()
 *       | Label()
 */
public class SimpleExp implements Node {
   public NodeChoice f0;

   public SimpleExp(NodeChoice n0) {
      f0 = n0;
   }

   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
}

