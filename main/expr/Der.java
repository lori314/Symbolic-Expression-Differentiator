package expr;

import expr.operator.Poly;

import java.util.ArrayList;

public class Der implements Factor {
    private Expr expr;
    private int sign;

    public Der(Expr expr, int sign) {
        this.expr = expr;
        this.sign = sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public Poly toPoly() {
        return getValue().toPoly();
    }

    public Factor derivative() {
        Expr value = getValue();
        return new Der(value, sign);
    }

    public Expr getValue() {
        Factor f = expr.derivative();
        if (f instanceof Expr) {
            Expr expr = (Expr) f;
            expr.setSign(sign);
            return expr;
        }
        return null;
    }

    public Factor replace(ArrayList<Power> var, ArrayList<Expr> value) {
        Expr newExpr = getValue().replace(var, value);
        return new Der(newExpr, sign);
    }
}
