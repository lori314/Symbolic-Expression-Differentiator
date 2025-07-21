package expr;

import expr.operator.Mono;
import expr.operator.OperaTerm;
import expr.operator.Poly;

import java.math.BigInteger;
import java.util.ArrayList;

public class Num implements Factor {
    private final BigInteger num;
    private int sign;

    public Num(BigInteger num, int sign) {
        this.num = num;
        this.sign = sign;
    }

    public String toString() {
        return this.num.toString();
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public Factor replace(ArrayList<Power> var, ArrayList<Expr> value) {
        return this;
    }

    public Poly toPoly() {
        Poly newPoly = new Poly();
        OperaTerm term = new OperaTerm(num.multiply(BigInteger.valueOf(sign)));
        Mono mono = new Mono(0,"");
        term.addFactor(mono);
        newPoly.addTerm(term);
        return newPoly;
    }

    public Factor derivative() {
        return new Num(BigInteger.ZERO,1);
    }
}
