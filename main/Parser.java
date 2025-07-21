import expr.Num;
import expr.Power;
import expr.Tri;
import expr.Func;
import expr.Der;
import expr.Expr;
import expr.Term;
import expr.Factor;
import func.Define;
import func.FuncLib;
import func.Recur;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Lexer lexer;
    private ArrayList<FuncLib> funcLibs;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.funcLibs = new ArrayList<>();
    }

    public void addFuncLib(FuncLib funcLib) {
        funcLibs.add(funcLib);
    }

    public Expr parseExpr() {
        Expr expr = new Expr(1,1);
        expr.addTerm(parseTerm());
        while (!lexer.isEnd() && (lexer.getCurToken().getType() == Token.Type.ADD ||
                lexer.getCurToken().getType() == Token.Type.SUB)) {
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseFactor());
        while (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.MUL) {
            lexer.nextToken();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        Token token = lexer.getCurToken();
        int sign = 1;
        if (token.getType() == Token.Type.SUB) {
            sign = -1;
            lexer.nextToken();
            token = lexer.getCurToken();
        }
        else if (token.getType() == Token.Type.ADD) {
            sign = 1;
            lexer.nextToken();
            token = lexer.getCurToken();
        }
        if (token.getType() == Token.Type.NUM) {
            Num num = parseNum();
            num.setSign(sign);
            return num;
        } else if (token.getType() == Token.Type.POWER) {
            Power power = parsePower();
            power.setSign(sign);
            return power;
        } else if (token.getType() == Token.Type.SIN || token.getType() == Token.Type.COS) {
            Tri tri = parseTri();
            tri.setSign(sign);
            return tri;
        } else if (token.getType() == Token.Type.FUNC) {
            Func func = parseFunc();
            func.setSign(sign);
            return func;
        } else if (token.getType() == Token.Type.DER) {
            Der der = parseDer();
            der.setSign(sign);
            return der;
        } else {
            int dp = 0;
            if (token.getType() == Token.Type.LPAREN) {
                lexer.nextToken();
                dp++;
            }
            Expr expr = parseExpr();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RPAREN && dp == 1) {
                lexer.nextToken();
                if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.EXP) {
                    lexer.nextToken();
                    expr.setExponent(lexer.getCurToken().getContent().charAt(0) - '0');
                    lexer.nextToken();
                }
            }
            expr.setSign(sign);
            return expr;
        }
    }

    public Num parseNum() {
        Num num;
        Token token = lexer.getCurToken();
        lexer.nextToken();
        BigInteger big = new BigInteger(token.getContent());
        num = new Num(big,1);
        return num;
    }

    public Power parsePower() {
        Token token = lexer.getCurToken();
        String name = token.getContent();
        Power power = new Power(name,1,1);
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.EXP) {
            lexer.nextToken();
            power.setExponent(lexer.getCurToken().getContent().charAt(0) - '0');
            lexer.nextToken();
        }
        return power;
    }

    public Tri parseTri() {
        int depth = 0;
        Token token = lexer.getCurToken();
        String name = token.getContent();
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LPAREN) {
            lexer.nextToken();
            depth++;
        }
        Expr expr = parseExpr();
        Tri tri = new Tri(name,1,1,expr);
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RPAREN && depth == 1) {
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.EXP) {
                lexer.nextToken();
                tri.setExponent(lexer.getCurToken().getContent().charAt(0) - '0');
                lexer.nextToken();
            }
        }
        return tri;
    }

    public Func parseFunc() {
        final String name = lexer.getCurToken().getContent();
        int num = 0;
        ArrayList<Expr> args = new ArrayList<>();
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LBRACKET) {
            lexer.nextToken();
            num = Integer.parseInt(lexer.getCurToken().getContent());
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RBRACKET) {
                lexer.nextToken();
            }
        }
        parseArgs(args);
        FuncLib funcLib = null;
        for (int i = 0; i < funcLibs.size(); i++) {
            if (funcLibs.get(i).getName().equals(name)) {
                funcLib = funcLibs.get(i);
            }
        }
        return new Func(name,num,1,args,funcLib);
    }

    public Der parseDer() {
        int depth = 0;
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LPAREN) {
            lexer.nextToken();
            depth++;
        }
        Expr expr = parseExpr();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RPAREN && depth == 1) {
            lexer.nextToken();
        }
        return new Der(expr,1);
    }

    public Define parseEasyFunc() {
        final String name = lexer.getCurToken().getContent();
        ArrayList<Power> args = new ArrayList<>();
        lexer.nextToken();
        getArgsPower(args);
        Expr expr = parseExpr();
        lexer.reset();
        int num = 0;
        return new Define(name,num,expr,args);
    }

    public Define parseDefine() {
        final String name = lexer.getCurToken().getContent();
        int num = 0;
        ArrayList<Power> args = new ArrayList<>();
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LBRACKET) {
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.NUM) {
                num = Integer.parseInt(lexer.getCurToken().getContent());
                lexer.nextToken();
            }
            else {
                lexer.reset();
                return null;
            }
            lexer.nextToken();
        }
        getArgsPower(args);
        Expr expr = parseExpr();
        lexer.reset();
        return new Define(name,num,expr,args);
    }

    private void getArgsPower(ArrayList<Power> args) {
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LPAREN) {
            lexer.nextToken();
            while (!lexer.isEnd() && lexer.getCurToken().getType() != Token.Type.RPAREN) {
                args.add(parsePower());
                if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.COMMA) {
                    lexer.nextToken();
                }
            }
            lexer.nextToken();
        }
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.EQUAL) {
            lexer.nextToken();
        }
    }

    public Recur parseRecur() {
        ArrayList<Power> args = new ArrayList<>();
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LBRACKET) {
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.POWER) {
                lexer.nextToken();
            }
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RBRACKET) {
                lexer.nextToken();
            }
        }
        getArgsPower(args);
        int sign1 = 1;
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.SUB) {
            sign1 = -1;
            lexer.nextToken();
        }
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.ADD) {
            sign1 = 1;
            lexer.nextToken();
        }
        final BigInteger num1 = parseRecurNum();
        ArrayList<Expr> args1 = new ArrayList<>();
        parseArgs(args1);
        int sign2 = 1;
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.SUB) {
            sign2 = -1;
            lexer.nextToken();
        }
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.ADD) {
            sign2 = 1;
            lexer.nextToken();
        }
        final BigInteger num2 = parseRecurNum();
        ArrayList<Expr> args2 = new ArrayList<>();
        parseArgs(args2);
        Expr expr = new Expr(1,1);
        if (!lexer.isEnd()) {
            expr = parseExpr();
        }
        lexer.reset();
        if (num1 != null) {
            if (num2 != null) {
                return new Recur(args,expr,num1.multiply(BigInteger.valueOf(sign1)),
                        num2.multiply(BigInteger.valueOf(sign2)),args1,args2);
            }
        }
        return null;
    }

    private void parseArgs(ArrayList<Expr> args1) {
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LPAREN) {
            lexer.nextToken();
            while (!lexer.isEnd() && lexer.getCurToken().getType() != Token.Type.RPAREN) {
                args1.add(parseExpr());
                if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.COMMA) {
                    lexer.nextToken();
                }
            }
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RPAREN) {
                lexer.nextToken();
            }
        }
    }

    private BigInteger parseRecurNum() {
        BigInteger num = null;
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.NUM) {
            num = new BigInteger(lexer.getCurToken().getContent());
            lexer.nextToken();
        }
        lexer.nextToken();
        if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.FUNC) {
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.LBRACKET) {
                lexer.nextToken();
                lexer.nextToken();
                lexer.nextToken();
            }
            lexer.nextToken();
            if (!lexer.isEnd() && lexer.getCurToken().getType() == Token.Type.RBRACKET) {
                lexer.nextToken();
            }
        }
        return num;
    }
}

