//Written by David Owen and Noah Barrall

package vmtranslator;

import static vmtranslator.CommandType.*;

import java.io.IOException;
import java.io.PrintStream;

public class CodeWriter {

    private PrintStream out;
    private String filename;
    private int labelCount;

    public CodeWriter(String filename) throws IOException {
        out = new PrintStream(filename);
        this.filename = filename.substring(
            filename.lastIndexOf("/") + 1, filename.length() - 4);
        labelCount = 0;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void close() {
        out.close();
    }
    
    public void writePushPop(CommandType commandType,
        String segment, int index) {
        out.println();
        
        if (commandType.equals(C_PUSH)) {
            out.println("    // push " + segment + " " + index);

            if (segment.equals("constant")) {
                out.println("    @" + index);
                out.println("    D=A");
            
            } else if (segment.equals("local") || segment.equals("argument") ||
                segment.equals("this") || segment.equals("that")) {

                out.println("    @" + index);
                out.println("    D=A");

                if (segment.equals("local")) {
                    out.println("    @LCL");
                } else if (segment.equals("argument")) {
                    out.println("    @ARG");
                } else if (segment.equals("this")) {
                    out.println("    @THIS");
                } else if (segment.equals("that")) {
                    out.println("    @THAT");
                }

                out.println("    A=D+M");
                out.println("    D=M");

            } else if (segment.equals("pointer") || segment.equals("temp")) {

                if (segment.equals("pointer")) {
                    out.println("    @" + (3 + index));
                } else if (segment.equals("temp")) {
                    out.println("    @" + (5 + index));
                }

                out.println("    D=M");
            
            } else if (segment.equals("static")) {
                out.println("    @" + filename + "." + index);
                out.println("    D=M");
            }

            // push D
            out.println("    @SP");
            out.println("    M=M+1");
            out.println("    A=M-1");
            out.println("    M=D");
        
        } else if (commandType.equals(C_POP)) {
            out.println("    // pop " + segment + " " + index);

            if (segment.equals("local") || segment.equals("argument") ||
                segment.equals("this") || segment.equals("that")) {
                
                out.println("    @" + index);
                out.println("    D=A");

                if (segment.equals("local")) {
                    out.println("    @LCL");
                } else if (segment.equals("argument")) {
                    out.println("    @ARG");
                } else if (segment.equals("this")) {
                    out.println("    @THIS");
                } else if (segment.equals("that")) {
                    out.println("    @THAT");
                }

                out.println("    D=D+M");
                out.println("    @R13");
                out.println("    M=D");

                out.println("    @SP");
                out.println("    AM=M-1");
                out.println("    D=M");

                out.println("    @R13");
                out.println("    A=M");
                out.println("    M=D");

            } else if (segment.equals("pointer") || segment.equals("temp")) {

                out.println("    @SP");
                out.println("    AM=M-1");
                out.println("    D=M");

                if (segment.equals("pointer")) {
                    out.println("    @" + (3 + index));
                } else if (segment.equals("temp")) {
                    out.println("    @" + (5 + index));
                }

                out.println("    M=D");
            
            } else if (segment.equals("static")) {

                out.println("    @SP");
                out.println("    AM=M-1");
                out.println("    D=M");

                out.println("    @" + filename + "." + index);
                out.println("    M=D");
            }
        }
    }

    public void writeLabel(String label) {
        out.println("//Label " + label);
        out.println("(" + label + ")");
            
        }
    
    public void writeGoto(String label) {
        out.println("@" + label);
        out.println("    0;JMP");
        }
    


    public void writeIf(String label) {
        out.println("    @SP");
        out.println("    M=M-1");
        out.println("    A=M");
        out.println("    D=M");
        out.println("@" + label);
        out.println("    D;JNE");
        }
    
    public void writeFunction(String functionName, int numLocals){
        out.println("//Label " + functionName);
        out.println("(" + functionName + ")");

        //Initialize local variables to 0
        for (int i = 0; i < numLocals; i++) {
            writePushPop(C_PUSH, "constant", 0);
        }

    }

    public void writeReturn() {
        // R13 = LCL
        out.println("    @LCL");
        out.println("    D=M");
        out.println("    @R13");
        out.println("    M=D");

        // R14 = M[R13 - 5]
        out.println("    @5");
        out.println("    A=D-A"); //5=LCL-5
        out.println("    D=M"); //Storing LCL in D
        out.println("    @R14"); 
        out.println("    M=D");

        // SP--
        out.println("    @SP"); 
        out.println("    M=M-1"); //Decrementing stack pointer

        // M[ARG] = M[SP]
        out.println("    @SP"); //Loads SP to A
        out.println("    A=M");
        out.println("    D=M");
        out.println("    @ARG"); //Loads ARG to A
        out.println("    A=M");
        out.println("    M=D");

        // SP = ARG + 1
        out.println("    @ARG");
        out.println("    D=M");
        out.println("    @SP");
        out.println("    M=D+1");

        // THAT = M[R13 - 1] // or R13--; THAT = M[R13]
        out.println("    @R13");
        out.println("    A=M-1");
        out.println("    D=M");
        out.println("    @THAT"); 
        out.println("    M=D");

        // THIS = M[R13 - 2] //    R13--; THIS = M[R13]
        out.println("    @R13");
        out.println("    D=M");
        out.println("    @2");
        out.println("    A=D-A");
        out.println("    D=M");
        out.println("    @THIS"); 
        out.println("    M=D");

        // ARG = M[R13 - 3]  //    R13--; ARG = M[R13]
        out.println("    @R13");
        out.println("    D=M");
        out.println("    @3");
        out.println("    A=D-A");
        out.println("    D=M");
        out.println("    @ARG"); 
        out.println("    M=D");

        // LCL = M[R13 - 4]  //    R13--; LCL = M[R13]
        out.println("    @R13");
        out.println("    D=M");
        out.println("    @4");
        out.println("    A=D-A");
        out.println("    D=M");
        out.println("    @LCL"); 
        out.println("    M=D");

        // goto R14
        out.println("    @R14");
        out.println("    A=M");
        out.println("    0;JMP");
    }

    public void writeCall(String functionName, int numArgs) {

        // M[SP++] = RETURN_n (n to make a unique label)
        out.println("    @RETURN_" + labelCount); 
        out.println("    D=A"); //Save return address to the stack
        out.println("    @SP");
        out.println("    A=M"); 
        out.println("    M=D"); //setting SP to the return address
        out.println("    @SP");
        out.println("    M=M+1"); //incrementing SP

  
        // M[SP++] = LCL
        out.println("    @LCL");
        out.println("    D=M");
        out.println("    @SP");
        out.println("    A=M");
        out.println("    M=D");
        out.println("    @SP");
        out.println("    M=M+1");

        // M[SP++] = ARG
        out.println("    @ARG");
        out.println("    D=M");
        out.println("    @SP");
        out.println("    A=M");
        out.println("    M=D");
        out.println("    @SP");
        out.println("    M=M+1");

        // M[SP++] = THIS
        out.println("    @THIS");
        out.println("    D=M");
        out.println("    @SP");
        out.println("    A=M");
        out.println("    M=D");
        out.println("    @SP");
        out.println("    M=M+1");

        // M[SP++] = THAT
        out.println("    @THAT");
        out.println("    D=M");
        out.println("    @SP");
        out.println("    A=M");
        out.println("    M=D");
        out.println("    @SP");
        out.println("    M=M+1");

        // ARG = SP - 6 (i.e.,  SP - 5 - numArgs)
        out.println("    @SP");
        out.println("    D=M");
        out.println("    @" + (5+numArgs));
        out.println("    A=D-A");
        out.println("    @ARG");
        out.println("    M=D");

        // LCL = SP
        out.println("    @SP");
        out.println("    D=M");
        out.println("    @LCL");
        out.println("    M=D");

        // goto Main.fibonacci
        writeGoto(functionName);

       // RETURN_n: 
        writeLabel("RETURN_" + labelCount++);
        
    }

        public void writeInit() {
            out.println("    @256"); //loading 256 into A Register
            out.println("    D=A");
            out.println("    @SP");
            out.println("    M=D");
            writeCall("Sys.init", 0);

        } 

    public void writeArithmetic(String command) {
        out.println("\n    // " + command);

        if (command.equals("add") || command.equals("sub") ||
            command.equals("and") || command.equals("or")) {

            out.println("    @SP");
            out.println("    AM=M-1");
            out.println("    D=M");
            out.println("    A=A-1");

            if (command.equals("add")) {
                out.println("    M=M+D");
            } else if (command.equals("sub")) {
                out.println("    M=M-D");
            } else if (command.equals("and")) {
                out.println("    M=M&D");
            } else if (command.equals("or")) {
                out.println("    M=M|D");
            }
        
        } else if (command.equals("eq") || command.equals("lt") ||
            command.equals("gt")) {
            
            out.println("    @SP");
            out.println("    AM=M-1");
            out.println("    D=M");
            out.println("    A=A-1");
            out.println("    D=M-D");
            out.println("    M=-1");
            out.println("    @END_" + labelCount);
            
            if (command.equals("eq")) {
                out.println("    D;JEQ");
            } else if (command.equals("lt")) {
                out.println("    D;JLT");
            } else if (command.equals("gt")) {
                out.println("    D;JGT");
            }

            out.println("    @SP");
            out.println("    A=M-1");
            out.println("    M=0");
            out.println("(END_" + labelCount++ + ")");
        
        } else if (command.equals("neg") || command.equals("not")) {

            out.println("    @SP");
            out.println("    A=M-1");

            if (command.equals("neg")) {
                out.println("    M=-M");
            } else if (command.equals("not")) {
                out.println("    M=!M");
            }
        }
    }
}                                                                        
