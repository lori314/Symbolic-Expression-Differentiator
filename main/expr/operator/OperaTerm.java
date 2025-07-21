package expr.operator;

import java.math.BigInteger;
import java.util.ArrayList;

public class OperaTerm {
    private BigInteger coefficient;
    private ArrayList<OperaFactor> factors;

    public OperaTerm(BigInteger coefficient) {
        this.coefficient = coefficient;
        this.factors = new ArrayList<>();
    }

    public void addFactor(OperaFactor factor) {
        if (factor.toString().equals("1")) {
            return;
        }
        else if (factor.toString().equals("0")) {
            coefficient = BigInteger.ZERO;
            factors.clear();
            return;
        }
        this.factors.add(factor);
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void mergeFactors() {
        for (int i = 0; i < factors.size(); i++) {
            OperaFactor current = factors.get(i);
            for (int j = i + 1; j < factors.size(); j++) {
                OperaFactor next = factors.get(j);
                if (current.addable(next)) {
                    current.setExponent(current.getExponent() + next.getExponent());
                    factors.remove(j);
                    j--;
                }
            }
        }
    }

    public OperaTerm multiply(OperaTerm other) {
        OperaTerm newTerm = new OperaTerm(this.coefficient.multiply(other.coefficient));
        for (int i = 0; i < factors.size(); i++) {
            OperaFactor current = factors.get(i).copy();
            newTerm.addFactor(current);
        }
        for (int i = 0; i < other.factors.size(); i++) {
            OperaFactor current = other.factors.get(i).copy();
            newTerm.addFactor(current);
        }
        newTerm.mergeFactors();
        return newTerm;
    }

    public boolean addable(OperaTerm other) {
        int flag = 0;
        if (factors.size() != other.factors.size()) {
            return false;
        }
        else {
            for (int i = 0; i < factors.size(); i++) {
                OperaFactor current = factors.get(i);
                for (int j = 0; j < other.factors.size(); j++) {
                    OperaFactor next = other.factors.get(j);
                    if (current.isSame(next)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    return false;
                }
                else {
                    flag = 0;
                }
            }
        }
        return true;
    }

    public boolean isSame(OperaTerm other) {
        if (! this.coefficient.equals(other.coefficient)) {
            return false;
        }
        if (this.factors.size() != other.factors.size()) {
            return false;
        }
        for (int i = 0; i < factors.size(); i++) {
            OperaFactor current = factors.get(i);
            boolean found = false;
            for (int j = 0; j < other.factors.size(); j++) {
                OperaFactor next = other.factors.get(j);
                if (current.isSame(next)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public OperaTerm copy() {
        OperaTerm newTerm = new OperaTerm(this.coefficient);
        for (int i = 0; i < factors.size(); i++) {
            OperaFactor current = factors.get(i);
            newTerm.addFactor(current);
        }
        return newTerm;
    }

    public boolean isFactor() {
        if (factors.isEmpty()) {
            return true;
        }
        else {
            return factors.size() == 1 && coefficient.equals(BigInteger.ONE);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (coefficient.equals(BigInteger.ZERO)) {
            return "0";
        }
        else if (coefficient.equals(BigInteger.ONE)) {
            for (int i = 0; i < factors.size(); i++) {
                OperaFactor current = factors.get(i);
                sb.append(current).append("*");
            }
        }
        else if (coefficient.equals(BigInteger.valueOf(-1))) {
            sb.append("-");
            for (int i = 0; i < factors.size(); i++) {
                OperaFactor current = factors.get(i);
                sb.append(current).append("*");
            }
        }
        else {
            sb.append(coefficient).append("*");
            for (int i = 0; i < factors.size(); i++) {
                OperaFactor current = factors.get(i);
                sb.append(current).append("*");
            }
        }
        if (sb.toString().equals("-")) {
            return "-1";
        }
        if (sb.length() == 0) {
            return "1";
        }
        return sb.toString().replaceAll("\\*$", "");
    }
}
