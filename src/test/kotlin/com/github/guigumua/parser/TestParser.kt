package com.github.guigumua.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


class TestParser {
    @Test
    fun testLetStatement() {
        val tests = listOf(
            "let a = 1;" to AstKind.IntegerLiteral,
            "let a = 1.1;" to AstKind.FloatLiteral,
            "let a = true;" to AstKind.BooleanLiteral,
            "let a = false;" to AstKind.BooleanLiteral,
            "let a = \"hello\";" to AstKind.StringLiteral,
            "let a = add(1,2);" to AstKind.FunctionCallExpression,
            "let a = add(1,2) + 1;" to AstKind.BinaryExpression,
            "let a = b;" to AstKind.Identifier,
            "let a = fn() {};" to AstKind.FunctionExpression,
            "let a = fn(b,c) {};" to AstKind.FunctionExpression,
            "let a = fn(b,c) { a+c; };" to AstKind.FunctionExpression,
        )
        for ((input, expectAstKind) in tests) {
            val lexer = Lexer(input)
            val parser = Parser(lexer)
            val program = parser.parse()
            parser.reportErrors()
            assertEquals(1, program.statements.size)
            val statement = program.statements[0]
            assertEquals(AstKind.LetStatement, statement!!.kind())
            val letStatement = statement as LetStatement
            assertEquals(expectAstKind, letStatement.value!!.kind())
        }
    }

    @Test
    fun testError() {
        val tests = listOf(
            "let = 1;" to "Expected Identifier, got Assign at start: 4 end: 5 literal: =",
            "let a 1;" to "Expected Assign, got Integer at start: 6 end: 7 literal: 1",
            "let a;" to "Expected Assign, got SemiColon at start: 5 end: 6 literal: ;\nExpected expression, got SemiColon at start: 5 end: 6 literal: ;",
            "let a=;" to "Expected expression, got SemiColon at start: 6 end: 7 literal: ;",
            "let a = 1" to "Expected SemiColon, got Eof at start: 9 end: 9 literal: ",
            "%;" to "Unexpected token Illegal at start: 0 end: 1 literal: %",
        )

        for ((input, expectError) in tests) {
            val lexer = Lexer(input)
            val parser = Parser(lexer)
            parser.parse()
            val execution = assertThrows<IllegalStateException> {
                parser.reportErrors()
            }
            assertEquals(expectError, execution.message)
        }

    }

    @Test
    fun testIfStatement() {
        val tests = listOf(
            "if(true) {1;}",
            "if(false) {} else {}",
            "if(false) {} else { if(true) {} }",
            "if(false) { if(true) {} else {} } else { if(true) {} }",
            "if(true) { return 1; } else { return 2; }",
            "if(true) { if(true) { return 1; } return 1; } else { return 2; }"
        )

        for (test in tests) {
            val parser = Parser(Lexer(test))
            val program = parser.parse()
            parser.reportErrors()
            assertEquals(1, program.statements.size)
            val statement = program.statements[0]
            assertEquals(AstKind.IfStatement, statement!!.kind())
            val ifStatement = statement as IfStatement
            assertEquals(AstKind.BooleanLiteral, ifStatement.condition!!.kind())
            assertEquals(AstKind.BlockStatement, ifStatement.consequence!!.kind())
            println(statement)
        }
    }

    @Test
    fun testReturnStatement() {
        val input = """
            return 5;
            return a;
            return true;
            return add();
        """.trimIndent()
        val lexer = Lexer(input)
        val parser = Parser(lexer)
        val program = parser.parse()
        parser.reportErrors()
        for (statement in program.statements) {
            assert(statement!!.kind() == AstKind.ReturnStatement)
        }
    }

    @Test
    fun testExpressionStatement() {
        val input = """
            1+1;
            2+2;
            hello;
            "abc";
            add(1,2);
        """.trimIndent()
        val lexer = Lexer(input)
        val parser = Parser(lexer)
        val program = parser.parse()
        parser.reportErrors()
        assertEquals(5, program.statements.size)
        for (statement in program.statements) {
            assert(statement!!.kind() == AstKind.ExpressionStatement)
        }
    }

    @Test
    fun testLiteral() {
        val input = """
            5;
            1.5;
            true;
            false;
            "hello";
        """.trimIndent()
        val lexer = Lexer(input)
        val parser = Parser(lexer)
        val program = parser.parse()
        parser.reportErrors()
        assertEquals(5, program.statements.size)
        val expects = listOf(
            AstKind.IntegerLiteral to 5,
            AstKind.FloatLiteral to 1.5f,
            AstKind.BooleanLiteral to true,
            AstKind.BooleanLiteral to false,
            AstKind.StringLiteral to "hello",
        )
        for ((index, statement) in program.statements.withIndex()) {
            val expect = expects[index]
            assertEquals(AstKind.ExpressionStatement, statement!!.kind())
            val expressionStatement = statement as ExpressionStatement
            assertEquals(expect.first, expressionStatement.expression!!.kind())
            val literal = statement.expression as Literal<*>
            assertEquals(expect.second, literal.literalValue())
        }
    }

    @Test
    fun testBinaryExpression() {
        val input = """
            5 + 5;
            5 - 5;
            5 * 5;
            5 / 5;
            5 > 5;
            5 < 5;
            5 == 5;
            5 != 5;
            true == true;
            true != false;
            false == false;
            false != true;
            a + b;
            a - b;
            a * b;
            a / b;
            a > b;
            a < b;
            a == b;
            a != b;
            a >= b;
            a <= b;
            1+1==2+2;
        """.trimIndent()

        val lexer = Lexer(input)
        val parser = Parser(lexer)
        val program = parser.parse()
        parser.reportErrors()
        for (statement in program.statements) {
            assertEquals(AstKind.ExpressionStatement, statement!!.kind())
            val expressionStatement = statement as ExpressionStatement
            assertEquals(AstKind.BinaryExpression, expressionStatement.expression!!.kind())
        }
    }

    @Test
    fun testPrecedence() {
        val inputs = listOf(
            "1 / 2 + 1 * 2;" to "((1 / 2) + (1 * 2))",
            "1 + 2 * 3;" to "(1 + (2 * 3))",
            "(1 + 2) * 3 + 1 + !1;" to "((((1 + 2) * 3) + 1) + (!1))",
            "1 + !!!true;" to "(1 + (!(!(!true))))",
            "add(1,2);" to "add(1, 2)",
            "add(1+2, 3+4);" to "add((1 + 2), (3 + 4))",
            "add(1+2, 3+4) + 5;" to "(add((1 + 2), (3 + 4)) + 5)",
            "1+add(1+2, 3+4);" to "(1 + add((1 + 2), (3 + 4)))",
            "add(1+2, add(2+3));" to "add((1 + 2), add((2 + 3)))",
            "1 > add(1);" to "(1 > add(1))",
            "test(\"hello\");" to "test(\"hello\")",
            "fn(a,b){a+b;}(1,2);" to "fn(a, b) { (a + b); }(1, 2)",
        )
        for ((input, expect) in inputs) {
            val lexer = Lexer(input)
            val parser = Parser(lexer)
            val program = parser.parse()
            parser.reportErrors()
            for (statement in program.statements) {
                assertEquals(AstKind.ExpressionStatement, statement!!.kind())
                val expressionStatement = statement as ExpressionStatement
                assertEquals(expect, expressionStatement.expression.toString())
            }
        }
    }
}
