package com.github.guigumua.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestEvaluator {
    @Test
    fun testEvaluator() {
        val tests = listOf(
            "1;" to IntegerValue(1),
            "1.1;" to FloatValue(1.1f),
            "true;" to BooleanValue.True,
            "false;" to BooleanValue.False,
            "\"hello\";" to StringValue("hello"),
            "1 + 2;" to IntegerValue(3),
            "1.5 + 2.5;" to FloatValue(4.0f),
            "1 - 2;" to IntegerValue(-1),
            "-1;" to IntegerValue(-1),
            "+1;" to IntegerValue(1),
            "-1.5;" to FloatValue(-1.5f),
            "+1.5;" to FloatValue(1.5f),
            "1.5 - 2.5;" to FloatValue(-1.0f),
            "\"hello\" + \" world\";" to StringValue("hello world"),
            "1 * 2;" to IntegerValue(2),
            "1.5 * 2.5;" to FloatValue(3.75f),
            "1 / 2;" to IntegerValue(0),
            "1.0 / 2.0;" to FloatValue(0.5f),
            "1 > 2;" to BooleanValue.False,
            "1 < 2;" to BooleanValue.True,
            "1 == 2;" to BooleanValue.False,
            "1 != 2;" to BooleanValue.True,
            "1 >= 2;" to BooleanValue.False,
            "1 <= 2;" to BooleanValue.True,
            "1.0 > 2.0;" to BooleanValue.False,
            "1.0 < 2.0;" to BooleanValue.True,
            "1.0 == 2.0;" to BooleanValue.False,
            "1.0 != 2.0;" to BooleanValue.True,
            "1.0 >= 2.0;" to BooleanValue.False,
            "1.0 <= 2.0;" to BooleanValue.True,
            "true == true;" to BooleanValue.True,
            "true == false;" to BooleanValue.False,
            "true != true;" to BooleanValue.False,
            "true != false;" to BooleanValue.True,
            "false == false;" to BooleanValue.True,
            "false == true;" to BooleanValue.False,
            "false != false;" to BooleanValue.False,
            "false != true;" to BooleanValue.True,
            "1 + 1 > 2;" to BooleanValue.False,
            "1 + 1 < 2;" to BooleanValue.False,
            "1 + 1 == 2;" to BooleanValue.True,
            "\"hello\" == \"hello\";" to BooleanValue.True,
            "\"hello\" != \"hello\";" to BooleanValue.False,
            "\"hello\" == \"world\";" to BooleanValue.False,
            "\"hello\" != \"world\";" to BooleanValue.True,
            "1 + 1 == 2 + 2;" to BooleanValue.False,
            "if(true) {1;} else {2;}" to IntegerValue(1),
            "if(false) {1;} else {2;}" to IntegerValue(2),
            "1;return 2;3;" to IntegerValue(2),
            "if(true) { if(true) { return 1; } return 2; } else { return 3; }" to IntegerValue(1),

        )
        for ((input, expectValue) in tests) {
            val lexer = Lexer(input)
            val parser = Parser(lexer)
            val program = parser.parse()
            parser.reportErrors()
            val value = AstEvaluator().visit(program, Environment())
            assertEquals(expectValue, value)
        }
    }
}
