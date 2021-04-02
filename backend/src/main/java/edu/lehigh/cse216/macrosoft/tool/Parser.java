package edu.lehigh.cse216.macrosoft.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {
    private final String source;

    private Scanner scan;
    private boolean hadError;

    /* Exposed fields that will be read by the generator */
    String pkg;
    final List<String> imports = new ArrayList<>();
    final List<Operation> ops = new ArrayList<>();

    private enum StmtType {  // ********************************************************
        PACKAGE("package\\s+([\\w.]+)", StmtType::loadPackageStmt),
        IMPORT("import\\s+([\\w.*]+)", StmtType::loadImportStmt),
        METHOD("\\[([CRUD?])]\\s+(\\w+)\\(([\\s\\w,]*)\\)", StmtType::loadMethodStmt),
        PREP_STMT(".*", StmtType::loadPrepStmt);

        final Pattern ptn;
        BiConsumer<Parser, Matcher> valueLoader;
        StmtType(String ptn, BiConsumer<Parser, Matcher> c) {
            this.ptn = Pattern.compile(ptn);
            valueLoader = c;
        }

        boolean loadValues(Parser p, String stmt) {
            Matcher matcher = ptn.matcher(stmt);
            if (!matcher.matches()) return false;
            valueLoader.accept(p, matcher);
            return true;
        }

        static void loadPackageStmt(Parser p, Matcher matcher) {
            p.pkg = matcher.group(1);
        }

        static void loadImportStmt(Parser p, Matcher matcher) {
            p.imports.add(matcher.group(2));
        }

        static void loadMethodStmt(Parser p, Matcher matcher) {
            String type = matcher.group(1);
            String name = matcher.group(2);
            String argsStr = matcher.group(3);
            String prepStmt = p.readStmt();
            p.ops.add(new Operation(type, name, argsStr, prepStmt));
        }

        static void loadPrepStmt(Parser p, Matcher matcher) {
            p.hadError = true;
        }
    }  // *****************************************************************************

    Parser(String source) {
        this.source = source
                .replaceAll("#.*", "")
                .replaceAll("[\\s\\n]{2,}", " ");
    }

    boolean hadError() {
        return hadError;
    }

    void parse() {
        scan = new Scanner(source);
        scan.useDelimiter(";");
        pkg = "";
        hadError = false;
        imports.clear();
        ops.clear();
        String stmt;
        parseSource: while ((stmt = readStmt()) != null) {
            for (StmtType type : StmtType.values()) {
                if (type.loadValues(this, stmt))
                    continue parseSource;
            }
            hadError = true;
        }
        scan.close();
    }

    private String readStmt() {
        if (scan.hasNext()) {
            String s = scan.next() .trim();
            return s.length() == 0 ? readStmt() : s;
        }
        return null;
    }

    static class Operation {
        // Operation fields      Example: selectPostById(int id)
        final String type;             //  R
        final String name;             //  selectPostById
        final String[] args;           //  String[] {"int id", "..."}
        final String argsStr;          //  "int id, ..."
        final String prepStmt;         //  SELECT * FROM posts...

        Operation(String type, String name,
                  String argsStr, String prepStmt) {
            this.type = type;
            this.name = name;
            this.prepStmt = prepStmt;
            this.args = argsStr.length() == 0 ? new String[]{} :
                    argsStr.split("\\s*,\\s*");
            this.argsStr = argsStr;
        }
    }
}
