package expr;

import expr.operator.Mono;
import expr.operator.OperaTerm;
import expr.operator.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Power implements Factor {
    private final String name;
    private int exponent;
    private int sign;

    public Power(String name,int exp, int sign) {
        this.name = name;
        this.exponent = exp;
        this.sign = sign;
    }

    public void setExponent(int exp) {
        this.exponent = exp;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public Factor replace(ArrayList<Power> var, ArrayList<Expr> value) {
        for (int i = 0; i < var.size(); i++) {
            if (var.get(i).name.equals(name)) {
                Expr newExpr = new Expr(exponent, sign);
                Term term = new Term();
                term.addFactor(value.get(i));
                newExpr.addTerm(term);
                return newExpr;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        if (exponent == 0) {
            return "1";
        }
        else if (exponent == 1) {
            return name;
        }
        else {
            return name + "^" + exponent;
        }
    }

    public Factor derivative() {
        Expr newExpr = new Expr(1, sign);
        Term term = new Term();
        if (exponent == 0) {
            return new Num(BigInteger.ZERO,1);
        }
        else {
            term.addFactor(new Power(name, exponent - 1, 1));
            term.addFactor(new Num(BigInteger.valueOf(exponent), 1));
            newExpr.addTerm(term);
            return newExpr;
        }
    }

    public Poly toPoly() {
        Poly newPoly = new Poly();
        OperaTerm term = new OperaTerm(BigInteger.valueOf(sign));
        Mono mono = new Mono(exponent,name);
        term.addFactor(mono);
        newPoly.addTerm(term);
        return newPoly;
    }

}
