package expr;

import expr.operator.OperaTerm;
import expr.operator.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Expr implements Factor {
    private final HashSet<Term> terms;
    private int exponent;
    private int sign;

    public Expr(int exponent, int sign) {
        this.terms = new HashSet<>();
        this.exponent = exponent;
        this.sign = sign;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean isEmpty() {
        return terms.isEmpty();
    }

    @Override
    public String toString() {
        Iterator<Term> iter = terms.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append("+");
            sb.append(iter.next().toString());
            while (iter.hasNext()) {
                sb.append("+");
                sb.append(iter.next().toString());
            }
        }
        if (exponent == 0) {
            return "1";
        }
        else if (exponent == 1) {
            return sb.toString();
        }
        else {
            return "(" + sb + ")^" + exponent;
        }
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public Factor derivative() {
        if (exponent == 0) {
            return new Num(BigInteger.ZERO,1);
        }
        Expr newExpr1 = new Expr(1, 1);
        for (Term term : terms) {
            Expr expr = term.derivative();
            for (Term term1 : expr.terms) {
                newExpr1.addTerm(term1);
            }
        }
        Term term = new Term();
        term.addFactor(new Num(BigInteger.valueOf(exponent),1));
        Expr expr = new Expr(exponent - 1, 1);
        for (Term term1 : this.terms) {
            expr.addTerm(term1);
        }
        term.addFactor(expr);
        term.addFactor(newExpr1);
        Expr newExpr = new Expr(1, sign);
        newExpr.addTerm(term);
        return newExpr;
    }

    public Expr replace(ArrayList<Power> var, ArrayList<Expr> value) {
        Expr newExpr = new Expr(exponent, sign);
        for (Term term : terms) {
            newExpr.addTerm(term.replace(var, value));
        }
        return newExpr;
    }

    public Poly toPoly() {
        Poly newPoly = new Poly();
        OperaTerm con = new OperaTerm(BigInteger.valueOf(-1));
        Poly cons = new Poly();
        cons.addTerm(con);
        for (Term term : terms) {
            newPoly = newPoly.add(term.toPoly());
        }
        newPoly = newPoly.pow(exponent);
        if (sign == -1) {
            newPoly = newPoly.multiply(cons);
        }
        return newPoly;
    }
}
