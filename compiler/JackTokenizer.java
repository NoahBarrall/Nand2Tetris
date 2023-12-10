//Written by David Owen and Noah Barrall


package compiler;

import static compiler.KeyWord.*;
import static compiler.TokenType.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class JackTokenizer {

    private BufferedReader input;
    private char nextCharacter;
    private TokenType tokenType;
    private KeyWord keyWord;
    private String currentToken;

    public JackTokenizer(String filenameOrInputString) {

        try {
            input = new BufferedReader(
                    new FileReader(filenameOrInputString));
        } catch (FileNotFoundException e) {
            input = new BufferedReader(
                    new StringReader(filenameOrInputString));
        }

        readNextCharacter();  // Set up for first call to advance.
        advance();  // Read first token (into currentToken field).
    }
    
    private void readNextCharacter() {
        
        try {
            nextCharacter = (char) input.read();
            
        } catch (IOException e) {
            System.err.println("Problem reading file :(");
            System.exit(0);
        }
    }
    
    public boolean hasMoreTokens() {
        return (nextCharacter != 65535 /* EOF */);
    }
    
    public void advance() {
        
        if (nextCharacter == '/') {
            readNextCharacter();
            commentOrDivisionOperator();
            
        } else if (Character.isWhitespace(nextCharacter)) {
            readNextCharacter();
            
            advance();  // Skip ahead to next token after
                        // whitespace.
            
        } else if (nextCharacter == '_' ||
                Character.isLetter(nextCharacter)) {
            currentToken = "" + nextCharacter;
            readNextCharacter();
            keywordOrIdentifier();    
        } else if (Character.isDigit(nextCharacter)) {
            currentToken = "";
            integerConstant();
        } else if (nextCharacter == '@' || nextCharacter == '#') {
            tokenType = SYMBOL;
            currentToken = "" + nextCharacter;
            readNextCharacter();
        } else if (nextCharacter == '{' || nextCharacter == '}' ||
                nextCharacter == ',' || nextCharacter == ';' ||
                nextCharacter == '(' || nextCharacter == ')' ||
                nextCharacter == '[' || nextCharacter == ']' ||
                nextCharacter == '.' || nextCharacter == '+' ||
                nextCharacter == '-' || nextCharacter == '*' || 
                nextCharacter == '/' || nextCharacter == '&' || 
                nextCharacter == '|' || nextCharacter == '<' || 
                nextCharacter == '>' || nextCharacter == '=' || 
                nextCharacter == '~') {
            tokenType = SYMBOL;
            currentToken = "" + nextCharacter;
            readNextCharacter();  // Prepare for next call to
                                  // advance.
        } else if (nextCharacter == '\"') {
            tokenType = STRING_CONST;
            currentToken = readStringConstant();
            readNextCharacter();  // Prepare for next call to advance.
        }
    }
    
    private String readStringConstant() {
        StringBuilder stringBuilder = new StringBuilder();
        readNextCharacter(); // Move past the opening double quote
    
        while (nextCharacter != '\"') {
            stringBuilder.append(nextCharacter);
            readNextCharacter();
        }
    
        readNextCharacter(); // Move past the closing double quote
        return stringBuilder.toString();
    }
    

    private void commentOrDivisionOperator() {
        
        if (nextCharacter == '/') {
            readNextCharacter();
            singleLineComment();
            
        } else if (nextCharacter == '*') {
            readNextCharacter();
            multiLineComment();
            
        } else {
            tokenType = SYMBOL;
            currentToken = "/";
            // Don't need to call readNextCharacter to prepare
            // for advance, since the character after the / has
            // already been read.
        }
    }
    
    private void singleLineComment() {

        if (nextCharacter == '\n') {
            readNextCharacter();
            advance();  // Skip ahead to next token after comment.
        
        } else {
            readNextCharacter();
            singleLineComment();
        }
    }
    
    private void multiLineComment() {
        
        if (nextCharacter == '*') {
            readNextCharacter();
            multiLineCommentAfterStar();
        
        } else {
            readNextCharacter();
            multiLineComment();
        }
    }
    
    private void multiLineCommentAfterStar() {
        
        if (nextCharacter == '/') {
            readNextCharacter();
            advance();
        
        } else if (nextCharacter == '*') {
            readNextCharacter();
            multiLineCommentAfterStar();
        
        } else {
            readNextCharacter();
            multiLineComment();
        }
    }
    
    private void keywordOrIdentifier() {
        
        if (nextCharacter == '_' ||
                Character.isLetterOrDigit(nextCharacter)) {
            currentToken += nextCharacter;
            readNextCharacter();
            keywordOrIdentifier();
            
        } else if (currentToken.equals("class")) {
            tokenType = KEYWORD;
            keyWord = CLASS;
            // Don't need to call readNextCharacter to prepare
            // for advance, since the character after the keyword
            // has already been read.
            
        } else if (currentToken.equals("field")) {
            tokenType = KEYWORD;
            keyWord = FIELD;
            
        } else if (currentToken.equals("static")) {
            tokenType = KEYWORD;
            keyWord = STATIC;
            
        } else if (currentToken.equals("int")) {
            tokenType = KEYWORD;
            keyWord = INT;
            
        } else if (currentToken.equals("char")) {
            tokenType = KEYWORD;
            keyWord = CHAR;
    
        } else if (currentToken.equals("boolean")) {
            tokenType = KEYWORD;
            keyWord = BOOLEAN;

        } else if (currentToken.equals("constructor")) {
            tokenType = KEYWORD;
            keyWord = CONSTRUCTOR;
        } else if (currentToken.equals("function")) {
            tokenType = KEYWORD;
            keyWord = FUNCTION;
        } else if (currentToken.equals("method")) {
            tokenType = KEYWORD;
            keyWord = METHOD;
        } else if (currentToken.equals("var")) {
            tokenType = KEYWORD;
            keyWord = VAR;
        } else if (currentToken.equals("void")) {
            tokenType = KEYWORD;
            keyWord = VOID;
        } else if (currentToken.equals("true")) {
            tokenType = KEYWORD;
            keyWord = TRUE;
        } else if (currentToken.equals("false")) {
            tokenType = KEYWORD;
            keyWord = FALSE;
        } else if (currentToken.equals("null")) {
            tokenType = KEYWORD;
            keyWord = NULL;
        } else if (currentToken.equals("this")) {
            tokenType = KEYWORD;
            keyWord = THIS;
        } else if (currentToken.equals("let")) {
            tokenType = KEYWORD;
            keyWord = LET;
        } else if (currentToken.equals("do")) {
            tokenType = KEYWORD;
            keyWord = DO;
        } else if (currentToken.equals("if")) {
            tokenType = KEYWORD;
            keyWord = IF;
        } else if (currentToken.equals("else")) {
            tokenType = KEYWORD;
            keyWord = ELSE;
        } else if (currentToken.equals("while")) {
            tokenType = KEYWORD;
            keyWord = WHILE;
        } else if (currentToken.equals("return")) {
            tokenType = KEYWORD;
            keyWord = RETURN;
        } else {
            tokenType = IDENTIFIER;
            // Don't need to call readNextCharacter to prepare for
            // advance, since the character after the identifier
            // has already been read.
        }
    }

    private void integerConstant() {
        while (Character.isDigit(nextCharacter)) {
            currentToken += nextCharacter;
            readNextCharacter();
        }
        tokenType = INT_CONST;
    }
    

    public TokenType tokenType() {
        return tokenType;
    }
    
    public KeyWord keyWord() {
        return keyWord;
    }
    
    public char symbol() {
        return currentToken.charAt(0);
    }
    
    public String identifier() {
        return currentToken;
    }
    
    // Not used in simpler test programs, but needed so that
    // JackTonizerTest compiles successfully.
    public int intVal() {
        return Integer.parseInt(currentToken);
    }
    
    // Not used in simpler test programs, but needed so that
    // JackTokenizerTest compiles successfully.
    public String stringVal() {
        return currentToken;
    }

    public void close() throws IOException {
        input.close();
    }
}
