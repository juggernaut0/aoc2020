package components

import kui.*

abstract class BasePuzzle(private val title: String) : Component() {
    private var input: String = ""
    private var output: String by renderOnSet("")

    abstract fun solve(input: String): String
    open fun MarkupBuilder.visualize() {}

    override fun render() {
        markup().div {
            h2 { +title }
            textarea(Props(classes = listOf(AocStyles.inputBox), attrs = mapOf("rows" to "12")), model = ::input)
            button(Props(
                classes = listOf(AocStyles.solveButton),
                click = { output = solve(input) }
            )) { +"Solve" }
            pre { +output }
            visualize()
        }
    }
}
