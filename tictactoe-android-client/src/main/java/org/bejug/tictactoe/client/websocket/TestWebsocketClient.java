package org.bejug.tictactoe.client.websocket;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 *
 */
public class TestWebsocketClient extends WebSocketClient {

    private static final String TAG = TestWebsocketClient.class.getSimpleName();

    private TicTacToeCallback mCallback = sDummyCallback;

    public TestWebsocketClient(URI serverURI) {
        super(serverURI);
    }

    public TestWebsocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Log.d(TAG, "onOpen:: ");
        mCallback.onConnected();
    }

    @Override
    public void onMessage(String s) {
        Log.d(TAG, "onMessage:: " + s);
        mCallback.onMessageReceived(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.d(TAG, "onClose:: " + s);
    }

    @Override
    public void onError(Exception e) {
        Log.d(TAG, "onError:: " + e.getMessage());
    }

    public interface TicTacToeCallback {
        void onMessageReceived(String msg);
        void onConnected();
    }

    public void setTacTacToeCallback(TicTacToeCallback clbck) {
        mCallback = clbck;
    }

    private static TicTacToeCallback sDummyCallback = new TicTacToeCallback() {
        @Override
        public void onMessageReceived(String msg) {
            Log.d(TAG, "onMessage:: " + msg);
        }

        @Override
        public void onConnected() {
            Log.d(TAG, "onOpen:: ");
        }

    };
}
