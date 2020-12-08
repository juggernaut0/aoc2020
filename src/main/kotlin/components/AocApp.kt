package components

import kui.Component
import kui.Props
import kui.classes
import kui.renderOnSet
import solutions.*

class AocApp : Component() {
    private val tabs: List<BasePuzzle> = listOf(
        Day1(),
        Day2(),
        Day3(),
        Day4(),
        Day5(),
        Day6(),
        Day7(),
        Day8(),
    )
    private var currentTab by renderOnSet(tabs.size) // TODO default to 1 when not Dec 2020

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
                for (i in 1..25) {
                    val visibility = if (i != (currentTab)) mapOf("style" to "display: none;") else emptyMap()
                    div(Props(classes = listOf(AocStyles.puzzlePanel), attrs = visibility)) {
                        tabs.getOrNull(i - 1)?.also { component(it) }
                    }
                }
            }
        }
    }
}
