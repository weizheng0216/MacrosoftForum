package edu.lehigh.cse216.macrosoft.tool;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generator {
    public static void main(String[] args) throws IOException {
        String templatePath = "backend/src/main/java/edu/lehigh/cse216/macrosoft/tool/Template.java";
        String definitionPath = "backend/DatabaseDescription";
        String outputPath = "backend/src/main/java/edu/lehigh/cse216/macrosoft/backend/Database.java";
        Generator gen = new Generator(templatePath, definitionPath, outputPath);
        gen.generate();
    }

    private final String outputPath;
    private final String definitionPath;
    private final String templatePath;

    private final String templateClassName;
    private final String outputClassName;

    private PrintWriter out;
    private Parser parser;
    private Scanner templateScan;

    Generator(String templatePath,
              String definitionPath,
              String outputPath) {
        this.templatePath = templatePath;
        this.definitionPath = definitionPath;
        this.outputPath = outputPath;
        Pattern ptn = Pattern.compile("(.*/)?(\\w+)\\.java$");
        Matcher mTemp = ptn.matcher(templatePath);
        Matcher mOut = ptn.matcher(outputPath);
        templateClassName = mTemp.matches() ? mTemp.group(2) : "Template";
        outputClassName = mOut.matches() ? mOut.group(2) : "Database";
    }

    private void initSources() throws IOException {
        // Read/Parse the database description
        byte[] bytes = Files.readAllBytes(Paths.get(definitionPath));
        String defSource = new String(bytes, Charset.defaultCharset());
        parser = new Parser(defSource);
        parser.parse();
        if (parser.hadError()) {
            System.err.println("Something in your description is wrong.\n" +
                    "Good luck finding it!");
            System.exit(-1);
        }
        // Open other resources
        out = new PrintWriter(outputPath, "UTF-8");
        templateScan = new Scanner(new FileReader(templatePath));
    }

    private void closeSources() {
        out.close();
        templateScan.close();
    }

    void generate() throws IOException {
        initSources();
        String line;
        scanTemplate: while (templateScan.hasNextLine()) {
            line = templateScan.nextLine();
            for (TemplateLabel label : TemplateLabel.values()) {
                if (line.contains(label.ptn)) {
                    label.printer.accept(out, parser);
                    continue scanTemplate;
                }
            }
            // Didn't match any labels, copy line
            line = line.replaceAll(templateClassName, outputClassName);
            out.println(line);
        }
        closeSources();
    }

    /* Labels in Template */
    private enum TemplateLabel {
        PACKAGE("[PACKAGE]", TemplateLabel::expandPackage),
        IMPORTS("[IMPORTS]", TemplateLabel::expandImports),
        PREP_STMT("[PREP_STMT]", TemplateLabel::expandPrepStmt),
        INIT_PREP("[INIT_PREP]", TemplateLabel::expandInitPrep),
        OPERATIONS("[OPERATIONS]", TemplateLabel::expandOperations);

        String ptn;
        BiConsumer<PrintWriter, Parser> printer;
        TemplateLabel(String ptn,
                      BiConsumer<PrintWriter, Parser> printer) {
            this.ptn = ptn;
            this.printer = printer;
        }

        // ****************************************************************
        // *                   major expand functions
        // ****************************************************************

        static void expandPackage(PrintWriter out, Parser p) {
            if (p.pkg.length() != 0)
                fprintf(out, 0, "package %s;\n", p.pkg);
        }

        static void expandImports(PrintWriter out, Parser p) {
            for (String importDef : p.imports)
                fprintf(out, 0, "import %s;\n", importDef);
        }

        static void expandPrepStmt(PrintWriter out, Parser p) {
            for (Parser.Operation op : p.ops)
                fprintf(out, 1, "private PreparedStatement m%s;\n",
                        capitalizeInitial(op.name));
        }

        static void expandInitPrep(PrintWriter out, Parser p) {
            for (Parser.Operation op : p.ops)
                fprintf(out, 2, "m%s = mConnection.prepareStatement(\"%s\");\n",
                        capitalizeInitial(op.name), op.prepStmt);
        }

        static void expandOperations(PrintWriter out, Parser p) {
            for (Parser.Operation op : p.ops) {
                String exeType = "";
                switch (op.type) {
                    case "C":
                    case "U":
                    case "D": exeType = "executeUpdate"; break;
                    case "R": exeType = "executeQuery"; break;
                    case "?": exeType = "execute";
                }
                fprintf(out, 1, "%s %s(%s) throws SQLException {\n",
                        op.type.equals("R") ? "ResultSet" : "void",
                        op.name, op.argsStr);
                // set prep_stmt arguments
                int argListLen = op.args.length;
                for (int i = 0; i < argListLen; i++) {
                    String[] arg = op.args[i].split(" ");
                    fprintf(out, 2, "m%s.set%s(%d, %s);\n",
                            capitalizeInitial(op.name),
                            getSetterSuffix(arg[0]), i+1, arg[1]);
                }
                // execute sql
                fprintf(out, 2, "%sm%s.%s();\n",
                        op.type.equals("R") ? "return " : "",
                        capitalizeInitial(op.name), exeType);
                fprintf(out, 1, "}\n\n");
            }
        }

        // ****************************************************************
        // *                     utility functions
        // ****************************************************************

        /* Convert Java type string to prepStmt setter name */
        static String getSetterSuffix(String typeStr) {
            return capitalizeInitial(typeStr);
        }

        /* Capitalize the first letter of input */
        static String capitalizeInitial(String s) {
            if (s.length() != 0)
                return s.substring(0, 1).toUpperCase() + s.substring(1);
            return "";
        }

        /* Just like fprintf, with extra indentation support */
        static void fprintf(PrintWriter out, int in,
                            String s, Object... args) {
            final int INDENT_LEN = 4;
            int spaces = in * INDENT_LEN;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < spaces; i++)
                sb.append(" ");
            out.printf(sb.append(s).toString(), args);
        }
    }
}
