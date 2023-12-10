package vmtranslator.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import static vmtranslator.CommandType.*;
import vmtranslator.Parser;

public class ParserTest {

    @Test
    public void testArithmetic() {
        Parser parser = new Parser("add \n sub \n neg \n eq \n " +
                "gt \n lt \n and \n or \n not");
        
        for (String s : new String[] { "add", "sub", "neg", "eq",
                "gt", "lt", "and", "or", "not" }) {
            parser.advance();
            assertEquals(C_ARITHMETIC, parser.commandType());
            assertEquals(s, parser.arg1());
        }
    }

    @Test
    public void testPush() {
        Parser parser = new Parser("push constant 3");
        parser.advance();
        assertEquals(C_PUSH, parser.commandType());
        assertEquals("constant", parser.arg1());
        assertEquals(3, parser.arg2());
    }

    @Test
    public void testPop() {
        Parser parser = new Parser("pop pointer 1");
        parser.advance();
        assertEquals(C_POP, parser.commandType());
        assertEquals("pointer", parser.arg1());
        assertEquals(1, parser.arg2());
    }

    @Test
    public void testCommentsAndWhiteSpace() {
        Parser parser = new Parser(
                " ////\n// ab cd \n \t push  constant   5 \n// efg");
        parser.advance();
        assertEquals(C_PUSH, parser.commandType());
        assertEquals("constant", parser.arg1());
        assertEquals(5, parser.arg2());
        assertEquals(false, parser.hasMoreLines());
    }
}
