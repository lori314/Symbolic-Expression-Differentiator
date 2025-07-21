package expr.operator;

import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private final ArrayList<OperaTerm> terms;

    public Poly() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(OperaTerm term) {
        this.terms.add(term);
    }

    public void mergeTerms() {
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i);
            for (int j = i + 1; j < terms.size(); j++) {
                OperaTerm next = terms.get(j);
                if (current.addable(next)) {
                    current.setCoefficient(current.getCoefficient().add(next.getCoefficient()));
                    terms.remove(j);
                    j--;
                }
            }
        }
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i);
            if (current.getCoefficient().equals(BigInteger.ZERO)) {
                terms.remove(i);
                i--;
            }
        }
    }

    public Poly add(Poly other) {
        Poly newPoly = new Poly();
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i);
            newPoly.addTerm(current);
        }
        for (int i = 0; i < other.terms.size(); i++) {
            OperaTerm current = other.terms.get(i);
            newPoly.addTerm(current);
        }
        newPoly.mergeTerms();
        return newPoly;
    }

    public Poly multiply(Poly other) {
        Poly newPoly = new Poly();
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i);
            for (int j = 0; j < other.terms.size(); j++) {
                OperaTerm next = other.terms.get(j);
                OperaTerm newTerm = current.multiply(next);
                newPoly.addTerm(newTerm);
            }
        }
        newPoly.mergeTerms();
        return newPoly;
    }

    public Poly pow(int exponent) {
        Poly newPoly = new Poly();
        OperaTerm term = new OperaTerm(BigInteger.ONE);
        newPoly.addTerm(term);
        for (int i = 0; i < exponent; i++) {
            newPoly = newPoly.multiply(this);
        }
        newPoly.mergeTerms();
        return newPoly;
    }

    public Poly copy() {
        Poly newPoly = new Poly();
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i).copy();
            newPoly.addTerm(current);
        }
        return newPoly;
    }

    public boolean isSame(Poly other) {
        if (this.terms.size() != other.terms.size()) {
            return false;
        }
        for (int i = 0; i < terms.size(); i++) {
            OperaTerm current = terms.get(i);
            boolean found = false;
            for (int j = 0; j < other.terms.size(); j++) {
                OperaTerm next = other.terms.get(j);
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

    public boolean isFactor() {
        if (this.terms.size() != 1) {
            return false;
        }
        else if (!this.terms.get(0).isFactor()) {
            return false;
        }
        return true;
    }

    public boolean isZero() {
        return terms.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OperaTerm term : terms) {
            if (term.getCoefficient().compareTo(BigInteger.ZERO) <= 0) {
                continue;
            }
            sb.append(term).append("+");
        }
        for (OperaTerm term : terms) {
            if (term.getCoefficient().compareTo(BigInteger.ZERO) >= 0) {
                continue;
            }
            sb.append(term).append("+");
        }
        if (sb.length() == 0) {
            return "0";
        }
        return sb.toString().replaceAll("\\+$", "")
                .replaceAll("\\+-","-")
                .replaceAll("-\\+","-");
    }
}
