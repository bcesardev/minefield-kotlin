package model

enum class BoardEvent { VICTORY, DEFEAT }

class Board(val numberOfLines: Int, val numberOfColumns: Int, private val numberOfMines: Int) {
    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvent) -> Unit>()

    init {
        generateFields()
        connectNearbies()
        sortMines()
    }

    private fun generateFields() {

    }

    private fun connectNearbies() {

    }

    private fun sortMines() {

    }

    fun forEachFields(callback: (Field) -> Unit){
        fields.forEach { line -> line.forEach(callback) }
    }
}