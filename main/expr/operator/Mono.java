package expr.operator;

public class Mono implements OperaFactor {
    private int exponent;
    private String name;

    public Mono(int exponent, String name) {
        this.exponent = exponent;
        this.name = name;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public boolean isSame(OperaFactor factor) {
        if (factor instanceof Mono) {
            Mono mono = (Mono) factor;
            return mono.toString().equals(this.toString());
        }
        return false;
    }

    public boolean addable(OperaFactor factor) {
        if (factor instanceof Mono) {
            Mono mono = (Mono) factor;
            return mono.name.equals(this.name);
        }
        return false;
    }

    @Override
    public OperaFactor copy() {
        return new Mono(this.exponent, this.name);
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
}
