//Written by David Owen and Noah Barrall

package compiler;

import static compiler.KeyWord.*;
import static compiler.TokenType.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class CompilationEngine {

    private static final int TAB_WIDTH = 2;
    private int indentLevel = 0;
    private final JackTokenizer tokenizer;
    private final PrintStream out;

    public CompilationEngine(String inputFilename,
            String outputFilename) throws FileNotFoundException,
            IOException {
        tokenizer = new JackTokenizer(inputFilename);
        out = new PrintStream(outputFilename);
        compileClass();
        tokenizer.close();
        out.close();
    }

    private void writeIndentedLine(String line) {
        String s = "";

        for (int i = 0; i < indentLevel * TAB_WIDTH; i++)
            s += " ";

        out.println(s + line);
        System.out.println(s + line); // Just for testing.
    }

    // Here is the rule, from page 201, that corresponds to this
    // method:
    //
    // class: 'class' className '{' classVarDec* subroutineDec* '}'
    //
    // The meaning of the rule is: a class begins with the keyword
    // "class." After that there is a className (an identifier),
    // a curly bracket, zero or more (that's what the star means)
    // classVarDecs, zero or more subroutineDecs, and finally a
    // closing curly bracket.
    private void compileClass() {
        writeIndentedLine("<class>");
        indentLevel++;

        // 'class'
        // (We could call tokenizer.keyword() here, but it's safe
        // to just assume the first token is the keyword "class";
        // it has to be. In general, any time the next token is
        // predictable we won't bother to retrieve it from the
        // tokenizer. If we were writing a compiler that did
        // error checking we'd have to check for other things
        // besides the keyword "class." But writing that sort
        // of compiler would be much more difficult.)
        writeIndentedLine("<keyword> class </keyword>");
        tokenizer.advance();

        // className
        // (Although there's a grammar rule for className, the .xml files
        // from the textbook website don't include a set of tags for it,
        // so we won't either.)
        writeIndentedLine("<identifier> " + tokenizer.identifier() +
                " </identifier>");
        tokenizer.advance();

        // '{'
        writeIndentedLine("<symbol> { </symbol>");
        tokenizer.advance();

        // classVarDec*
        // (When we have zero or more of something, we'll have a
        // while loop. The condition will check for any token
        // indicating that another one of these things is coming.
        // For example, this loop is checking for the beginning of
        // another varDec, which could be indicated by the keyword
        // "field" or the keyword "static."
        while (tokenizer.tokenType().equals(KEYWORD) &&
                (tokenizer.keyWord().equals(FIELD) ||
                        (tokenizer.keyWord().equals(STATIC)))) {
            compileClassVarDec();
        }

        while (tokenizer.tokenType().equals(KEYWORD) &&
                (tokenizer.keyWord().equals(CONSTRUCTOR) ||
                        (tokenizer.keyWord().equals(FUNCTION) ||
                                (tokenizer.keyWord().equals(METHOD))))) {
            compileSubroutineDec();
        }

        // '}'
        writeIndentedLine("<symbol> } </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</class>");
    }

    // classVarDec: ( 'static' | 'field' ) type varName (',' varName)* ';'
    private void compileClassVarDec() {
        writeIndentedLine("<classVarDec>");
        indentLevel++;

        // ( 'static' | 'field' )
        if (tokenizer.keyWord().equals(STATIC)) {
            writeIndentedLine("<keyword> static </keyword>");
        } else { // tokenizer.keyWord().equals(FIELD)
            writeIndentedLine("<keyword> field </keyword>");
        }

        tokenizer.advance();

        // type
        compileType();

        // varName
        compileVarName();

        // (',' varName)*
        while (tokenizer.tokenType().equals(SYMBOL) &&
                tokenizer.symbol() == ',') {
            // ','
            writeIndentedLine("<symbol> , </symbol>");
            tokenizer.advance();

            // varName
            compileVarName();
        }

        // ';'
        writeIndentedLine("<symbol> ; </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</classVarDec>");
    }

    // type: 'int' | 'char' | 'boolean' | className
    private void compileType() {
        // No <type> tag, since type isn't included in the list
        // on page 202.

        // 'int' | 'char' | 'boolean'
        if (tokenizer.tokenType().equals(KEYWORD)) {
            if (tokenizer.keyWord().equals(INT)) {
                writeIndentedLine("<keyword> int </keyword>");
            } else if (tokenizer.keyWord().equals(CHAR)) {
                writeIndentedLine("<keyword> char </keyword>");
            } else if (tokenizer.keyWord().equals(BOOLEAN)) {
                writeIndentedLine("<keyword> boolean </keyword>");
            }
            tokenizer.advance();

            // className
        } else {
            compileClassName();
        }
    }

    private void compileSubroutineDec() {
        writeIndentedLine("<subroutineDec>");
        indentLevel++;

        if (tokenizer.keyWord().equals(CONSTRUCTOR)) {
            writeIndentedLine("<keyword> constructor </keyword>");
        } else if (tokenizer.keyWord().equals(FUNCTION)) {
            writeIndentedLine("<keyword> function </keyword>");
        } else {
            writeIndentedLine("<keyword> method </keyword>");
        }

        tokenizer.advance();

        if (tokenizer.keyWord().equals(VOID)) {
            writeIndentedLine("<keyword> void </keyword>");
            tokenizer.advance();
        } else {
            // Type
            compileType();
        }

        // subroutineName
        compileSubroutineName();

        writeIndentedLine("<symbol> ( </symbol>");
        tokenizer.advance();

        compileParameterList();

        writeIndentedLine("<symbol> ) </symbol>");
        tokenizer.advance();

        compileSubroutineBody();

        indentLevel--;
        writeIndentedLine("</subroutineDec>");
    }

    private void compileParameterList() {
        writeIndentedLine("<parameterList>");
        indentLevel++;
        if (!(tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == ')')) {
            compileType();
            compileVarName();

            while (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == ',') {
                writeIndentedLine("<symbol> , </symbol>");
                tokenizer.advance();

                // Type
                compileType();

                // VarName
                compileVarName();
            }
        }

        indentLevel--;
        writeIndentedLine("</parameterList>");
    }

    private void compileSubroutineBody() {
        writeIndentedLine("<subroutineBody>");
        indentLevel++;

        writeIndentedLine("<symbol> { </symbol>");
        tokenizer.advance();

        while (tokenizer.tokenType().equals(KEYWORD) &&
                (tokenizer.keyWord().equals(VAR))) {
            compileVarDec();
        }

        compileStatements();

        writeIndentedLine("<symbol> } </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</subroutineBody>");
    }

    private void compileVarDec() {
        writeIndentedLine("<varDec>");
        indentLevel++;

        writeIndentedLine("<keyword> var </keyword>");
        tokenizer.advance();

        // Type
        compileType();

        // varName
        compileVarName();

        // (',' varName)*
        while (tokenizer.tokenType().equals(SYMBOL) &&
                tokenizer.symbol() == ',') {
            // ','
            writeIndentedLine("<symbol> , </symbol>");
            tokenizer.advance();

            // varName
            compileVarName();
        }

        // ';'
        writeIndentedLine("<symbol> ; </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</varDec>");
    }

    private void compileClassName() {
        writeIndentedLine("<identifier> " + tokenizer.identifier() + " </identifier>");
        tokenizer.advance();
    }

    private void compileSubroutineName() {
        writeIndentedLine("<identifier> " + tokenizer.identifier() + " </identifier>");
        tokenizer.advance();
    }

    // varName: identifier
    private void compileVarName() {
        writeIndentedLine("<identifier> " + tokenizer.identifier() +
                " </identifier>");
        tokenizer.advance();
    }

    private void compileStatements() {
        writeIndentedLine("<statements>");
        indentLevel++;

        while (!tokenizer.tokenType().equals(SYMBOL) || tokenizer.symbol() != '}') {
            compileStatement();
        }
        indentLevel--;
        writeIndentedLine("</statements>");
    }

    private void compileStatement() {
        if (tokenizer.keyWord() == DO) {
            compileDoStatement();
        } else if (tokenizer.keyWord() == LET) {
            compileLetStatement();
        } else if (tokenizer.keyWord() == IF) {
            compileIfStatement();
        } else if (tokenizer.keyWord() == WHILE) {
            compileWhileStatement();
        } else if (tokenizer.keyWord() == RETURN) {
            compileReturnStatement();
        }
    }

    private void compileLetStatement() {
        writeIndentedLine("<letStatement>");
        indentLevel++;

        // 'let'
        writeIndentedLine("<keyword> let </keyword>");
        tokenizer.advance();

        // varName
        compileVarName();

        if (tokenizer.tokenType().equals(SYMBOL) && (tokenizer.symbol() == '[')) {
            writeIndentedLine("<symbol> [ </symbol>");
            tokenizer.advance();

            // expression
            compileExpression();
            tokenizer.advance();

            if (tokenizer.tokenType().equals(SYMBOL) && (tokenizer.symbol() == ']')) {
                writeIndentedLine("<symbol> ] </symbol>");
            }
            tokenizer.advance();
        }

        writeIndentedLine("<symbol> = </symbol>");
        tokenizer.advance();

        // expression
        compileExpression();

        // ';'
        writeIndentedLine("<symbol> ; </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</letStatement>");
    }

    private void compileIfStatement() {
        writeIndentedLine("<ifStatement>");
        indentLevel++;

        // 'let'
        writeIndentedLine("<keyword> if </keyword>");
        tokenizer.advance();

        writeIndentedLine("<symbol> ( </symbol>");
        tokenizer.advance();

        compileExpression();

        writeIndentedLine("<symbol> ) </symbol>");
        tokenizer.advance();

        writeIndentedLine("<symbol> { </symbol>");
        tokenizer.advance();

        compileStatements();

        writeIndentedLine("<symbol> } </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</ifStatement>");
    }

    private void compileWhileStatement() {
        writeIndentedLine("<whileStatement>");
        indentLevel++;

        writeIndentedLine("<keyword> while </keyword>");
        tokenizer.advance();

        writeIndentedLine("<symbol> ( </symbol>");
        tokenizer.advance();

        // expression
        compileExpression();

        writeIndentedLine("<symbol> ) </symbol>");
        tokenizer.advance();

        writeIndentedLine("<symbol> { </symbol>");
        tokenizer.advance();

        // statements
        compileStatements();

        writeIndentedLine("<symbol> } </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</whileStatement>");
    }

    private void compileReturnStatement() {
        writeIndentedLine("<returnStatement>");
        indentLevel++;

        // 'return'
        writeIndentedLine("<keyword> return </keyword>");
        tokenizer.advance();

        if (!(tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == ';')) {
            // expression
            compileExpression();
        }

        // ';'
        writeIndentedLine("<symbol> ; </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</returnStatement>");
    }

    private void compileDoStatement() {
        writeIndentedLine("<doStatement>");
        indentLevel++;

        writeIndentedLine("<keyword> do </keyword>");
        tokenizer.advance();

        // subroutineCall
        compileSubroutineCall();

        // ;
        writeIndentedLine("<symbol> ; </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</doStatement>");
    }

    private void compileExpression() {
        writeIndentedLine("<expression>");
        indentLevel++;

        // term
        compileTerm();

        while (isOperator()) {
            compileOp();
            compileTerm();
        }

        indentLevel--;
        writeIndentedLine("</expression>");
    }

    private boolean isOperator() {
        boolean isOperator = tokenizer.tokenType().equals(SYMBOL) &&
                (tokenizer.symbol() == '+' || tokenizer.symbol() == '-' ||
                        tokenizer.symbol() == '*' || tokenizer.symbol() == '/' ||
                        tokenizer.symbol() == '&' || tokenizer.symbol() == '|' ||
                        tokenizer.symbol() == '<' || tokenizer.symbol() == '>' ||
                        tokenizer.symbol() == '=');

        return isOperator;
    }

    private void compileTerm() {
        writeIndentedLine("<term>");
        indentLevel++;

        if (tokenizer.tokenType().equals(IDENTIFIER)) {
            String identifier = tokenizer.identifier();
            tokenizer.advance();

            // Check for array access
            if (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == '[') {
                writeIndentedLine("<identifier> " + identifier + " </identifier>");
                writeIndentedLine("<symbol> [ </symbol>");
                tokenizer.advance();

                // expression
                compileExpression();

                writeIndentedLine("<symbol> ] </symbol>");
                tokenizer.advance();
            } else if (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == '(') {
                // subroutine call without an object or class name
                writeIndentedLine("<subroutineCall>");
                indentLevel++;
                writeIndentedLine("<identifier> " + identifier + " </identifier>");
                writeIndentedLine("<symbol> ( </symbol>");
                tokenizer.advance();

                // expressionList
                compileExpressionList();

                writeIndentedLine("<symbol> ) </symbol>");
                tokenizer.advance();
                indentLevel--;
                writeIndentedLine("</subroutineCall>");
            } else if (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == '.') {
                // subroutine call with an object or class name
                writeIndentedLine("<subroutineCall>");
                indentLevel++;
                writeIndentedLine("<identifier> " + identifier + " </identifier>");
                writeIndentedLine("<symbol> . </symbol>");
                tokenizer.advance();

                // subroutineName
                writeIndentedLine("<identifier> " + tokenizer.identifier() + " </identifier>");
                tokenizer.advance();

                writeIndentedLine("<symbol> ( </symbol>");
                tokenizer.advance();

                // expressionList
                compileExpressionList();

                writeIndentedLine("<symbol> ) </symbol>");
                tokenizer.advance();
                indentLevel--;
                writeIndentedLine("</subroutineCall>");
            } else {
                // Just an identifier without array access or subroutine call
                writeIndentedLine("<identifier> " + identifier + " </identifier>");
            }
        } else if (tokenizer.tokenType().equals(INT_CONST)) {
            writeIndentedLine("<integerConstant> " + tokenizer.intVal() + " </integerConstant>");
            tokenizer.advance();
        } else if (tokenizer.tokenType().equals(STRING_CONST)) {
            writeIndentedLine("<stringConstant> " + tokenizer.stringVal() + " </stringConstant>");
            tokenizer.advance();
        } else if (tokenizer.tokenType().equals(KEYWORD)) {
            if (tokenizer.keyWord().equals(TRUE)) {
                writeIndentedLine("<keyword> true </keyword>");
            } else if (tokenizer.keyWord().equals(FALSE)) {
                writeIndentedLine("<keyword> false </keyword>");
            } else if (tokenizer.keyWord().equals(NULL)) {
                writeIndentedLine("<keyword> null </keyword>");
            } else {
                writeIndentedLine("<keyword> this </keyword>");
            }
            tokenizer.advance();
        } else if (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == '(') {
            // expression in parentheses
            writeIndentedLine("<symbol> ( </symbol>");
            tokenizer.advance();

            // expression
            compileExpression();

            writeIndentedLine("<symbol> ) </symbol>");
            tokenizer.advance();
        } else if (tokenizer.tokenType().equals(SYMBOL) && (tokenizer.symbol() == '-' || tokenizer.symbol() == '~')) {
            writeIndentedLine("<symbol> " + tokenizer.symbol() + " </symbol>");
            tokenizer.advance();
            compileTerm();
        }

        indentLevel--;
        writeIndentedLine("</term>");
    }

    private void compileSubroutineCall() {
        writeIndentedLine("<subroutineCall>");
        indentLevel++;

        // subroutineName or (className | varName)
        writeIndentedLine("<identifier> " + tokenizer.identifier() + " </identifier>");
        tokenizer.advance();

        if (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == '.') {
            writeIndentedLine("<symbol> . </symbol>");
            tokenizer.advance();

            // subroutineName
            writeIndentedLine("<identifier> " + tokenizer.identifier() + " </identifier>");
            tokenizer.advance();
        }

        writeIndentedLine("<symbol> ( </symbol>");
        tokenizer.advance();

        compileExpressionList();

        writeIndentedLine("<symbol> ) </symbol>");
        tokenizer.advance();

        indentLevel--;
        writeIndentedLine("</subroutineCall>");
    }

    private void compileExpressionList() {
        writeIndentedLine("<expressionList>");
        indentLevel++;

        if (!(tokenizer.tokenType().equals(SYMBOL) && (tokenizer.symbol() == ')' || tokenizer.symbol() == ';'))) {
            compileExpression();

            // (',' expression)*
            while (tokenizer.tokenType().equals(SYMBOL) && tokenizer.symbol() == ',') {
                writeIndentedLine("<symbol> , </symbol>");
                tokenizer.advance();

                // expression
                compileExpression();
            }
        }

        indentLevel--;
        writeIndentedLine("</expressionList>");
    }

    private void compileOp() {
        if (isOperator()) {
            String symbol = "";
            switch (tokenizer.symbol()) {
                case '<':
                    symbol = "&lt;";
                    break;
                case '>':
                    symbol = "&gt;";
                    break;
                case '&':
                    symbol = "&amp;";
                    break;
                default:
                    symbol = Character.toString(tokenizer.symbol());
                    break;
            }
            writeIndentedLine("<symbol> " + symbol + " </symbol>");
            tokenizer.advance();
        }
    }

    private void compileUnaryOp() {
        if (tokenizer.tokenType().equals(SYMBOL) &&
                (tokenizer.symbol() == '-' || tokenizer.symbol() == '~')) {
            writeIndentedLine("<symbol> " + tokenizer.symbol() + " </symbol>");
            tokenizer.advance();
        }
    }
}