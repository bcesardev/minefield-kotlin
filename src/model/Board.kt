package model

import java.util.*

enum class BoardEvent { VICTORY, DEFEAT }

class Board(val qtyOfLines: Int, val qtyOfColumns: Int, private val qtyOfMines: Int) {

    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvent) -> Unit>()

    init {
        generateFields()
        connectNearby()
        sortMines()
    }

    private fun generateFields() {
        for (line in 0 until qtyOfLines) {
            fields.add(ArrayList())
            for (column in 0 until qtyOfColumns) {
                val newField = Field(line, column)
                newField.onEvent(this::checkVictoryOrDefeat)
                fields[line].add(newField)
            }
        }
    }

    private fun connectNearby() {
        forEachFields { connectNearby(it) }
    }

    private fun connectNearby(field: Field) {
        val (line, column) = field
        val lines = arrayOf(line - 1, line, line + 1)
        val columns = arrayOf(column - 1, column, column + 1)

        lines.forEach { l ->
            columns.forEach { c ->
                val actual = fields.getOrNull(l)?.getOrNull(c)
                actual?.takeIf { field != it }?.let { field.addNearby(it) }
            }
        }
    }

    private fun sortMines() {
        val generator = Random()

        var drawnLine = -1
        var drawnColumn = -1
        var qtyOfMinesActual = 0

        while (qtyOfMinesActual < this.qtyOfMines) {
            drawnLine = generator.nextInt(qtyOfLines)
            drawnColumn = generator.nextInt(qtyOfColumns)

            val drawnField = fields[drawnLine][drawnColumn]
            if (drawnField.safe) {
                drawnField.mine()
                qtyOfMinesActual++
            }
        }
    }

    private fun goalAchieved(): Boolean {
        var playerWon = true
        forEachFields { if (!it.goalAchieved) playerWon = false }
        return playerWon
    }

    private fun checkVictoryOrDefeat(field: Field, event: FieldEvent) {
        if (event == FieldEvent.EXPLOSION) {
            callbacks.forEach { it(BoardEvent.DEFEAT) }
        } else if (goalAchieved()) {
            callbacks.forEach { it(BoardEvent.VICTORY) }
        }
    }

    fun forEachFields(callback: (Field) -> Unit) {
        fields.forEach { line -> line.forEach(callback) }
    }

    fun onEvent(callback: (BoardEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun reset() {
        forEachFields { it.reset() }
        sortMines()
    }
}