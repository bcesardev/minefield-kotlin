package model

import java.util.*
import kotlin.collections.ArrayList

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
        for (line in 0 until numberOfLines) {
            fields.add(ArrayList())
            for (column in 0 until numberOfColumns) {
                val newField = Field(line, column)
                newField.onEvent(this::checkVictoryOrDefeat)
                fields[line].add(newField)
            }
        }
    }

    private fun connectNearbies() {
        forEachFields { connectNearbies(it) }
    }

    private fun connectNearbies(field: Field) {
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

        var lineDrawn = -1
        var columnDrawn = -1
        var numberOfMinesActual = 0

        while (numberOfMinesActual < this.numberOfMines) {
            lineDrawn = generator.nextInt(numberOfLines)
            columnDrawn = generator.nextInt(numberOfColumns)

            val fieldDrawn = fields[lineDrawn][columnDrawn]
            if (fieldDrawn.safed) {
                fieldDrawn.mine()
                numberOfMinesActual++
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