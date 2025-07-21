package func;

import expr.Func;
import expr.Expr;
import expr.Power;
import expr.Term;
import expr.Num;

import java.math.BigInteger;
import java.util.ArrayList;

public class Recur {
    private ArrayList<Power> args;
    private ArrayList<Expr> args1;
    private BigInteger funcNsub1num;
    private ArrayList<Expr> args2;
    private BigInteger funcNsub2num;
    private Expr expr;

    public Recur(ArrayList<Power> args, Expr expr, BigInteger funcNsub1num, BigInteger funcNsub2num,
        ArrayList<Expr> args1, ArrayList<Expr> args2) {
        this.args = args;
        this.args1 = args1;
        this.funcNsub1num = funcNsub1num;
        this.args2 = args2;
        this.funcNsub2num = funcNsub2num;
        this.expr = expr;
    }

    public Define getDefine(int num,String name,FuncLib funcLib) {
        Num num1 = new Num(funcNsub1num,1);
        Num num2 = new Num(funcNsub2num,1);
        Term t1 = new Term();
        Term t2 = new Term();
        t1.addFactor(num1);
        t2.addFactor(num2);
        Func func1 = new Func(name,num - 1,1,args1,funcLib);
        Func func2 = new Func(name,num - 2,1,args2,funcLib);
        t1.addFactor(func1.getValue());
        t2.addFactor(func2.getValue());
        Expr expr = new Expr(1,1);
        expr.addTerm(t1);
        expr.addTerm(t2);
        Term t3 = new Term();
        if (!this.expr.isEmpty()) {
            t3.addFactor(this.expr);
            expr.addTerm(t3);
        }
        return new Define(name,num,expr,args);
    }
}
