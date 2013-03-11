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
    private static final String WS_EVENT = "org.bejug.tictactoe.client.websocket_event";

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

    private enum WebsocketConnectionEvent {
        CONNECTED, CLOSE, ERROR
    }

    public interface TicTacToeGameCallback {
        void onWebsocketConnectionChange(String state);
        void onGameStateChange(GameState gameState);
        void onGameBoardChange(TicTacToeCell[] positions);
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

    public void onWebsocketConnectionEvent(final WebsocketConnectionEvent event, final String extra) {
        switch (event) {
            case CONNECTED:
                Log.d(TAG, "Websocket connection:: connected");
                mCallback.onWebsocketConnectionChange("CONNECTED");
                break;

            case CLOSE:
                Log.d(TAG, "Websocket connection:: closed: " + extra);
                mCallback.onWebsocketConnectionChange("CLOSED");
                break;

            case ERROR:
                Log.d(TAG, "Websocket connection:: error: " + extra);
                mCallback.onWebsocketConnectionChange("ERROR");
                break;
        }
    }

    public void onWebsocketMessageInput(final String msg) {
        Log.d(TAG, "WS message received:: " + msg);
        if ("p1".equalsIgnoreCase(msg)) {
            setCurrentState(GameState.WAITING);
            Log.d(TAG, "Waiting for other player");
        } else if ("p2".equalsIgnoreCase(msg)) {
            this.player = TicTacToePossibility.NOUGHT;
            setCurrentState(GameState.PLAYING);
            Log.d(TAG, "Game joined, playing p2 with O, and my turn");
        } else if ("p3".equalsIgnoreCase(msg)) {
            this.player = TicTacToePossibility.CROSS;
            setCurrentState(GameState.WAITING);
            Log.d(TAG, "Game joined, playing p1 with X, and other players turn");
        } else if (msg.startsWith("o")) {
            final int position = Integer.parseInt(msg.substring(1));
            updateCells(position, TicTacToePossibility.NOUGHT);
            setCurrentState(GameState.PLAYING);
        } else if (msg.startsWith("x")) {
            final int position = Integer.parseInt(msg.substring(1));
            updateCells(position, TicTacToePossibility.CROSS);
            setCurrentState(GameState.PLAYING);
        }
    }

    private void updateCells(final int position, final TicTacToePossibility type) {
        this.positions[position] = new TicTacToeCell(type);
        this.currentState = GameState.PLAYING;
    }

    public void onPLayersInput(final int cellPosition) {
        Log.d(TAG, "User clicked on cell " + cellPosition);
        if (currentState == GameState.PLAYING) {
            updateCells(cellPosition, this.player);
            setCurrentState(GameState.WAITING);
        }
    }

    private void setCurrentState(final GameState gameState) {
        this.currentState = gameState;
        mCallback.onGameStateChange(gameState);
    }

    // Methods to handle the websocket events

    @Override
    public void onWSConnected() {
        mWSConnectionHandler.sendMessage(getWSEventMessage(WebsocketConnectionEvent.CONNECTED, null));
    }

    @Override
    public void onWSMessageReceived(String msg) {
        mWSMessageHandler.sendMessage(getWSStringMessage(msg));
    }

    @Override
    public void onWSClose(int code, String reason) {
        mWSConnectionHandler.sendMessage(getWSEventMessage(WebsocketConnectionEvent.CLOSE, reason));
    }

    @Override
    public void onWSError(Exception e) {
        mWSConnectionHandler.sendMessage(getWSEventMessage(WebsocketConnectionEvent.ERROR, e.getMessage()));
    }

    Handler mWSConnectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();
            final WebsocketConnectionEvent _event = (WebsocketConnectionEvent) bundle.getSerializable(WS_EVENT);
            final String _extra = bundle.getString(WS_MESSAGE);
            onWebsocketConnectionEvent(_event, _extra);
        }
    };

    Handler mWSMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();
            final String _msg = bundle.getString(WS_MESSAGE);
            onWebsocketMessageInput(_msg);
        }
    };

    private Message getWSStringMessage(final String msg) {
        final Bundle bundle = new Bundle();
        final Message message = mWSMessageHandler.obtainMessage();
        bundle.putString(WS_MESSAGE, msg);
        message.setData(bundle);
        return message;
    }

    private Message getWSEventMessage(final WebsocketConnectionEvent event, final String extraInfo) {
        final Bundle bundle = new Bundle();
        final Message message = mWSConnectionHandler.obtainMessage();
        bundle.putSerializable(WS_EVENT, event);
        bundle.putString(WS_MESSAGE, extraInfo);
        message.setData(bundle);
        return message;
    }

    public void setTicTacToeGameCallback(TicTacToeGameCallback callback) {
        if (callback == null) {
            mCallback = sDummyCallback;
        } else {
            mCallback = callback;
        }
    }

    private static TicTacToeGameCallback sDummyCallback = new TicTacToeGameCallback() {
        @Override
        public void onGameBoardChange(final TicTacToeCell[] positions) {

        }

        @Override
        public void onWebsocketConnectionChange(final String state) {

        }

        @Override
        public void onGameStateChange(final GameState gameState) {

        }
    };
}
