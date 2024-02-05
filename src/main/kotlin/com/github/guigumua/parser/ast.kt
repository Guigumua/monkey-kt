package com.github.guigumua.parser

interface Ast {
    fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R

    fun kind(): AstKind
}

enum class AstKind {
    Program,
    Identifier,

    FunctionExpression,
    BinaryExpression,
    UnaryExpression,
    ArrayIndexExpression,
    FunctionCallExpression,
    AssignmentExpression,

    FunctionDefStatement,
    ReturnStatement,
    IfStatement,
    WhileStatement,
    BlockStatement,
    LetStatement,
    ExpressionStatement,

    IntegerLiteral,
    FloatLiteral,
    StringLiteral,
    BooleanLiteral,
    ArrayLiteral,

}

interface AstVisitor<D, R> {

    fun visit(ast: Ast, data: D): R
    fun visitProgram(program: Program, data: D): R

    fun visitIdentifier(identifier: Identifier, data: D): R

    fun visitFunctionExpression(functionExpression: FunctionExpression, data: D): R

    fun visitBinaryExpression(binaryExpression: BinaryExpression, data: D): R

    fun visitUnaryExpression(unaryExpression: UnaryExpression, data: D): R

    fun visitFunctionCallExpression(functionCallExpression: FunctionCallExpression, data: D): R

    fun visitFunctionDefStatement(functionDefStatement: FunctionDefStatement, data: D): R

    fun visitReturnStatement(returnStatement: ReturnStatement, data: D): R

    fun visitIfStatement(ifStatement: IfStatement, data: D): R

    fun visitWhileStatement(whileStatement: WhileStatement, data: D): R

    fun visitBlockStatement(blockStatement: BlockStatement, data: D): R

    fun visitExpressionStatement(expressionStatement: ExpressionStatement, data: D): R
    override fun toString(): String
    fun visitLetStatement(letStatement: LetStatement, data: D): R
    fun visitIntegerLiteral(integerLiteral: IntegerLiteral, data: D): R
    fun visitFloatLiteral(floatLiteral: FloatLiteral, data: D): R
    fun visitStringLiteral(stringLiteral: StringLiteral, data: D): R
    fun visitBooleanLiteral(booleanLiteral: BooleanLiteral, data: D): R
    fun visitAssignmentExpression(assignmentExpression: AssignmentExpression, data: D): R
    fun visitArrayLiteral(arrayLiteral: ArrayLiteral, data: D): R
    fun visitArrayIndexExpression(arrayIndexExpression: ArrayIndexExpression, data: D): R
}

interface Statement : Ast

interface Expression : Ast {
    fun value()
}

class Program(val statements: List<Statement?>) : Ast {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitProgram(this, data)
    }

    override fun kind() = AstKind.Program

    override fun toString(): String {
        return statements.joinToString(" ")
    }
}

class Identifier(val value: String) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitIdentifier(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.Identifier
    }

    override fun toString(): String {
        return value
    }
}

class ExpressionStatement(val expression: Expression?) : Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitExpressionStatement(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.ExpressionStatement
    }

    override fun toString(): String {
        return "$expression;"
    }
}

class LetStatement(val name: Identifier?, val value: Expression?) : Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitLetStatement(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.LetStatement
    }

    override fun toString(): String {
        return "let $name = $value;"
    }
}

class BlockStatement(val statements: List<Statement?>) : Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitBlockStatement(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.BlockStatement
    }

    override fun toString(): String {
        return "{ ${statements.joinToString(" ")} }"
    }
}

class FunctionDefStatement(val name: Identifier?, val parameters: List<Identifier?>, val body: BlockStatement) :
    Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitFunctionDefStatement(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.FunctionDefStatement
    }

    override fun toString(): String {
        return "fn $name(${parameters.joinToString(", ")}) $body"
    }
}

