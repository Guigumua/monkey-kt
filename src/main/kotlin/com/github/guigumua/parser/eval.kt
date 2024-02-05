package com.github.guigumua.parser

interface Value<T> {
    fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        return ErrorValue("Unknown operator $op")
    }

    fun calculate(op: String): Value<Any?> {
        return ErrorValue("Unknown operator $op")
    }
}

object NullValue : Value<Any?> {
    override fun toString(): String {
        return "null"
    }
}

class IntegerValue(val value: Int) : Value<Any?> {

    override fun toString(): String {
        return value.toString()
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        if (value !is IntegerValue) {
            return ErrorValue("Unknown operator $op for IntegerValue and ${value::class.simpleName}")
        }
        return when (op) {
            "+" -> IntegerValue(this.value + value.value)
            "-" -> IntegerValue(this.value - value.value)
            "*" -> IntegerValue(this.value * value.value)
            "/" -> IntegerValue(this.value / value.value)
            ">" -> if (this.value > value.value) BooleanValue.True else BooleanValue.False
            "<" -> if (this.value < value.value) BooleanValue.True else BooleanValue.False
            "==" -> if (this.value == value.value) BooleanValue.True else BooleanValue.False
            "!=" -> if (this.value != value.value) BooleanValue.True else BooleanValue.False
            ">=" -> if (this.value >= value.value) BooleanValue.True else BooleanValue.False
            "<=" -> if (this.value <= value.value) BooleanValue.True else BooleanValue.False
            else -> ErrorValue("Unknown operator $op for IntegerValue")
        }
    }

    override fun calculate(op: String): Value<Any?> {
        return when (op) {
            "-" -> IntegerValue(-this.value)
            "+" -> this
            else -> ErrorValue("Unknown operator $op for IntegerValue")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntegerValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value
    }

}

class FloatValue(val value: Float) : Value<Any?> {

    override fun toString(): String {
        return value.toString()
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        if (value !is FloatValue) {
            return ErrorValue("Unknown operator $op for FloatValue and ${value::class.simpleName}")
        }
        return when (op) {
            "+" -> FloatValue(this.value + value.value)
            "-" -> FloatValue(this.value - value.value)
            "*" -> FloatValue(this.value * value.value)
            "/" -> FloatValue(this.value / value.value)
            ">" -> if (this.value > value.value) BooleanValue.True else BooleanValue.False
            "<" -> if (this.value < value.value) BooleanValue.True else BooleanValue.False
            "==" -> if (this.value == value.value) BooleanValue.True else BooleanValue.False
            "!=" -> if (this.value != value.value) BooleanValue.True else BooleanValue.False
            ">=" -> if (this.value >= value.value) BooleanValue.True else BooleanValue.False
            "<=" -> if (this.value <= value.value) BooleanValue.True else BooleanValue.False
            else -> ErrorValue("Unknown operator $op for FloatValue")
        }
    }

