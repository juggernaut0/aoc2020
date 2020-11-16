package components

import styleClass

object AocStyles {
    private const val col2 = "${200.0/12}%"
    private const val col3 = "${300.0/12}%"
    private const val col4 = "${400.0/12}%"
    private const val col6 = "${600.0/12}%"
    private const val col8 = "${800.0/12}%"
    private const val col10 = "${1000.0/12}%"

    private const val borderGray = "#ccc"

    val wrapper by styleClass {
        +"""
        width: 100%;
        font-family: "Segoe UI",Roboto,sans-serif;
        color: #222;
        """.trimIndent()
    }

    val container by styleClass {
        +"""
        display: flex;
        flex-wrap: wrap;
        width: 100%;
        box-sizing: border-box;
        border: 1px solid $borderGray;
        border-radius: 0.5rem;
        background-color: #f7f7f7;
        font-family: "Segoe UI",Roboto,sans-serif;
        color: #222;
        overflow: hidden;
        """.trimIndent()
    }

    val tabs by styleClass {
        +"""
        display: inline-block;
        background-color: #333;
        flex: 0 0 2rem;
        """.trimIndent()
    }

    val tab by styleClass {
        +"""
            width: 100%;
            height: 2rem;
            border: 0;
            border-bottom: 1px solid #666;
            background-color: inherit;
            color: #fff;
        """.trimIndent()

        pseudo("hover") {
            +"background-color: #999;"
        }

        pseudo("focus") {
            +"outline: none;"
        }
    }

    val puzzlePanel by styleClass {
        +"""
            padding: 1rem;
            flex-grow: 1;
        """.trimIndent()
    }

    val inputBox by styleClass {
        +"width: 100%;"
    }

    val solveButton by styleClass {
        +"""
            margin-left: auto;
            margin-right: auto;
            display: block;
            width: 20rem;
        """.trimIndent()
    }
}