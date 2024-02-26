package org.example.lang;

import org.example.policy.Yylex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestYylex {
    public static void main(String[] args) throws IOException {
        Yylex scanner = null;
        java.io.FileInputStream stream = null;
        java.io.Reader reader = null;
        var file = "C:\\Users\\xielongyu\\Desktop\\abe\\ly-cpabe\\src\\main\\resources\\input.txt";
        stream = new java.io.FileInputStream(file);
        reader = new java.io.InputStreamReader(stream, StandardCharsets.UTF_8);
        scanner = new Yylex(reader);
        while ( !scanner.yyatEOF() ) {
            var x = scanner.yylex();
            System.out.println("x = " + x);
        }
    }
}
