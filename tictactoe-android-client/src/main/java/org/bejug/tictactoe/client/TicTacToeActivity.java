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
    private Button gameControlButton;

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

        gameControlButton = (Button) findViewById(R.id.game_control_button);
        gameControlButton.setOnClickListener(gameControlButtonListener);

        if (game.getGameState() == TicTacToeGame.GameState.INIT) {
            gameControlButton.setVisibility(View.VISIBLE);
        }

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

    View.OnClickListener gameControlButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            game.startGame();
            gameControlButton.setVisibility(View.GONE);
        }
    };
}

