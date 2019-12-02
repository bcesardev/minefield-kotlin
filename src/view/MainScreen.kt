package view

import model.Board
import model.BoardEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() {
    MainScreen()
}

class MainScreen : JFrame() {

    private val board = Board(numberOfLines = 16, numberOfColumns = 30, numberOfMines = 89)
    private val boardPanel = BoardPanel(board)

    init {
        board.onEvent(this::showResult)
        add(boardPanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Mine Field"
        isVisible = true
    }

    private fun showResult(event: BoardEvent) {
        SwingUtilities.invokeLater {
            val msg = when (event) {
                BoardEvent.VICTORY -> "You win!"
                BoardEvent.DEFEAT -> "You lost. Game over!"
            }

            JOptionPane.showMessageDialog(this, msg)
            board.reset()

            boardPanel.repaint()
            boardPanel.validate()
        }
    }
}