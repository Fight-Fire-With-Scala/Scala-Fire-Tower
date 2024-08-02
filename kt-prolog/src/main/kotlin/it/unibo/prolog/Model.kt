package it.unibo.prolog

data class Position(val x: Int, val y: Int)

interface BoardEntity {
    val id: String
}

fun <T> fromId(id: String, values: Array<T>): T? where T : Enum<T>, T : BoardEntity {
    return values.find { it.id == id }
}

enum class Token(override val id: String) : BoardEntity {
    Firebreak("b"),
    Fire("f"),
    Water("w"),
    Reforest("r"),
    Empty("e")
}

enum class Cell(override val id: String) : BoardEntity {
    EternalFire("ef"),
    Woods("w"),
    Tower("t")
}

enum class Directions(val id: String, val delta: Position) {
    North("north", Position(0, 1)),
    West("west", Position(-1, 0)),
    East("east", Position(1, 0)),
    South("south", Position(0, -1))
}
