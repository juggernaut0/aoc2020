import components.AocApp
import kotlinx.browser.document

fun main() {
    kui.mountComponent(document.body!!, AocApp())
}
