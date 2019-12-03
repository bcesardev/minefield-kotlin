package model

enum class FieldEvent { OPENING, MARKING, UNMARKING, EXPLOSION, RESET }

data class Field(val line: Int, val column: Int) {

    private val nearbyList = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var marked: Boolean = false
    var opened: Boolean = false
    var mined: Boolean = false

    // Only reading
    val unmarked: Boolean get() = !marked
    val closed: Boolean get() = !opened
    val safe: Boolean get() = !mined
    val goalAchieved: Boolean get() = safe && opened || mined && marked
    val qtyOfNearbyMineds: Int get() = nearbyList.filter { it.mined }.size
    val nearbySafe: Boolean
        get() = nearbyList.map { it.safe }.reduce { result, safe -> result && safe }

    fun addNearby(nearby: Field) {
        nearbyList.add(nearby)
    }

    fun onEvent(callback: (Field, FieldEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (closed) {
            opened = true
            if (mined) {
                callbacks.forEach { it(this, FieldEvent.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, FieldEvent.OPENING) }
                nearbyList.filter { it.closed && it.safe && nearbySafe }.forEach { it.open() }
            }
        }
    }

    fun changeMarking() {
        if (closed) {
            marked = !marked
            val event = if (marked) FieldEvent.MARKING else FieldEvent.UNMARKING
            callbacks.forEach { it(this, event) }
        }
    }

    fun mine() {
        mined = true
    }

    fun reset() {
        opened = false
        mined = false
        marked = false
        callbacks.forEach { it(this, FieldEvent.RESET) }
    }
}