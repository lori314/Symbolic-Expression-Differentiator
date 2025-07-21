package func;

import expr.Expr;
import expr.Power;

import java.util.ArrayList;

public class Define {
    private String name;
    private int num;
    private Expr expr;
    private ArrayList<Power> args;

    public Define(String name,int num, Expr expr, ArrayList<Power> args) {
        this.name = name;
        this.num = num;
        this.expr = expr;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public Expr calExpr(ArrayList<Expr> args) {
        Expr expr = this.expr;
        expr = expr.replace(this.args, args);
        return expr;
    }
}
