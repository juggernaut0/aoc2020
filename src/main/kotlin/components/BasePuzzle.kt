package components

import kui.*

abstract class BasePuzzle(private val title: String) : Component() {
    private var input: String = ""
    private var output: String by renderOnSet("")

    abstract fun solvePart1(input: String): String
    abstract fun solvePart2(input: String): String
    open fun MarkupBuilder.visualize() {}

    override fun render() {
        markup().div(classes(Grid.dFlex)) {
            h2(classes(Grid.col12)) { +title }
            textarea(Props(classes = listOf(AocStyles.inputBox, Grid.col12), attrs = mapOf("rows" to "12")), model = ::input)
            button(Props(
                    classes = listOf(Grid.col6),
                    click = { output = solvePart1(input) }
            )) { +"Solve Part 1" }
            button(Props(
                    classes = listOf(Grid.col6),
                    click = { output = solvePart2(input) }
            )) { +"Solve Part 2" }
            pre(classes(Grid.col12)) { +output }
            visualize()
        }
    }
}
