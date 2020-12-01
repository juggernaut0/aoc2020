package components

import styleClass

object AocStyles {
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
        +"""
            box-sizing: border-box;
            resize: none;
        """.trimIndent()
    }
}

object Grid {
    private const val c2 = "${200.0/12}%"
    private const val c3 = "${300.0/12}%"
    private const val c4 = "${400.0/12}%"
    private const val c6 = "${600.0/12}%"
    private const val c8 = "${800.0/12}%"
    private const val c10 = "${1000.0/12}%"

    val dFlex by styleClass {
        +"""
            display: flex;
            flex-wrap: wrap;
        """.trimIndent()
    }

    val col6 by styleClass {
        +"""
            flex: 0 0 $c6;
        """.trimIndent()
    }

    val col12 by styleClass {
        +"""
            flex: 0 0 100%;
        """.trimIndent()
    }
}
