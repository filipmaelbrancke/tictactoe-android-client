package org.bejug.tictactoe.client;

/**
 *
 */
public class TicTacToeCell {

    private TicTacToeGame.TicTacToePossibility cellState;

    public TicTacToeCell(TicTacToeGame.TicTacToePossibility cellState) {
        this.cellState = cellState;
    }

    public TicTacToeGame.TicTacToePossibility getCellState() {
        return cellState;
    }

    public void setCellState(TicTacToeGame.TicTacToePossibility cellState) {
        this.cellState = cellState;
    }
}
