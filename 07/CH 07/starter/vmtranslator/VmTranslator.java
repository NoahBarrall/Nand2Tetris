package vmtranslator;

import static vmtranslator.CommandType.*;

import java.io.IOException;

//Written by David Owen and Noah Barrall

public class VmTranslator {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(args[0]);
        CodeWriter writer = new CodeWriter(
            args[0].substring(0, args[0].lastIndexOf(".")) + ".asm");

        while (parser.hasMoreLines()) {
            parser.advance();

            // Get parts of VM language command from parser, use
            // writer to output corresponding assembly commands.
            if (parser.commandType().equals(C_ARITHMETIC)){
                writer.writeArithmetic(parser.arg1());
            } else if (parser.commandType().equals(C_PUSH) || parser.commandType().equals(C_POP)){
                writer.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
            }
        }
        
        parser.close();
        writer.close();
    }
}
