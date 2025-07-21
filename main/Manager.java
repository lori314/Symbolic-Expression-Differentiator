import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import expr.Expr;
import func.Define;
import func.FuncLib;
import func.Recur;

public class Manager {
    public ArrayList<String> read() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> res = new ArrayList<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine().replaceAll("\\s+", "");
            res.add(processString(str));
        }
        int m = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < m * 3; i++) {
            String str = scanner.nextLine().replaceAll("\\s+", "");
            res.add(processString(str));
        }
        String str = scanner.nextLine().replaceAll("\\s+", "");
        res.add(processString(str));
        return res;
    }

    public void print(ArrayList<String> input) {
        int m = (input.size() - 1) / 3;
        int n = input.size() - m * 3 - 1;
        ArrayList<FuncLib> funcLibs = new ArrayList<>();
        Lexer lexer = new Lexer(input.get(3 * m + n));
        Parser parser = new Parser(lexer);
        for (int i = 0; i < n; i++) {
            ArrayList<Define> defines = new ArrayList<>();
            Lexer funcLexer = new Lexer(input.get(i));
            Parser funcParser = new Parser(funcLexer);
            for (FuncLib lib : funcLibs) {
                funcParser.addFuncLib(lib);
            }
            Define define = funcParser.parseEasyFunc();
            defines.add(define);
            FuncLib funcLib = new FuncLib(defines,null);
            funcLibs.add(funcLib);
        }
        for (int i = 0; i < m; i++) {
            ArrayList<Define> defines = new ArrayList<>();
            Recur recur = null;
            for (int j = 0; j < 3; j++) {
                Lexer funcLexer = new Lexer(input.get(j + n));
                Parser funcParser = new Parser(funcLexer);
                for (FuncLib lib : funcLibs) {
                    funcParser.addFuncLib(lib);
                }
                if (funcParser.parseDefine() != null) {
                    defines.add(funcParser.parseDefine());
                }
                else {
                    recur = funcParser.parseRecur();
                }
            }
            funcLibs.add(new FuncLib(defines,recur));
        }
        for (FuncLib lib : funcLibs) {
            parser.addFuncLib(lib);
        }
        Expr expr = parser.parseExpr();
        System.out.println(expr.toPoly().toString());
    }

    private static String processString(String str) {
        String input = str.replaceAll("\\s+", "").replaceAll("\\+\\+", "+")
            .replaceAll("--", "+")
            .replaceAll("-\\+", "-")
            .replaceAll("\\+-", "-")
            .replaceAll("-\\+", "-")
            .replaceAll("--", "+")
            .replaceAll("\\+\\+", "+")
            .replaceAll("(?<![0-9a-zA-Z)])\\+", "");
        // 使用正则表达式匹配字符串中的所有数字部分
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        // 使用StringBuilder构建最终结果
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            // 添加非数字部分
            result.append(input, lastEnd, matcher.start());
            // 处理数字部分
            String processedNumber = processNumber(matcher.group());
            result.append(processedNumber);
            // 更新最后处理的位置
            lastEnd = matcher.end();
        }

        // 添加剩余的非数字部分
        result.append(input.substring(lastEnd));
        return result.toString();
    }

    // 处理单个数字字符串的函数
    private static String processNumber(String number) {
        // 去除前导零
        String noLeadingZeros = number.replaceAll("^0+", "");
        // 如果结果为空（原数字全是零），保留一个零
        if (noLeadingZeros.isEmpty()) {
            return "0";
        }
        // 如果去除前导零后没有变化，说明数字中间的零不需要处理
        if (noLeadingZeros.equals(number)) {
            return number;
        }
        return noLeadingZeros;
    }
}
