package com.github.guigumua.parser

import java.io.PrintStream

enum class Precedence {
    Lowest,
    Assign, // =
    Equals, // == or !=
    LessGreater, // > or < or >= or <=
    Sum, // + -
    Product, // * or /
    Prefix, // -X or !X
    Call, // myFunction(X)
    ArrayIndex, // myArray[X]
}

class Parser(private val lexer: Lexer) {
    private val errors = mutableListOf<String>()
    private var token: Token = lexer.nextToken()
    private fun nextToken(): Token {
        token = lexer.nextToken()
        return token
    }

    fun reportErrors() {
        if (errors.isNotEmpty()) {
            error(errors.joinToString("\n"))
        }
    }

    fun printError(out: PrintStream) {
        out.println(errors.joinToString("\n"))
    }

    fun hasError(): Boolean {
        return errors.isNotEmpty()
    }

    fun parse(): Program {
        val statements = mutableListOf<Statement?>()
        while (token.kind != TokenKind.Eof) {
            statements.add(parseStatement())
        }
        return Program(statements)
    }

    private fun parseStatement(): Statement {
        return when (token.kind) {
            TokenKind.Let -> parseLetStatement()
            TokenKind.Return -> parseReturnStatement()
            TokenKind.If -> parseIfStatement()
            TokenKind.OpenBrace -> {
                nextToken()
                parseBlockStatement()
            }
            TokenKind.Function -> parseFunctionDefStatement()
            else -> parseExpressionStatement()
        }
    }

    private fun parseFunctionDefStatement(): Statement {
        nextToken()
        val name = parseIdentifier()
        expectTokenAndConsume(TokenKind.OpenParen)
        val parameters = parseFunctionParameters()
        expectTokenAndConsume(TokenKind.OpenBrace)
        val body = parseBlockStatement()
        return FunctionDefStatement(name, parameters, body)

    }

    private fun parseIfStatement(): Statement {
        nextToken()
        expectTokenAndConsume(TokenKind.OpenParen)
        val condition = parseExpression(Precedence.Lowest)
        expectTokenAndConsume(TokenKind.CloseParen)
        expectTokenAndConsume(TokenKind.OpenBrace)
        val consequence = parseBlockStatement()
        if (token.kind == TokenKind.Else) {
            nextToken()
            expectTokenAndConsume(TokenKind.OpenBrace)
            val alternative = parseBlockStatement()
            return IfStatement(condition, consequence, alternative)
        }
        return IfStatement(condition, consequence, null)
    }

    private fun expectTokenAndConsume(expected: TokenKind) {
        if (token.kind != expected) {
            errors.add("Expected $expected, got ${token.kind} at start: ${token.textSpan.start} end: ${token.textSpan.end} literal: ${token.textSpan.literal}")
        } else {
            nextToken()
        }
    }

    private fun parseLetStatement(): LetStatement {
        nextToken()
        val name = parseIdentifier()
        expectTokenAndConsume(TokenKind.Assign)
        val value = parseExpression(Precedence.Lowest)
        if (value == null) {
            errors.add("Expected expression, got ${token.kind} at start: ${token.textSpan.start} end: ${token.textSpan.end} literal: ${token.textSpan.literal}")
            return LetStatement(name, null)
        }
        expectTokenAndConsume(TokenKind.SemiColon)
        return LetStatement(name, value)
    }

    private fun getPrecedence(kind: TokenKind): Precedence {
        return when (kind) {
            TokenKind.Equals -> Precedence.Equals
            TokenKind.Assign -> Precedence.Assign
            TokenKind.NotEquals -> Precedence.Equals
            TokenKind.LessThan -> Precedence.LessGreater
            TokenKind.LessThanEquals -> Precedence.LessGreater
            TokenKind.GreaterThan -> Precedence.LessGreater
            TokenKind.GreaterThanEquals -> Precedence.LessGreater
            TokenKind.Plus -> Precedence.Sum
            TokenKind.Minus -> Precedence.Sum
            TokenKind.Asterisk -> Precedence.Product
            TokenKind.Slash -> Precedence.Product
            TokenKind.OpenParen -> Precedence.Call
            TokenKind.OpenBracket -> Precedence.ArrayIndex
            else -> Precedence.Lowest
        }
    }

    private fun parseExpressionStatement(): ExpressionStatement {
        val expression = parseExpression(Precedence.Lowest)
        expectTokenAndConsume(TokenKind.SemiColon)
        return ExpressionStatement(expression)
    }

