package expr;

import expr.operator.OperaTerm;
import expr.operator.Poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Term {
    private final HashSet<Factor> factors;

    public Term() {
        this.factors = new HashSet<>();
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public Term replace(ArrayList<Power> var, ArrayList<Expr> value) {
        Term newTerm = new Term();
        for (Factor factor : this.factors) {
            Factor newFactor = factor.replace(var, value);
            newTerm.addFactor(newFactor);
        }
        return newTerm;
    }

    public Expr derivative() {
        Expr newExpr = new Expr(1, 1);
        for (Factor factor : this.factors) {
            Term newTerm = new Term();
            newTerm.addFactor(factor.derivative());
            for (Factor factor1 : this.factors) {
                if (factor != factor1) {
                    newTerm.addFactor(factor1);
                }
            }
            newExpr.addTerm(newTerm);
        }
        return newExpr;
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString());
        if (iter.hasNext()) {
            sb.append("*");
            sb.append(iter.next().toString());
            while (iter.hasNext()) {
                sb.append("*");
                sb.append(iter.next().toString());
            }
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Poly newPoly = new Poly();
        OperaTerm term = new OperaTerm(BigInteger.ONE);
        newPoly.addTerm(term);
        for (Factor factor : factors) {
            newPoly = newPoly.multiply(factor.toPoly());
        }
        return newPoly;
    }
}
