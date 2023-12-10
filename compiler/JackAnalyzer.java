package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JackAnalyzer {
    
    public static void parse(String dirName) {

        try {
            for (File file : (new File(dirName)).listFiles()) {
                String n = file.getName();

                if (n.endsWith(".jack"))
                    new CompilationEngine(
                            dirName + "/" + n, dirName + "/" +
                            n.replace(".jack", ".xml"));
            }

        } catch (FileNotFoundException e) {
            System.err.println(
                    "File not found.  Bad input directory name?");
            
        } catch (IOException e) {
            System.err.println("Trouble closing an input file.");
        }
    }

    public static void main(String[] args) {
        parse(args[0]);
    }
}
