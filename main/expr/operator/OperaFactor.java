package expr.operator;

public interface OperaFactor {
    int getExponent();

    void setExponent(int exponent);

    boolean isSame(OperaFactor factor);

    boolean addable(OperaFactor factor);

    OperaFactor copy();

    String toString();
}
