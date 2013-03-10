package org.bejug.tictactoe.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.bejug.tictactoe.client.websocket.TicTacToeWebsocketClient;
import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public class TicTacToeGame implements TicTacToeWebsocketClient.TicTacToeWSClientCallback {

    private static final String TAG = TicTacToeGame.class.getSimpleName();
    private static final String SERVER_URL = "ws://ec2-54-242-90-129.compute-1.amazonaws.com:80/tictactoeserver/endpoint";
    private static final String WS_MESSAGE = "org.bejug.tictactoe.client.websocket_message";

    private GameState currentState;
    private TicTacToePossibility player = TicTacToePossibility.NONE;
    private TicTacToeGameCallback mCallback = sDummyCallback;
    private TicTacToeWebsocketClient wsClient;

    private TicTacToeCell[] positions = null;

    public enum GameState {
        INIT, WAITING, PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }

    public enum TicTacToePossibility {
        NONE, CROSS, NOUGHT
    }

    public interface TicTacToeGameCallback {
        void onWebsocketConnectionChange(String state);
        void onGameStateChange(GameState gameState);
        void onGameBoardChange();
    }

    public TicTacToeGame() {
        initGame();
    }

    private void initGame() {
        this.currentState = GameState.INIT;
        // initialize the tictactoe positions
        positions = new TicTacToeCell[9];
        for (int i = 0; i < 9; i++) {
            positions[i] = new TicTacToeCell(TicTacToePossibility.NONE);
        }
    }

    private void startGame() {
        try {
            wsClient = new TicTacToeWebsocketClient(new URI(SERVER_URL), new Draft_17());
            wsClient.setTicTacToeWSClientCallback(this);
            wsClient.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "WebSocket exception: " + e.getMessage());
        }
    }

    public void onWebsocketMessageInput(final String msg) {
        Log.d(TAG, "WS message received:: " + msg);
        if ("p1".equalsIgnoreCase(msg)) {
            setCurrentState(GameState.WAITING);
        } else if ("p2".equalsIgnoreCase(msg)) {
            this.player = TicTacToePossibility.NOUGHT;
            setCurrentState(GameState.PLAYING);
        } else if ("p3".equalsIgnoreCase(msg)) {
            this.player = TicTacToePossibility.CROSS;
            setCurrentState(GameState.PLAYING);
        } else if (msg.startsWith("o")) {

        } else if (msg.startsWith("x")) {

        }
    }

    public void onPLayersInput(final int cellLocation) {

    }

    private void setCurrentState(final GameState gameState) {
        this.currentState = gameState;
        mCallback.onGameStateChange(gameState);
    }

    // Methods to handle the websocket events

    @Override
    public void onWSConnected() {
        Log.d(TAG, "Websocket connection:: connected");
    }

    @Override
    public void onWSMessageReceived(String msg) {
        mWSMessageHandler.sendMessage(getMessage(msg));
    }

    @Override
    public void onWSClose(int code, String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onWSError(Exception e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    Handler mWSMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();
            final String _msg = bundle.getString(WS_MESSAGE);
            onWebsocketMessageInput(_msg);
        }
    };

    private Message getMessage(final String msg) {
        final Bundle bundle = new Bundle();
        final Message message = mWSMessageHandler.obtainMessage();
        bundle.putString(WS_MESSAGE, msg);
        message.setData(bundle);
        return message;
    }

    public void setTicTacToeGameCallback(TicTacToeGameCallback clbck) {
        if (clbck == null) {
            mCallback = sDummyCallback;
        } else {
            mCallback = clbck;
        }
    }

    private static TicTacToeGameCallback sDummyCallback = new TicTacToeGameCallback() {
        @Override
        public void onGameBoardChange() {

        }

        @Override
        public void onWebsocketConnectionChange(String state) {

        }

        @Override
        public void onGameStateChange(GameState gameState) {

        }
    };
}
