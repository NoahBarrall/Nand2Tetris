package compiler.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import static compiler.KeyWord.*;
import static compiler.TokenType.*;

import compiler.JackTokenizer;
import compiler.KeyWord;  // For Keyword declarations.

public class JackTokenizerTest {

    @Test
    public void testDivisionOperator() {
        JackTokenizer tokenizer = new JackTokenizer("/");
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('/', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }

    @Test
    public void testSingleLineComment() {
        JackTokenizer tokenizer = new JackTokenizer(
                "// comment \n /");
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('/', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }

    @Test
    public void testMultiLineComment() {
        JackTokenizer tokenizer = new JackTokenizer(
                "/* comment \n */ /** * // ** \n */ /");
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('/', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testKeywords() {
        String input = "class constructor function method " +
                "field static var int char boolean void true " +
                "false null this let do if else while return";
        KeyWord[] keywords = { CLASS, CONSTRUCTOR, FUNCTION,
                METHOD, FIELD, STATIC, VAR, INT, CHAR, BOOLEAN,
                VOID, TRUE, FALSE, NULL, THIS, LET, DO, IF, ELSE,
                WHILE, RETURN };
        JackTokenizer tokenizer = new JackTokenizer(input);
        
        for (KeyWord k : keywords) {
            assertEquals(KEYWORD, tokenizer.tokenType());
            assertEquals(k, tokenizer.keyWord());
            tokenizer.advance();
        }
        
        assertEquals(false, tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testIdentifiers() {
        JackTokenizer tokenizer = new JackTokenizer(
                "abc _abc abc_123 _abc_123");
        assertEquals(IDENTIFIER, tokenizer.tokenType());
        assertEquals("abc", tokenizer.identifier());
        tokenizer.advance();
        assertEquals(IDENTIFIER, tokenizer.tokenType());
        assertEquals("_abc", tokenizer.identifier());
        tokenizer.advance();
        assertEquals(IDENTIFIER, tokenizer.tokenType());
        assertEquals("abc_123", tokenizer.identifier());
        tokenizer.advance();
        assertEquals(IDENTIFIER, tokenizer.tokenType());
        assertEquals("_abc_123", tokenizer.identifier());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }

    @Test
    public void testIntegerConstants() {
        JackTokenizer tokenizer = new JackTokenizer(
                "0 10 210 3210 \n");
        assertEquals(INT_CONST, tokenizer.tokenType());
        assertEquals(0, tokenizer.intVal());
        tokenizer.advance();
        assertEquals(INT_CONST, tokenizer.tokenType());
        assertEquals(10, tokenizer.intVal());
        tokenizer.advance();
        assertEquals(INT_CONST, tokenizer.tokenType());
        assertEquals(210, tokenizer.intVal());
        tokenizer.advance();
        assertEquals(INT_CONST, tokenizer.tokenType());
        assertEquals(3210, tokenizer.intVal());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }

    @Test
    public void testStringConstants() {
        JackTokenizer tokenizer = new JackTokenizer(
                "\"a\" \"abc\" \"123\" \"{/* */;\" \n");
        assertEquals(STRING_CONST, tokenizer.tokenType());
        assertEquals("a", tokenizer.stringVal());
        tokenizer.advance();
        assertEquals(STRING_CONST, tokenizer.tokenType());
        assertEquals("abc", tokenizer.stringVal());
        tokenizer.advance();
        assertEquals(STRING_CONST, tokenizer.tokenType());
        assertEquals("123", tokenizer.stringVal());
        tokenizer.advance();
        assertEquals(STRING_CONST, tokenizer.tokenType());
        assertEquals("{/* */;", tokenizer.stringVal());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }
    
    
    @Test
    public void testSymbols() {
        JackTokenizer tokenizer = new JackTokenizer("{;>=~");
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('{', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals(';', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('>', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('=', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(SYMBOL, tokenizer.tokenType());
        assertEquals('~', tokenizer.symbol());
        tokenizer.advance();
        assertEquals(false, tokenizer.hasMoreTokens());
    }
}