    override fun calculate(op: String): Value<Any?> {
        return when (op) {
            "-" -> FloatValue(-this.value)
            "+" -> this
            else -> ErrorValue("Unknown operator $op for FloatValue")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class StringValue(val value: String) : Value<Any?> {

    override fun toString(): String {
        return value
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        if (value !is StringValue) {
            return ErrorValue("Unknown operator $op for StringValue and ${value::class.simpleName}")
        }
        return when (op) {
            "+" -> StringValue(this.value + value.value)
            ">" -> if (this.value > value.value) BooleanValue.True else BooleanValue.False
            "<" -> if (this.value < value.value) BooleanValue.True else BooleanValue.False
            "==" -> if (this.value == value.value) BooleanValue.True else BooleanValue.False
            "!=" -> if (this.value != value.value) BooleanValue.True else BooleanValue.False
            ">=" -> if (this.value >= value.value) BooleanValue.True else BooleanValue.False
            "<=" -> if (this.value <= value.value) BooleanValue.True else BooleanValue.False
            else -> ErrorValue("Unknown operator $op for StringValue")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

enum class BooleanValue(val value: Boolean) : Value<Any?> {
    True(true),
    False(false),
    ;

    override fun toString(): String {
        return value.toString()
    }

    fun not(): BooleanValue {
        return if (this == True) False else True
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        if (value !is BooleanValue) {
            return ErrorValue("Unknown operator $op for BooleanValue and ${value::class.simpleName}")
        }
        return when (op) {
            "&&" -> if (this == True && value == True) True else False
            "||" -> if (this == True || value == True) True else False
            "==" -> if (this.value == value.value) True else False
            "!=" -> if (this.value != value.value) True else False
            else -> ErrorValue("Unknown operator $op for BooleanValue")
        }
    }

    override fun calculate(op: String): Value<Any?> {
        return when (op) {
            "!" -> not()
            else -> NullValue
        }
    }
}

class ArrayValue(val elements: List<Value<Any?>>) : Value<Any?> {
    override fun toString(): String {
        return elements.joinToString(", ", "[", "]")
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        if (value !is ArrayValue) {
            return ErrorValue("Unknown operator $op for ArrayValue and ${value::class.simpleName}")
        }
        return when (op) {
            "==" -> if (this.elements == value.elements) BooleanValue.True else BooleanValue.False
            "!=" -> if (this.elements != value.elements) BooleanValue.True else BooleanValue.False
            else -> ErrorValue("Unknown operator $op for ArrayValue")
        }
    }

    override fun calculate(op: String): Value<Any?> {
        return when (op) {
            "!" -> if (elements.isEmpty()) BooleanValue.True else BooleanValue.False
            else -> ErrorValue("Unknown operator $op for ArrayValue")
        }
    }
}

class ReturnValue(val value: Value<Any?>) : Value<Any?> {
    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReturnValue

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}

interface FunctionValue : Value<Any?> {
    fun call(arguments: List<Value<Any?>>, astEvaluator: AstEvaluator, environment: Environment): Value<Any?>
}

class UserFunctionValue(val parameters: List<Identifier>, val body: BlockStatement) :
    FunctionValue {
    override fun toString(): String {
        return "fn(${parameters.joinToString(", ")}) $body"
    }


    override fun call(arguments: List<Value<Any?>>, astEvaluator: AstEvaluator, environment: Environment): Value<Any?> {
        if (arguments.size != parameters.size) {
            return ErrorValue("Wrong number of arguments: want=${parameters.size}, got=${arguments.size}")
        }
        for ((index, parameter) in parameters.withIndex()) {
            environment.set(parameter.value, arguments[index])
        }
        val value = body.visit(astEvaluator, environment)
        if (value is ReturnValue) {
            return value.value
        }
        return value
    }
}

class ErrorValue(val message: String) : Value<Any?> {
    override fun toString(): String {
        return "Error: $message"
    }

    override fun calculate(op: String, value: Value<Any?>): Value<Any?> {
        return this
    }

    override fun calculate(op: String): Value<Any?> {
        return this
    }
}

class Environment(private val parent: Environment? = null) {
    private val store = mutableMapOf<String, Value<Any?>>()

    fun get(name: String): Value<Any?> {
        return store[name] ?: parent?.get(name) ?: ErrorValue("Unknown identifier $name")
    }

    fun set(name: String, value: Value<Any?>) {
        store[name] = value
    }

    companion object {
        fun createBuiltins(): Environment {
            val environment = Environment()
            environment.set(
                "print",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        arguments.forEach(::print)
                        return NullValue
                    }
                }
            )
            environment.set(
                "println",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        arguments.forEach(::println)
                        return NullValue
                    }
                }
            )
            environment.set(
                "len",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 1) {
                            return ErrorValue("Wrong number of arguments: want=1, got=${arguments.size}")
                        }
                        return when (val argument = arguments[0]) {
                            is StringValue -> IntegerValue(argument.value.length)
                            is ArrayValue -> IntegerValue(argument.elements.size)
                            else -> ErrorValue("Argument must be a string or an array")
                        }
                    }
                }
            )
            environment.set(
                "input",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.isNotEmpty()) {
                            return ErrorValue("Wrong number of arguments: want=1, got=${arguments.size}")
                        }
                        return StringValue(readln())
                    }
                }
            )

            environment.set(
                "toInt",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 1) {
                            return ErrorValue("Wrong number of arguments: want=1, got=${arguments.size}")
                        }
                        val argument = arguments[0]
                        if (argument !is StringValue) {
                            return ErrorValue("Argument must be a string")
                        }
                        return IntegerValue(argument.value.toInt())
                    }
                }
            )
            environment.set(
                "toFloat",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 1) {
                            return ErrorValue("Wrong number of arguments: want=1, got=${arguments.size}")
                        }
                        val argument = arguments[0]
                        if (argument !is StringValue) {
                            return ErrorValue("Argument must be a string")
                        }
                        return FloatValue(argument.value.toFloat())
                    }
                }
            )
            environment.set(
                "push",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 2) {
                            return ErrorValue("Wrong number of arguments: want=2, got=${arguments.size}")
                        }
                        val array = arguments[0]
                        if (array !is ArrayValue) {
                            return ErrorValue("First argument must be an array")
                        }
                        val value = arguments[1]
                        val newElements = array.elements.toMutableList()
                        newElements.add(value)
                        return ArrayValue(newElements)
                    }
                }
            )
            environment.set(
                "pop",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 1) {
                            return ErrorValue("Wrong number of arguments: want=1, got=${arguments.size}")
                        }
                        val array = arguments[0]
                        if (array !is ArrayValue) {
                            return ErrorValue("First argument must be an array")
                        }
                        val newElements = array.elements.toMutableList()
                        if (newElements.isEmpty()) {
                            return ErrorValue("Array is empty")
                        }
                        newElements.removeAt(newElements.size - 1)
                        return ArrayValue(newElements)
                    }
                }
            )
            environment.set(
                "map",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 2) {
                            return ErrorValue("Wrong number of arguments: want=2, got=${arguments.size}")
                        }
                        val array = arguments[0]
                        if (array !is ArrayValue) {
                            return ErrorValue("First argument must be an array")
                        }
                        val function = arguments[1]
                        if (function !is FunctionValue) {
                            return ErrorValue("Second argument must be a function")
                        }
                        val newElements = array.elements.map { function.call(listOf(it), astEvaluator, environment) }
                        return ArrayValue(newElements)
                    }
                }
            )
            environment.set(
                "filter",
                object : FunctionValue {
                    override fun call(
                        arguments: List<Value<Any?>>,
                        astEvaluator: AstEvaluator,
                        environment: Environment
                    ): Value<Any?> {
                        if (arguments.size != 2) {
                            return ErrorValue("Wrong number of arguments: want=2, got=${arguments.size}")
                        }
                        val array = arguments[0]
                        if (array !is ArrayValue) {
                            return ErrorValue("First argument must be an array")
                        }
                        val function = arguments[1]
                        if (function !is FunctionValue) {
                            return ErrorValue("Second argument must be a function")
                        }
                        val newElements = array.elements.filter {
                            function.call(
                                listOf(it),
                                astEvaluator,
                                environment
                            ) == BooleanValue.True
                        }
                        return ArrayValue(newElements)
                    }
                }
            )
            environment.set("reduce", object : FunctionValue {
                override fun call(arguments: List<Value<Any?>>, astEvaluator: AstEvaluator, environment: Environment): Value<Any?> {
                    if (arguments.size != 3) {
                        return ErrorValue("Wrong number of arguments: want=3, got=${arguments.size}")
                    }
                    val array = arguments[0]
                    if (array !is ArrayValue) {
                        return ErrorValue("First argument must be an array")
                    }
                    val function = arguments[1]
                    if (function !is FunctionValue) {
                        return ErrorValue("Second argument must be a function")
                    }
                    val initialValue = arguments[2]
                    var accumulator = initialValue
                    for (element in array.elements) {
                        accumulator = function.call(listOf(accumulator, element), astEvaluator, environment)
                    }
                    return accumulator
                }
            })
            return environment
        }
    }
}

