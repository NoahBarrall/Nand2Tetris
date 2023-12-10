//Written by David Owen and Noah Barrall

package vmtranslator;

import static vmtranslator.CommandType.*;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.Scanner;

public class Parser {

    private Scanner input;
    private String currentCommand, nextCommand;
    private String arg1;
    private int arg2;

    public Parser(String fileNameOrInputString) {
        try {
            input = new Scanner(new FileInputStream(
                fileNameOrInputString));
        } catch (IOException e) {
            input = new Scanner(fileNameOrInputString);
        }

        skipToNext();
    }

    public void close() {
        input.close();
    }

    public boolean hasMoreLines() {
        return nextCommand != null;
    }

    private void skipToNext() {
        nextCommand = null;
        
        while (input.hasNextLine()) {
            String line = input.nextLine();
            int endIndex = line.indexOf("//");
            if (endIndex != -1) line = line.substring(0, line.indexOf("//"));
            line = line.strip().replaceAll("[ \t]+", " ");

            if (!line.equals("")) {
                nextCommand = line;
                break;
            }
        }
    }

    public void advance() {
        String[] tokens = nextCommand.split(" ");
        currentCommand = tokens[0];

        if (tokens.length >= 2) arg1 = tokens[1];
        if (tokens.length >= 3) arg2 = Integer.parseInt(tokens[2]);

        skipToNext();
    }

    public CommandType commandType() {

        if (currentCommand.startsWith("push")) {
            return C_PUSH;
        } else if (currentCommand.startsWith("pop")) {
            return C_POP;
        } else if (currentCommand.startsWith("label")) {
            return C_LABEL;
        } else if (currentCommand.startsWith("goto")) {
            return C_GOTO;
        } else if (currentCommand.startsWith("if-goto")){
            return C_IF; 
        } else if (currentCommand.startsWith("function")){
            return C_FUNCTION;
        } else if (currentCommand.startsWith("return")) {
            return C_RETURN;
        } else if (currentCommand.startsWith("call")) {
            return C_CALL;
        } else {
            return C_ARITHMETIC;
        }
    }

    public String arg1() {

        if (commandType().equals(C_ARITHMETIC)) {
            return currentCommand;
        } else {
            return arg1;
        }
    }

    public int arg2() {
        return arg2;
    }

    // Not part of API, just for testing.
    public static void main(String[] args) {

    }
}
