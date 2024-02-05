package com.github.guigumua.parser

class Lexer(private val input: String) {
    private var position = 0
    fun nextToken(): Token {
        if (position >= input.length) {
            return Token(TokenKind.Eof, TextSpan(position, position, ""))
        }
        skipWhitespace()
        val start = position
        when (val currentChar = input[start]) {
            in '0'..'9' -> {
                return readNumber(start)
            }

            in 'a'..'z', in 'A'..'Z', '$', '_' -> {
                return readIdentifier(start)
            }

            '+', '-', '*', '/', '=', '>', '<', '!', '&', '|', '(', ')', '{', '}', '[', ']', ',', ':', ';' -> {
                return readPunctuation(start, currentChar)
            }

            '"' -> return readString(start)

            else -> {
                position++
                return Token(TokenKind.Illegal, TextSpan(start, position, input.substring(start, position)))
            }
        }
    }

    private fun readPunctuation(start: Int, currentChar: Char): Token {
        position++
        return when (currentChar) {
            '+' -> Token(TokenKind.Plus, TextSpan(start, position, "+"))
            '-' -> oneOrTwo(start, '>', TokenKind.Minus, TokenKind.Arrow)
            '*' -> oneOrTwo(start, '*', TokenKind.Asterisk, TokenKind.DoubleAsterisk)
            '/' -> Token(TokenKind.Slash, TextSpan(start, position, "/"))
            '=' -> oneOrTwo(start, '=', TokenKind.Assign, TokenKind.Equals)
            '>' -> oneOrTwo(start, '=', TokenKind.GreaterThan, TokenKind.GreaterThanEquals)
            '<' -> oneOrTwo(start, '=', TokenKind.LessThan, TokenKind.LessThanEquals)
            '!' -> oneOrTwo(start, '=', TokenKind.Bang, TokenKind.NotEquals)
            '&' -> oneOrTwo(start, '&', TokenKind.Ampersand, TokenKind.And)
            '|' -> oneOrTwo(start, '|', TokenKind.Pipe, TokenKind.Or)
            '(' -> Token(TokenKind.OpenParen, TextSpan(start, position, "("))
            ')' -> Token(TokenKind.CloseParen, TextSpan(start, position, ")"))
            '{' -> Token(TokenKind.OpenBrace, TextSpan(start, position, "{"))
            '}' -> Token(TokenKind.CloseBrace, TextSpan(start, position, "}"))
            '[' -> Token(TokenKind.OpenBracket, TextSpan(start, position, "["))
            ']' -> Token(TokenKind.CloseBracket, TextSpan(start, position, "]"))
            ',' -> Token(TokenKind.Comma, TextSpan(start, position, ","))
            ':' -> Token(TokenKind.Colon, TextSpan(start, position, ":"))
            ';' -> Token(TokenKind.SemiColon, TextSpan(start, position, ";"))
            else -> Token(TokenKind.Illegal, TextSpan(start, position, input.substring(position - 1, position)))
        }
    }

    private fun oneOrTwo(start: Int, two: Char, oneKind: TokenKind, twoKind: TokenKind): Token {
        return if (position < input.length && input[position] == two) {
            position++
            Token(twoKind, TextSpan(start, position, input.substring(start, position)))
        } else {
            Token(oneKind, TextSpan(start, position, input.substring(start, position)))
        }
    }

    private fun skipWhitespace() {
        if (position >= input.length) return
        while (position < input.length) {
            val currentChar = input[position]
            if (currentChar.isWhitespace()) {
                position++
            } else {
                break
            }
        }
    }

    private fun readString(start: Int): Token {
        position++
        while (position < input.length) {
            val currentChar = input[position]
            if (currentChar == '"') {
                position++
                break
            } else if (currentChar == '\\') {
                position++
                if (position < input.length && input[position] == '"') {
                    position++
                } else {
                    break
                }
            } else {
                position++
            }
        }
        val end = position
        val literal = input.substring(start, end)
        return Token(TokenKind.String, TextSpan(start, end, literal))
    }

    private fun readIdentifier(start: Int): Token {
        position++
        while (position < input.length) {
            val currentChar = input[position]
            if (currentChar in 'a'..'z' || currentChar in 'A'..'Z' || currentChar in '0'..'9' || currentChar == '_' || currentChar == '$') {
                position++
            } else {
                break
            }
        }
        val end = position
        val literal = input.substring(start, end)
        val kind = getKind(literal)
        return Token(kind, TextSpan(start, end, literal))
    }

    private fun readNumber(start: Int): Token {
        position++
        var isFloat = false
        while (position < input.length) {
            val ch = input[position]
            if (ch in '0'..'9') {
                position++
            } else if (ch == '.') {
                if (isFloat) {
                    return Token(TokenKind.Illegal, TextSpan(start, position, input.substring(start, position)))
                }
                isFloat = true
                position++
            } else {
                break
            }
        }
        if (isFloat) {
            return Token(TokenKind.Float, TextSpan(start, position, input.substring(start, position)))
        }
        return Token(TokenKind.Integer, TextSpan(start, position, input.substring(start, position)))
    }

    private fun getKind(literal: String): TokenKind {
        return when (literal) {
            "let" -> TokenKind.Let
            "if" -> TokenKind.If
            "else" -> TokenKind.Else
            "true" -> TokenKind.True
            "false" -> TokenKind.False
            "while" -> TokenKind.While
            "fn" -> TokenKind.Function
            "return" -> TokenKind.Return
            else -> TokenKind.Identifier
        }
    }
}
