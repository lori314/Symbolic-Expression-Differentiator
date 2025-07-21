package expr;

import expr.operator.OperaTerm;
import expr.operator.Poly;
import expr.operator.Trig;

import java.math.BigInteger;
import java.util.ArrayList;

public class Tri implements Factor {
    private final String name;
    private int exponent;
    private int sign;

    private Expr expr;

    public Tri(String name, int exp, int sign, Expr expr) {
        this.exponent = exp;
        this.sign = sign;
        this.name = name;
        this.expr = expr;
    }

    public void setExponent(int exp) {
        this.exponent = exp;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public Tri replace(ArrayList<Power> var, ArrayList<Expr> value) {
        Expr newExpr = this.expr.replace(var, value);
        return new Tri(name,exponent,sign,newExpr);
    }

    public Factor derivative() {
        Factor f = expr.derivative();
        if (f instanceof Expr) {
            if (exponent == 0) {
                return new Num(BigInteger.ZERO,1);
            }
            else {
                Term term = new Term();
                term.addFactor(new Num(BigInteger.valueOf(exponent),1));
                term.addFactor(new Tri(name,exponent - 1,1,expr));
                if (name.equals("sin")) {
                    term.addFactor(new Tri("cos",1,1,expr));
                }
                else {
                    term.addFactor(new Tri("sin",1,-1,expr));
                }
                term.addFactor(f);
                Expr newExpr = new Expr(1, sign);
                newExpr.addTerm(term);
                return newExpr;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (exponent == 0) {
            return "1";
        }
        else if (exponent == 1) {
            return name + "(" + expr.toString() + ")";
        }
        else {
            return name + "(" + expr.toString() + ")^" + exponent;
        }
    }

    @Override
    public Poly toPoly() {
        Poly newPoly = new Poly();
        OperaTerm term = new OperaTerm(BigInteger.valueOf(sign));
        Trig trig = new Trig(expr.toPoly(),exponent,name);
        term.addFactor(trig);
        newPoly.addTerm(term);
        return newPoly;
    }
}
