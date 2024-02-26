package org.example.lang

import org.junit.jupiter.api.Test
import java.io.StreamTokenizer
import java.io.StringReader



class testTokenzier {
    @Test
    fun test01() {
        val reader = StringReader("( Hello World 1234)")
        val tokenizer = StreamTokenizer(reader)
//        tokenizer.resetSyntax()
//        tokenizer.eolIsSignificant(true)
//        tokenizer.whitespaceChars('\t'.code, '\t'.code)
//        tokenizer.whitespaceChars(' '.code, ' '.code)
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
                println("Word: " + tokenizer.sval)
            } else if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                println("Number: " + tokenizer.nval)
            } else {
                println(tokenizer.sval)
            }
        }
    }
}