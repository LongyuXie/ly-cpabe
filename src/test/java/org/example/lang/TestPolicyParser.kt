package org.example.lang

import it.unisa.dia.gas.plaf.jpbc.util.io.Base64.InputStream
import org.example.policy.PolicyLexer
import org.example.policy.PolicyParser
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class TestPolicyParser {

    @Test
    fun testParser() {
        val testList = listOf<Pair<String, Boolean>>(
            Pair("a", true),
            Pair("a or b", true),
            Pair("a and b", true),
            Pair("(a or b)", true),
            Pair("(a, b)", false),
            Pair("0 of (a, b)", true),
            Pair("1, of (c)", false),
            Pair("3 of (a, b, c, d)", true),
            Pair("adw34", true),
            Pair("2 of (a, b, c) and x", true)
        )
        testList.forEach {
//            val input = it.first.byteInputStream()
            val input = ByteArrayInputStream(it.first.toByteArray())
            val lexer = PolicyLexer(input)
            val parser = PolicyParser(lexer)
            val ans = parser.parse()
            println("${it.first} -> ${ans}, expected -> ${it.second}")
            assertEquals(it.second, ans)
        }

    }
}