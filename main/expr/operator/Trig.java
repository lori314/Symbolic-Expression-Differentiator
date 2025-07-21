package expr.operator;

public class Trig implements OperaFactor {
    private Poly poly;
    private int exponent;
    private String name;

    public Trig(Poly poly, int exponent,String name) {
        this.poly = poly;
        this.exponent = exponent;
        this.name = name;
    }

    @Override
    public String toString() {
        if (exponent == 0) {
            return "1";
        }
        else if (poly.isZero() && name.equals("sin")) {
            return "0";
        }
        else if (poly.isZero() && name.equals("cos")) {
            return "1";
        }
        else if (exponent == 1) {
            if (poly.isFactor()) {
                return name + "(" + poly.toString() + ")";
            }
            else {
                return name + "((" + poly.toString() + "))";
            }
        }
        else {
            if (poly.isFactor()) {
                return name + "(" + poly.toString() + ")^" + exponent;
            }
            else {
                return name + "((" + poly.toString() + "))^" + exponent;
            }
        }
    }

    @Override
    public int getExponent() {
        return this.exponent;
    }

    @Override
    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public boolean isSame(OperaFactor factor) {
        if (factor instanceof Trig) {
            Trig trig = (Trig) factor;
            if (! this.name.equals(trig.name)) {
                return false;
            }
            if (this.exponent != trig.exponent) {
                return false;
            }
            return this.poly.isSame(trig.poly);
        }
        return false;
    }

    public boolean addable(OperaFactor factor) {
        if (factor instanceof Trig) {
            Trig trig = (Trig) factor;
            if (! this.name.equals(trig.name)) {
                return false;
            }
            return this.poly.isSame(trig.poly);
        }
        return false;
    }

    @Override
    public OperaFactor copy() {
        return new Trig(this.poly.copy(), this.exponent,this.name);
    }
}
