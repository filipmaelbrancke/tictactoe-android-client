package org.bejug.tictactoe.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

public class TicTacToeActivity extends Activity implements TicTacToeGame.TicTacToeGameCallback, TicTacToeView.TicTacToeViewCallback {

    private static String TAG = TicTacToeActivity.class.getSimpleName();

    @Inject
    TicTacToeGame game;

    private TicTacToeView board;
    private TextView websocketState;
    private TextView gameState;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((TicTacToeApplication) getApplication()).inject(this);

        setContentView(R.layout.main);

        board = (TicTacToeView) findViewById(R.id.tictactoe_board);
        websocketState = (TextView) findViewById(R.id.ws_state);
        gameState = (TextView) findViewById(R.id.game_state);

        game.setTicTacToeGameCallback(this);
        board.setTicTacToeViewCallback(this);

        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(clickListener);

    }

    @Override
    public void onGameBoardChange(TicTacToeCell[] positions) {
        board.setBoardCells(positions);
    }

    @Override
    public void onWebsocketConnectionChange(String state) {
        websocketState.setText(state);
    }

    @Override
    public void onGameStateChange(TicTacToeGame.GameState state) {
        gameState.setText(state.name());
    }

    @Override
    public void onUserClickedCell(int position) {
        game.onPLayersInput(position);
    }

    TextView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TicTacToeCell[] cells = new TicTacToeCell[9];
            for (int i = 0; i < 9; i++) {
                cells[i] = new TicTacToeCell(TicTacToeGame.TicTacToePossibility.NONE);
            }
            cells[0] = new TicTacToeCell(TicTacToeGame.TicTacToePossibility.NOUGHT);
            cells[2] = new TicTacToeCell(TicTacToeGame.TicTacToePossibility.NOUGHT);
            cells[4] = new TicTacToeCell(TicTacToeGame.TicTacToePossibility.CROSS);
            board.setBoardCells(cells);
        }
    };
}

