package components

import kui.Component
import kui.Props
import kui.classes
import kui.renderOnSet

class AocApp : Component() {
    private val tabs: List<BasePuzzle> = listOf(
        object : BasePuzzle("Day 1") {
            override fun solve(input: String): String {
                return input
            }
        }
    )
    private var currentTab by renderOnSet(1)

    override fun render() {
        markup().div(classes(AocStyles.wrapper)) {
            div(classes(AocStyles.container)) {
                div(classes(AocStyles.tabs)) {
                    for (i in 1..25) {
                        button(Props(
                            classes = listOf(AocStyles.tab),
                            click = { currentTab = i }
                        )) {
                            +"$i"
                        }
                    }
                }
                div(classes(AocStyles.puzzlePanel)) {
                    tabs.getOrNull(currentTab - 1)?.also { component(it) }
                }
            }
        }
    }
}