    private fun parseExpression(precedence: Precedence): Expression? {
        if (token.kind == TokenKind.Eof) {
            errors.add("Unexpected end of file")
            return null
        }
        val token = this.token
        var left = when (token.kind) {
            TokenKind.Integer -> {
                nextToken()
                IntegerLiteral(token.textSpan.literal.toInt())
            }

            TokenKind.Float -> {
                nextToken()
                FloatLiteral(token.textSpan.literal.toFloat())
            }

            TokenKind.True,
            TokenKind.False -> {
                nextToken()
                BooleanLiteral(token.kind == TokenKind.True)
            }

            TokenKind.String -> {
                nextToken()
                StringLiteral(token.textSpan.literal)
            }

            TokenKind.Identifier -> {
                nextToken()
                val identifier = Identifier(token.textSpan.literal)
                if (this.token.kind == TokenKind.Assign) {
                    nextToken()
                    val expression = parseExpression(Precedence.Lowest)
                    AssignmentExpression(identifier, expression)
                } else {
                    identifier
                }
            }

            TokenKind.Bang,
            TokenKind.Plus,
            TokenKind.Minus -> parseUnaryExpression(token)

            TokenKind.OpenParen -> parseGroupedExpression()
            TokenKind.Function -> parseFunctionExpression()
            TokenKind.OpenBracket -> parseArrayLiteral()
            TokenKind.SemiColon -> return null
            else -> {
                errors.add("Unexpected token ${token.kind} at start: ${token.textSpan.start} end: ${token.textSpan.end} literal: ${token.textSpan.literal}")
                nextToken()
                return null
            }
        }
        var nextToken = this.token
        if (nextToken.kind == TokenKind.Eof || nextToken.kind == TokenKind.SemiColon) {
            return left
        }
        while (nextToken.kind != TokenKind.Eof
            && nextToken.kind != TokenKind.SemiColon
            && precedence < getPrecedence(nextToken.kind)
        ) {
            left = when (nextToken.kind) {
                TokenKind.Plus,
                TokenKind.Minus,
                TokenKind.Asterisk,
                TokenKind.Slash,
                TokenKind.Equals,
                TokenKind.NotEquals,
                TokenKind.LessThan,
                TokenKind.LessThanEquals,
                TokenKind.GreaterThan,
                TokenKind.GreaterThanEquals,
                TokenKind.And,
                TokenKind.Or,
                -> parseBinaryExpression(left, nextToken)

                TokenKind.OpenParen -> parseFunctionCallExpression(left)
                TokenKind.OpenBracket -> parseArrayIndexExpression(left)
                else -> {
                    return left
                }
            }
            nextToken = this.token
        }
        return left
    }

    private fun parseArrayIndexExpression(left: Expression?): Expression {
        nextToken()
        val index = parseExpression(Precedence.Lowest)
        expectTokenAndConsume(TokenKind.CloseBracket)
        return ArrayIndexExpression(left, index)
    }

    private fun parseArrayLiteral(): Expression? {
        nextToken()
        val elements = parseExpressionList(TokenKind.CloseBracket)
        return ArrayLiteral(elements)
    }

    private fun parseExpressionList(end: TokenKind): List<Expression?> {
        if (token.kind == end) {
            nextToken()
            return emptyList()
        }
        val elements = mutableListOf<Expression?>()
        elements.add(parseExpression(Precedence.Lowest))
        while (token.kind == TokenKind.Comma) {
            nextToken()
            elements.add(parseExpression(Precedence.Lowest))
        }
        expectTokenAndConsume(end)
        return elements
    }


    private fun parseFunctionCallExpression(function: Expression?): Expression {
        nextToken()
        val functionCallArguments = parseExpressionList(TokenKind.CloseParen)
        return FunctionCallExpression(function, functionCallArguments)
    }

    private fun parseBinaryExpression(left: Expression?, op: Token): BinaryExpression {
        nextToken()
        val right = parseExpression(getPrecedence(op.kind))
        return BinaryExpression(left, op.textSpan.literal, right)
    }

    private fun parseUnaryExpression(op: Token): UnaryExpression {
        nextToken()
        val right = parseExpression(Precedence.Prefix)
        return UnaryExpression(op.textSpan.literal, right)
    }

    private fun parseGroupedExpression(): Expression? {
        nextToken()
        val expression = parseExpression(Precedence.Lowest)
        expectTokenAndConsume(TokenKind.CloseParen)
        return expression
    }

    private fun parseFunctionExpression(): FunctionExpression {
        nextToken()
        expectTokenAndConsume(TokenKind.OpenParen)
        val parameters = parseFunctionParameters()
        expectTokenAndConsume(TokenKind.OpenBrace)
        val body = parseBlockStatement()
        return FunctionExpression(parameters, body)
    }

    private fun parseFunctionParameters(): List<Identifier?> {
        if (token.kind == TokenKind.CloseParen) {
            nextToken()
            return emptyList()
        }
        val parameters = mutableListOf<Identifier?>()
        parameters.add(parseIdentifier())
        while (token.kind == TokenKind.Comma) {
            nextToken()
            parameters.add(parseIdentifier())
        }
        expectTokenAndConsume(TokenKind.CloseParen)
        return parameters
    }

    private fun parseBlockStatement(): BlockStatement {
        val statements = mutableListOf<Statement?>()
        while (token.kind != TokenKind.Eof && token.kind != TokenKind.CloseBrace) {
            statements.add(parseStatement())
        }
        expectTokenAndConsume(TokenKind.CloseBrace)
        return BlockStatement(statements)
    }

    private fun parseIdentifier(): Identifier? {
        val token = this.token
        if (token.kind == TokenKind.Eof) {
            errors.add("Unexpected end of file")
            return null
        }
        if (token.kind != TokenKind.Identifier) {
            errors.add("Expected Identifier, got ${token.kind} at start: ${token.textSpan.start} end: ${token.textSpan.end} literal: ${token.textSpan.literal}")
            return null
        }
        nextToken()
        return Identifier(token.textSpan.literal)
    }

    private fun parseReturnStatement(): ReturnStatement {
        nextToken()
        val value = parseExpression(Precedence.Lowest)
        expectTokenAndConsume(TokenKind.SemiColon)
        return ReturnStatement(value)
    }
}