class FunctionExpression(val parameters: List<Identifier?>, val body: BlockStatement) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitFunctionExpression(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.FunctionExpression
    }

    override fun toString(): String {
        return "fn(${parameters.joinToString(", ")}) $body"
    }
}

class BinaryExpression(val left: Expression?, val op: String, val right: Expression?) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitBinaryExpression(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.BinaryExpression
    }

    override fun toString(): String {
        return "($left $op $right)"
    }
}

class UnaryExpression(val op: String, val right: Expression?) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitUnaryExpression(this, data)
    }

    override fun toString(): String {
        return "(${op}$right)"
    }

    override fun kind(): AstKind {
        return AstKind.UnaryExpression
    }
}

class ReturnStatement(val value: Expression?) : Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitReturnStatement(this, data)
    }

    override fun toString(): String {
        return "return $value;"
    }

    override fun kind(): AstKind {
        return AstKind.ReturnStatement
    }
}

class IfStatement(val condition: Expression?, val consequence: BlockStatement?, val alternative: BlockStatement?) :
    Statement {

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitIfStatement(this, data)
    }

    override fun toString(): String {
        return "if ($condition) $consequence ${if (alternative != null) "else $alternative" else ""}"
    }

    override fun kind(): AstKind {
        return AstKind.IfStatement
    }
}

class WhileStatement(private val condition: Expression, private val body: BlockStatement) : Statement {
    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitWhileStatement(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.WhileStatement
    }

    override fun toString(): String {
        return "while ($condition) $body"
    }
}

interface Literal<T> : Expression {
    fun literalValue(): T
}

class IntegerLiteral(private val value: Int) : Literal<Int> {
    override fun literalValue(): Int = value

    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitIntegerLiteral(this, data)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun kind(): AstKind {
        return AstKind.IntegerLiteral
    }
}

class FloatLiteral(private val value: Float) : Literal<Float> {
    override fun value() {
        TODO()
    }

    override fun literalValue(): Float = value

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitFloatLiteral(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.FloatLiteral
    }

    override fun toString(): String {
        return value.toString()
    }
}

class StringLiteral(private val value: String) : Literal<String> {
    override fun value() {
        TODO()
    }

    override fun literalValue(): String {
        return value.removeSurrounding("\"")
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitStringLiteral(this, data)
    }

    override fun toString(): String {
        return value
    }

    override fun kind(): AstKind {
        return AstKind.StringLiteral
    }
}


class BooleanLiteral(private val value: Boolean) : Literal<Boolean> {
    override fun value() {
        TODO()
    }

    override fun literalValue(): Boolean = value

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitBooleanLiteral(this, data)
    }

    override fun toString(): String {
        return value.toString()
    }

    override fun kind(): AstKind {
        return AstKind.BooleanLiteral
    }
}

class AssignmentExpression(val name: Identifier?, val value: Expression?) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitAssignmentExpression(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.AssignmentExpression
    }

    override fun toString(): String {
        return "$name = $value"
    }
}

class FunctionCallExpression(val function: Expression?, val arguments: List<Expression?>) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitFunctionCallExpression(this, data)
    }

    override fun toString(): String {
        return "$function(${arguments.joinToString(", ")})"
    }

    override fun kind(): AstKind {
        return AstKind.FunctionCallExpression
    }
}

class ArrayLiteral(val elements: List<Expression?>) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitArrayLiteral(this, data)
    }


    override fun kind(): AstKind {
        return AstKind.ArrayLiteral
    }

    override fun toString(): String {
        return "[${elements.joinToString(", ")}]"
    }
}

class ArrayIndexExpression(val array: Expression?, val index: Expression?) : Expression {
    override fun value() {
        TODO()
    }

    override fun <D, R> visit(visitor: AstVisitor<D, R>, data: D): R {
        return visitor.visitArrayIndexExpression(this, data)
    }

    override fun kind(): AstKind {
        return AstKind.ArrayIndexExpression
    }

    override fun toString(): String {
        return "$array[$index]"
    }
}
