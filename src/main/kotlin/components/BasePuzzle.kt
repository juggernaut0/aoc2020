package components

import kotlinx.browser.localStorage
import kui.*
import org.w3c.dom.get
import org.w3c.dom.set

abstract class BasePuzzle(private val title: String) : Component() {
    private val storageKey = "aoc2020-${this::class.simpleName}"
    private var input: String = localStorage[storageKey] ?: ""
    private var output: String by renderOnSet("")

    private inline fun solveAndSave(solveFn: (String) -> String) {
        localStorage[storageKey] = input
        output = solveFn(input)
    }

    abstract fun solvePart1(input: String): String
    abstract fun solvePart2(input: String): String
    open fun MarkupBuilder.visualize() {}

    override fun render() {
        markup().div(classes(Grid.dFlex)) {
            h2(classes(Grid.col12)) { +title }
            textarea(Props(classes = listOf(AocStyles.inputBox, Grid.col12), attrs = mapOf("rows" to "12")), model = ::input)
            button(Props(
                    classes = listOf(Grid.col6),
                    click = { solveAndSave { solvePart1(it) } }
            )) { +"Solve Part 1" }
            button(Props(
                    classes = listOf(Grid.col6),
                    click = { solveAndSave { solvePart2(it) } }
            )) { +"Solve Part 2" }
            pre(classes(Grid.col12)) { +output }
            visualize()
        }
    }
}
