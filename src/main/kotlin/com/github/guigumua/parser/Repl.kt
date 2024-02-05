package com.github.guigumua.parser


object Repl {
    fun start() {
        val environment = Environment.createBuiltins()
        val astEvaluator = AstEvaluator()
        while (!Thread.currentThread().isInterrupted) {
            print(">> ")
            val line = readln()
            val lexer = Lexer(line)
            val parser = Parser(lexer)
            val program = parser.parse()
            if (parser.hasError()) {
                parser.printError(System.out)
                continue
            }
            val evaluate = astEvaluator.visit(program, environment)
            println(evaluate)
        }
    }

}
