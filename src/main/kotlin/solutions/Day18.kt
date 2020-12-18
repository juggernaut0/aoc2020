package solutions

import components.BasePuzzle

class Day18 : BasePuzzle("Day 18: Operation Order") {
    override fun solvePart1(input: String): String {
        return input.lineSequence()
            .filter { it.isNotBlank() }
            .map { parseExpr(TokenStream(it)).eval() }
            .sum()
            .toString()
    }

    override fun solvePart2(input: String): String {
        return input.lineSequence()
            .filter { it.isNotBlank() }
            .map { parseExpr2(TokenStream(it)).eval() }
            .sum()
            .toString()
    }

    interface Expr {
        fun eval(): Long
    }
    enum class Op { ADD, MUL }
    data class BinaryExpr(val left: Expr, val right: Expr, val op: Op) : Expr {
        override fun eval(): Long {
            return when (op) {
                Op.ADD -> left.eval() + right.eval()
                Op.MUL -> left.eval() * right.eval()
            }
        }

        override fun toString(): String {
            return "($left $op $right)"
        }
    }
    data class Primary(val value: Long) : Expr {
        override fun eval(): Long {
            return value
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    class TokenStream(private val src: String) {
        private var current = 0

        fun peek(): Char? = src.getOrNull(current)
        fun advance(): Char? {
            val c = peek() ?: return null

            do {
                current++
            } while (peek()?.isWhitespace() == true)

            return c
        }

        override fun toString(): String {
            return "'${src.substring(current)}'"
        }
    }

    private fun parseExpr(line: TokenStream): Expr {
        val leftChar = line.advance() ?: throw IllegalStateException("empty source")
        var left = if (leftChar == '(') {
            parseExpr(line)
        } else {
            Primary(leftChar.toString().toLong())
        }
        while (true) {
            val opChar = line.advance() ?: return left
            val op = when (opChar) {
                ')' -> return left
                '+' -> Op.ADD
                '*' -> Op.MUL
                else -> throw IllegalStateException("Unexpected $opChar")
            }
            val rightChar = line.advance() ?: throw IllegalStateException("Unexpected end of input")
            val right = if (rightChar == '(') {
                parseExpr(line)
            } else {
                Primary(rightChar.toString().toLong())
            }
            left = BinaryExpr(left, right, op)
        }
    }

    private fun parseExpr2(line: TokenStream): Expr {
        var left = parseSum(line)
        while (true) {
            val opChar = line.peek() ?: return left
            val op = when (opChar) {
                ')' -> return left
                '*' -> Op.MUL
                '+' -> return left
                else -> throw IllegalStateException("Unexpected $opChar")
            }
            line.advance()
            val right = parseSum(line)
            left = BinaryExpr(left, right, op)
        }
    }

    private fun parseSum(line: TokenStream): Expr {
        var left = parsePrimary(line)
        while (true) {
            val opChar = line.peek() ?: return left
            val op = when (opChar) {
                ')' -> return left
                '*' -> return left
                '+' -> Op.ADD
                else -> throw IllegalStateException("Unexpected $opChar")
            }
            line.advance()
            val right = parsePrimary(line)
            left = BinaryExpr(left, right, op)
        }
    }

    private fun parsePrimary(line: TokenStream): Expr {
        val c = line.advance() ?: throw IllegalStateException("Unexpected end of input")
        return if (c == '(') {
            val e = parseExpr2(line)
            line.advance()?.also { check(it == ')') }
            e
        } else {
            Primary(c.toString().toLong())
        }
    }
}