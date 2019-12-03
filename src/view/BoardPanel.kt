package view

import model.Board
import java.awt.GridLayout
import javax.swing.JPanel

class BoardPanel(board: Board) : JPanel() {

    init {
        layout = GridLayout(board.qtyOfLines, board.qtyOfColumns)
        board.forEachFields { field ->
            val button = ButtonField(field)
            add(button)
        }
    }
}