class AstEvaluator : AstVisitor<Environment, Value<Any?>> {
    override fun visitProgram(program: Program, data: Environment): Value<Any?> {
        var result: Value<Any?> = NullValue
        for (statement in program.statements) {
            val value = statement?.visit(this, data) ?: ErrorValue("Statement is null")
            if (value is ErrorValue) {
                return value
            }
            if (value is ReturnValue) {
                return value.value
            }
            result = value
        }
        return result
    }

    override fun visit(ast: Ast, data: Environment): Value<Any?> {
        return ast.visit(this, data)
    }

    override fun visitIdentifier(identifier: Identifier, data: Environment): Value<Any?> {
        return data.get(identifier.value)
    }

    override fun visitFunctionExpression(functionExpression: FunctionExpression, data: Environment): Value<Any?> {
        return UserFunctionValue(
            functionExpression.parameters.map {
                it ?: return ErrorValue("Function parameter must have a name")
            },
            functionExpression.body
        )
    }

    override fun visitBinaryExpression(binaryExpression: BinaryExpression, data: Environment): Value<Any?> {
        val leftValue = binaryExpression.left?.visit(this, data) ?: ErrorValue("Left expression is null")
        if (leftValue is ErrorValue) {
            return leftValue
        }
        val rightValue = binaryExpression.right?.visit(this, data) ?: ErrorValue("Right expression is null")
        if (rightValue is ErrorValue) {
            return rightValue
        }
        val op = binaryExpression.op
        return leftValue.calculate(op, rightValue)
    }

    override fun visitUnaryExpression(unaryExpression: UnaryExpression, data: Environment): Value<Any?> {
        val rightValue = unaryExpression.right?.visit(this, data) ?: ErrorValue("Right expression is null")
        if (rightValue is ErrorValue) {
            return rightValue
        }
        return rightValue.calculate(unaryExpression.op)
    }

    override fun visitFunctionCallExpression(functionCallExpression: FunctionCallExpression, data: Environment): Value<Any?> {
        val function = functionCallExpression.function?.visit(this, data) ?: ErrorValue("Function is null")
        if (function !is FunctionValue) {
            return ErrorValue("Not a function")
        }
        val arguments = functionCallExpression.arguments.map {
            it?.visit(this, data) ?: return ErrorValue("Argument is null")
        }
        val value = function.call(arguments, this, Environment(data))
        if (value is ReturnValue) {
            return value.value
        }
        return value
    }

