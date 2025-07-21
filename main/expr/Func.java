package expr;

import expr.operator.Poly;
import func.FuncLib;

import java.util.ArrayList;

public class Func implements Factor {
    private final String name;
    private int sign;
    private ArrayList<Expr> args;
    private int num;
    private FuncLib funcLib;

    public Func(String name,int num, int sign,  ArrayList<Expr> args, FuncLib funcLib) {
        this.name = name;
        this.num = num;
        this.sign = sign;
        this.args = args;
        this.funcLib = funcLib;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public Expr getValue() {
        Expr expr = funcLib.getValue(args, num);
        expr.setSign(sign);
        return expr;
    }

    public Expr replace(ArrayList<Power> var, ArrayList<Expr> value) {
        Expr newExpr = getValue();
        newExpr = newExpr.replace(var, value);
        return newExpr;
    }

    public Factor derivative() {
        Expr expr = getValue();
        return expr.derivative();
    }

    @Override
    public Poly toPoly() {
        Expr expr = getValue();
        return expr.toPoly();
    }
}
