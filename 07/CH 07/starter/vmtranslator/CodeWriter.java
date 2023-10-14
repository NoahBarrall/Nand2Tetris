package vmtranslator;

import static vmtranslator.CommandType.*;

import java.io.IOException;
import java.io.PrintStream;

//Written by David Owen and Noah Barrall


public class CodeWriter {

    private PrintStream out;
    private int labelCount;
    private String filename;

    public CodeWriter(String filename) throws IOException {
        out = new PrintStream(filename);
        int k = filename.lastIndexOf("/");
        this.filename = filename.substring(k+1);
        labelCount = 0;
    }

    public void close() {
        out.close();
    }
    
    public void writePushPop(CommandType commandType,
        String segment, int index) {
        out.println();
        
        if (commandType.equals(C_PUSH)) {
            out.println("    // push " + segment + " " + index);

                // Add code to output (nicely formatted) assembly
                // implementation of push command.
            if (segment.equals("constant")){
                //D=index
                out.println("@" + index);
                out.println("D=A");
            
                //push D on to the stack (example on page 137)
                out.println("@SP");
                out.println("M=M+1");
                out.println("A=M-1");
                out.println("M=D");
            } else if (segment.equals("local")){
                out.println("@LCL");
                out.println("D=M");
                out.println("@" + index);
                out.println("A=D+A");
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("this")){
                out.println("@THIS");
                out.println("D=M");
                out.println("@" + index);
                out.println("A=D+A");
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("that")){
                out.println("@THAT");
                out.println("D=M");
                out.println("@" + index);
                out.println("A=D+A");
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("argument")){
                out.println("@ARG");
                out.println("D=M");
                out.println("@" + index);
                out.println("A=D+A");
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("temp")){
                out.println("@" + (5 + index));
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("pointer")){
                out.println("@" + (3 + index));
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            } else if (segment.equals("static")){
                out.println("@" + filename + "." + index);
                out.println("D=M");
                out.println("@SP");
                out.println("A=M");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M+1");
            }

        } else if (commandType.equals(C_POP)) {
            out.println("    // pop " + segment + " " + index);

            // Add code to output (nicely formatted) assembly ...
            if (segment.equals("local")){
                out.println("@LCL");
                out.println("D=M");
                out.println("@" + index);
                out.println("D=D+A");
                out.println("@R13");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@R13");
                out.println("A=M");
                out.println("M=D");
            } else if (segment.equals("this")){
                out.println("@THIS");
                out.println("D=M");
                out.println("@" + index);
                out.println("D=D+A");
                out.println("@R13");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@R13");
                out.println("A=M");
                out.println("M=D");
            } else if (segment.equals("that")){
                out.println("@THAT");
                out.println("D=M");
                out.println("@" + index);
                out.println("D=D+A");
                out.println("@R13");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@R13");
                out.println("A=M");
                out.println("M=D");
            } else if (segment.equals("argument")){
                out.println("@ARG");
                out.println("D=M");
                out.println("@" + index);
                out.println("D=D+A");
                out.println("@R13");
                out.println("M=D");
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@R13");
                out.println("A=M");
                out.println("M=D");
            } else if (segment.equals("temp")){
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@" + (5 + index));
                out.println("M=D");
            } else if (segment.equals("pointer")){
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@" + (3 + index));
                out.println("M=D");
            } else if (segment.equals("static")){
                out.println("@SP");
                out.println("M=M-1");
                out.println("A=M");
                out.println("D=M");
                out.println("@" + filename + "." + index);
                out.println("M=D");
            }
            
        }
    }

    public void writeArithmetic(String command) {
        out.println("\n    // " + command);

        // Add code to output (nicely formatted) assembly ...
        if (command.equals("add")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A-1");
            out.println("M=D+M");
            out.println("@SP");
            out.println("M=M-1");
        } else if (command.equals("sub")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A-1");
            out.println("M=M-D"); 
            out.println("@SP");
            out.println("M=M-1");  
        } else if (command.equals("or")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A-1");
            out.println("M=D|M");
            out.println("@SP");
            out.println("M=M-1");
        } else if (command.equals("and")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A-1");
            out.println("M=D&M");
            out.println("@SP");
            out.println("M=M-1");
        } else if (command.equals("eq")){
            out.println("@SP");
            out.println("M=M-1");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A+1");
            out.println("D=D-M");
            out.println("@FALSE" + labelCount);
            out.println("D;JNE");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=-1");
            out.println("@END" + labelCount);
            out.println("0;JMP");
            out.println("(FALSE" + labelCount + ")");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=0");
            out.println("(END" + labelCount + ")");
            labelCount++;
        } else if (command.equals("lt")){
            out.println("@SP");
            out.println("M=M-1");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A+1");
            out.println("D=D-M");
            out.println("@FALSE" + labelCount);
            out.println("D;JGE");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=-1");
            out.println("@END" + labelCount);
            out.println("0;JMP");
            out.println("(FALSE" + labelCount + ")");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=0");
            out.println("(END" + labelCount + ")");
            labelCount++;

        } else if (command.equals("gt")){
            out.println("@SP");
            out.println("M=M-1");
            out.println("A=M-1");
            out.println("D=M");
            out.println("A=A+1");
            out.println("D=D-M");
            out.println("@FALSE" + labelCount);
            out.println("D;JLE");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=-1");
            out.println("@END" + labelCount);
            out.println("0;JMP");
            out.println("(FALSE" + labelCount + ")");
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=0");
            out.println("(END" + labelCount + ")");
            labelCount++;
        } else if (command.equals("not")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=!M");
        } else if (command.equals("neg")){
            out.println("@SP");
            out.println("A=M-1");
            out.println("M=-M");
        }

    }

    // Not part of API, just for testing.
    public static void main(String[] args) {

    }
}                                                                        
