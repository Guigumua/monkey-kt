package com.github.guigumua.parser

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestLexer {
    @Test
    fun test() {
        val input = """
            1 1.1 "string" + - * / = > < ! >= <= == != && || ( ) { } [ ] , : ; ->
            let if else return while fn
            "\"hello"
            hello
        """.trimIndent()
        val lexer = Lexer(input)
        val tests = listOf(
            TokenKind.Integer to "1",
            TokenKind.Float to "1.1",
            TokenKind.String to "\"string\"",
            TokenKind.Plus to "+",
            TokenKind.Minus to "-",
            TokenKind.Asterisk to "*",
            TokenKind.Slash to "/",
            TokenKind.Assign to "=",
            TokenKind.GreaterThan to ">",
            TokenKind.LessThan to "<",
            TokenKind.Bang to "!",
            TokenKind.GreaterThanEquals to ">=",
            TokenKind.LessThanEquals to "<=",
            TokenKind.Equals to "==",
            TokenKind.NotEquals to "!=",
            TokenKind.And to "&&",
            TokenKind.Or to "||",
            TokenKind.OpenParen to "(",
            TokenKind.CloseParen to ")",
            TokenKind.OpenBrace to "{",
            TokenKind.CloseBrace to "}",
            TokenKind.OpenBracket to "[",
            TokenKind.CloseBracket to "]",
            TokenKind.Comma to ",",
            TokenKind.Colon to ":",
            TokenKind.SemiColon to ";",
            TokenKind.Arrow to "->",
            TokenKind.Let to "let",
            TokenKind.If to "if",
            TokenKind.Else to "else",
            TokenKind.Return to "return",
            TokenKind.While to "while",
            TokenKind.Function to "fn",
            TokenKind.String to "\"\\\"hello\"",
            TokenKind.Identifier to "hello",
            TokenKind.Eof to "",
        )
        for (test in tests) {
            val nextToken = lexer.nextToken()
            assertEquals(test.first, nextToken.kind)
            assertEquals(test.second, nextToken.textSpan.literal)
            assertEquals(input.subSequence(nextToken.textSpan.start, nextToken.textSpan.end), nextToken.textSpan.literal)
        }
    }
}
