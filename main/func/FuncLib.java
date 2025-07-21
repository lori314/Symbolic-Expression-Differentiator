package func;

import expr.Expr;

import java.util.ArrayList;

public class FuncLib {
    private String name;
    private ArrayList<Define> defines;
    private Recur recur;

    public FuncLib(ArrayList<Define> defines, Recur recur) {
        this.defines = defines;
        this.recur = recur;
        this.name = defines.get(0).getName();
        if (defines.get(0).getNum() != 0) {
            Define define = defines.get(0);
            this.defines.remove(0);
            this.defines.add(define);
        }
        recurrence();
    }

    public String getName() {
        return name;
    }

    private void recurrence() {
        if (defines.size() < 2) {
            return;
        }
        for (int i = 2; i <= 5; i++) {
            defines.add(recur.getDefine(i,name,this));
        }
    }

    public Expr getValue(ArrayList<Expr> args,int num) {
        Define define = defines.get(num);
        return define.calExpr(args);
    }
}
