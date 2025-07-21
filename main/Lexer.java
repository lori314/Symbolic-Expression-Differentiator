import java.util.ArrayList;

public class Lexer {
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int index = 0;

    public Lexer(String input) {
        deal(input);
    }

    public Token getCurToken() {
        return tokens.get(index);
    }

    public void nextToken() {
        index++;
    }

    public void reset() {
        index = 0;
    }

    public void deal(String input) {
        int pos = 0;
        while (pos < input.length()) {
            if (input.charAt(pos) == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "("));
                pos++;
            } else if (input.charAt(pos) == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")"));
                pos++;
            } else if (input.charAt(pos) == '+') {
                tokens.add(new Token(Token.Type.ADD, "+"));
                pos++;
            } else if (input.charAt(pos) == '-') {
                tokens.add(new Token(Token.Type.SUB, "-"));
                pos++;
            } else if (input.charAt(pos) == '*') {
                tokens.add(new Token(Token.Type.MUL, "*"));
                pos++;
            } else if (input.charAt(pos) == '^') {
                tokens.add(new Token(Token.Type.EXP,"^"));
                pos++;
            } else if (input.charAt(pos) == 's') {
                tokens.add(new Token(Token.Type.SIN, "sin"));
                pos = pos + 3;
            } else if (input.charAt(pos) == 'c') {
                tokens.add(new Token(Token.Type.COS, "cos"));
                pos = pos + 3;
            } else if (input.charAt(pos) == 'f' || input.charAt(pos) == 'g'
                || input.charAt(pos) == 'h') {
                tokens.add(new Token(Token.Type.FUNC, input.charAt(pos) + ""));
                pos++;
            } else if (input.charAt(pos) == 'd') {
                tokens.add(new Token(Token.Type.DER, "dx"));
                pos = pos + 2;
            } else if (input.charAt(pos) == ',') {
                tokens.add(new Token(Token.Type.COMMA, ","));
                pos++;
            } else if (input.charAt(pos) == '{') {
                tokens.add(new Token(Token.Type.LBRACKET, "{"));
                pos++;
            } else if (input.charAt(pos) == '}') {
                tokens.add(new Token(Token.Type.RBRACKET, "}"));
                pos++;
            } else if (input.charAt(pos) == '=') {
                tokens.add(new Token(Token.Type.EQUAL, "="));
                pos++;
            } else if (input.charAt(pos) >= 'a' && input.charAt(pos) <= 'z') {
                tokens.add(new Token(Token.Type.POWER, input.substring(pos, pos + 1)));
                pos++;
            } else {
                pos = processNumber(input, pos);
            }
        }
    }

    private int processNumber(String input, int p) {
        int pos = p;
        char now = input.charAt(pos);
        StringBuilder sb = new StringBuilder();
        while (now >= '0' && now <= '9') {
            sb.append(now);
            pos++;
            if (pos >= input.length()) {
                break;
            }
            now = input.charAt(pos);
        }
        tokens.add(new Token(Token.Type.NUM, sb.toString()));
        return pos;
    }

    public boolean isEnd() {
        return index >= tokens.size();
    }
}
