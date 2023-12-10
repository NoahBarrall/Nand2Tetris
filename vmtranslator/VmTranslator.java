//Written by David Owen and Noah Barrall

package vmtranslator;

import static vmtranslator.CommandType.*;

import java.io.File;
import java.io.IOException;

public class VmTranslator {

    private static void translateFile(Parser parser, CodeWriter writer) {

        while (parser.hasMoreLines()) {
            parser.advance();
            CommandType type = parser.commandType();

            if (type.equals(C_PUSH) || type.equals(C_POP)) {
                writer.writePushPop(type, parser.arg1(), parser.arg2());
            } else if (type.equals(C_LABEL)) {
                writer.writeLabel(parser.arg1());
            } else if (type.equals(C_GOTO)) {
                writer.writeGoto(parser.arg1());
            } else if (type.equals(C_IF)) {
                writer.writeIf(parser.arg1());
            } else if (type.equals(C_FUNCTION)) {
                writer.writeFunction(parser.arg1(), parser.arg2());
            } else if (type.equals(C_RETURN)) {
                writer.writeReturn();
            } else if (type.equals(C_CALL)) {
                writer.writeCall(parser.arg1(), parser.arg2());
            } else { // C_ARITHMETIC
                writer.writeArithmetic(parser.arg1());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String outputFilename = null;

        if (args[0].endsWith(".vm")) {
            outputFilename = args[0].replaceAll(".vm", ".asm");
        } else {
            if (args[0].endsWith("/"))
                args[0] = args[0].substring(0, args[0].length() - 1);

            if (args[0].contains("/")) {
                outputFilename = args[0] +
                    args[0].substring(args[0].lastIndexOf("/")) + ".asm";
            } else {
                outputFilename = args[0] + "/" + args[0] + ".asm";
            }
        }

        CodeWriter writer = new CodeWriter(outputFilename);
        writer.writeInit();


        if (args[0].endsWith(".vm")) {
            Parser parser = new Parser(args[0]);
            translateFile(parser, writer);
            parser.close();
        
        } else {
            for (File file : (new File(args[0])).listFiles()) {
                String filename = file.getName();

                if (filename.endsWith(".vm")) {
                    writer.setFilename(filename.replaceAll(".vm", ""));
                    Parser parser = new Parser(args[0] + "/" + filename);
                    translateFile(parser, writer);
                    parser.close();
                }
            }
        }

        writer.close();
    }
}
