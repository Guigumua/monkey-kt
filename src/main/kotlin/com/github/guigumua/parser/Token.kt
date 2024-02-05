package com.github.guigumua.parser

enum class TokenKind {
    // Literals
    Integer,
    Float,
    String,

    // Operators
    Plus,
    Minus,
    Asterisk,
    Slash,
    Assign,
    Ampersand,
    Pipe,
    DoubleAsterisk,
    GreaterThan,
    LessThan,
    Bang,
    GreaterThanEquals,
    LessThanEquals,
    Equals,
    NotEquals,

    And,
    Or,

    // Keywords
    Let,
    If,
    Else,
    True,
    False,
    While,
    Function,
    Return,

    // Separators
    OpenParen,
    CloseParen,
    OpenBrace,
    CloseBrace,
    OpenBracket,
    CloseBracket,
    Comma,
    Colon,
    SemiColon,
    Arrow,

    // Other
    Illegal,
    Identifier,
    Eof,
}

class TextSpan(val start: Int, val end: Int, val literal: String) {
    fun length() = end - start
    override fun toString(): String {
        return "TextSpan(start=$start, end=$end, literal='$literal')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextSpan

        if (start != other.start) return false
        if (end != other.end) return false
        if (literal != other.literal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + end
        result = 31 * result + literal.hashCode()
        return result
    }


}

class Token(val kind: TokenKind, val textSpan: TextSpan) {
    override fun toString(): String {
        return "Token(kind=$kind, textSpan=$textSpan)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (kind != other.kind) return false
        if (textSpan != other.textSpan) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind.hashCode()
        result = 31 * result + textSpan.hashCode()
        return result
    }

}
