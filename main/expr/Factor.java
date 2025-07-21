package expr;

import expr.operator.Poly;

import java.util.ArrayList;

public interface Factor {
    Poly toPoly();

    Factor derivative();

    Factor replace(ArrayList<Power> var, ArrayList<Expr> value);
}
