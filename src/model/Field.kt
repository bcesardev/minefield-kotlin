package model

enum class FieldEvent { OPENING, MARKING, UNMARKING, EXPLOSION, RESET }

data class Field(val line: Int, val column: Int) {

    private val nearby = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var marked: Boolean = false
    var opened: Boolean = false
    var mined: Boolean = false

    // Only reading
    val unmarked: Boolean get() = !marked
    val closed: Boolean get() = !opened
    val safed: Boolean get() = !mined
    val goalAchieved: Boolean get() = safed && opened || mined && marked
    val numberOfNearbyMineds: Int get() = nearby.filter { it.mined }.size
}