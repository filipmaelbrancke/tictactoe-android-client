package org.bejug.tictactoe.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.bejug.tictactoe.client.websocket.TestWebsocketClient;
import org.bejug.tictactoe.client.websocket.WebSocket;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_76;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TicTacToeActivity extends Activity implements TestWebsocketClient.TicTacToeCallback {

    private static String TAG = TicTacToeActivity.class.getSimpleName();

    private static final String WS_MESSAGE = "org.bejug.tictactoe.client.websocket_message";

    private TestWebsocketClient c;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);


        /*WebSocket webSocket = new WebSocket(URI.create("ws://ec2-54-242-90-129.compute-1.amazonaws.com:80/tictactoeserver/endpoint"), WebSocket.Draft.DRAFT76, "myId");
        try {
            webSocket.connect();
        } catch (IOException e) {
            Log.e(TAG, "WebSocket exception: " + e.getMessage());
        }*/


        try {
            c = new TestWebsocketClient(new URI("ws://ec2-54-242-90-129.compute-1.amazonaws.com:80/tictactoeserver/endpoint"), new Draft_17());
            c.setTacTacToeCallback(this);
            c.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "WebSocket exception: " + e.getMessage());
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Bundle bundle = msg.getData();
            final String _msg = bundle.getString(WS_MESSAGE);
            Toast.makeText(getApplicationContext(), "msg: " + _msg, Toast.LENGTH_LONG).show();
        }
    };

    public void showMessage1() {
        Toast.makeText(this, "Waiting for other player", Toast.LENGTH_LONG).show();
    }

    public void showMessage2() {
        Toast.makeText(this, "Game joined: playing p2", Toast.LENGTH_LONG).show();
    }

    public void showMessage3() {
        Toast.makeText(this, "Game joined: playing p1", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMessageReceived(String msg) {
        Log.d(TAG, " // message:: " + msg);
        /*if ("p1".equalsIgnoreCase(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage1();
                }
            });
        } else if ("p2".equalsIgnoreCase(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage2();
                }
            });
        } else if ("p3".equalsIgnoreCase(msg)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage3();
                }
            });
        } else if (msg.startsWith("o")) {

        } else if (msg.startsWith("x")) {

        }*/

        mHandler.sendMessage(getMessage(msg));
    }

    private Message getMessage(final String msg) {
        final Bundle bundle = new Bundle();
        final Message message = mHandler.obtainMessage();
        bundle.putString(WS_MESSAGE, msg);
        message.setData(bundle);
        return message;
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "  connected");
    }
}