    override fun visitFunctionDefStatement(functionDefStatement: FunctionDefStatement, data: Environment): Value<Any?> {
        val parameters = functionDefStatement.parameters.map {
            it ?: return ErrorValue("Function parameter must have a name")
        }
        val function = UserFunctionValue(parameters, functionDefStatement.body)
        data.set(functionDefStatement.name!!.value, function)
        return function
    }

    override fun visitReturnStatement(returnStatement: ReturnStatement, data: Environment): Value<Any?> {
        val value = returnStatement.value?.visit(this, data) ?: ErrorValue("Return value is null")
        if (value is ErrorValue) {
            return value
        }
        return ReturnValue(value)
    }

    override fun visitIfStatement(ifStatement: IfStatement, data: Environment): Value<Any?> {
        val condition = ifStatement.condition?.visit(this, data) ?: ErrorValue("Condition is null")
        if (condition !is BooleanValue) {
            return ErrorValue("Condition must be a boolean")
        }
        return if (condition.value) {
            ifStatement.consequence?.visit(this, data) ?: ErrorValue("Consequence is null")
        } else {
            ifStatement.alternative?.visit(this, data) ?: ErrorValue("Alternative is null")
        }
    }

    override fun visitWhileStatement(whileStatement: WhileStatement, data: Environment): Value<Any?> {
        TODO("Not yet implemented")
    }

    override fun visitBlockStatement(blockStatement: BlockStatement, data: Environment): Value<Any?> {
        var result: Value<Any?> = NullValue
        for (statement in blockStatement.statements) {
            result = statement?.visit(this, data) ?: ErrorValue("Statement is null")
            if (result is ErrorValue) {
                return result
            }
            if (result is ReturnValue) {
                return result
            }
        }
        return result
    }

    override fun visitExpressionStatement(expressionStatement: ExpressionStatement, data: Environment): Value<Any?> {
        return expressionStatement.expression?.visit(this, data) ?: ErrorValue("Expression is null")
    }

    override fun toString(): String {
        return "AstEvaluator()"
    }

    override fun visitArrayIndexExpression(arrayIndexExpression: ArrayIndexExpression, data: Environment): Value<Any?> {
        val array = arrayIndexExpression.array?.visit(this, data) ?: ErrorValue("Array is null")
        if (array !is ArrayValue) {
            return ErrorValue("Not an array")
        }
        val index = arrayIndexExpression.index?.visit(this, data) ?: ErrorValue("Index is null")
        if (index !is IntegerValue) {
            return ErrorValue("Index must be an integer")
        }
        if (index.value !in array.elements.indices) {
            return ErrorValue("Index out of bounds")
        }
        return array.elements[index.value]
    }

    override fun visitArrayLiteral(arrayLiteral: ArrayLiteral, data: Environment): Value<Any?> {
        val elements = arrayLiteral.elements.map {
            it?.visit(this, data) ?: return ErrorValue("Array element is null")
        }
        if (elements.any { it is ErrorValue }) {
            return ErrorValue("Array elements must be valid")
        }
        return ArrayValue(elements)
    }

    override fun visitAssignmentExpression(assignmentExpression: AssignmentExpression, data: Environment): Value<Any?> {
        if (assignmentExpression.value == null) {
            return ErrorValue("Value is null")
        }
        val value = assignmentExpression.value.visit(this, data)
        if (value is ErrorValue) {
            return value
        }
        if (assignmentExpression.name == null) {
            return ErrorValue("Assignment must have a name")
        }
        data.set(assignmentExpression.name.value, value)
        return value
    }

    override fun visitBooleanLiteral(booleanLiteral: BooleanLiteral, data: Environment): Value<Any?> {
        return if (booleanLiteral.literalValue()) BooleanValue.True else BooleanValue.False
    }

    override fun visitStringLiteral(stringLiteral: StringLiteral, data: Environment): Value<Any?> {
        return StringValue(stringLiteral.literalValue())
    }

    override fun visitFloatLiteral(floatLiteral: FloatLiteral, data: Environment): Value<Any?> {
        return FloatValue(floatLiteral.literalValue())
    }

    override fun visitIntegerLiteral(integerLiteral: IntegerLiteral, data: Environment): Value<Any?> {
        return IntegerValue(integerLiteral.literalValue())
    }

    override fun visitLetStatement(letStatement: LetStatement, data: Environment): Value<Any?> {
        if (letStatement.name == null) {
            return ErrorValue("Statement must have a name")
        }
        val value = letStatement.value?.visit(this, data) ?: ErrorValue("Statement must have a value")
        if (value is ErrorValue) {
            return value
        }
        data.set(letStatement.name.value, value)
        return value
    }
}
