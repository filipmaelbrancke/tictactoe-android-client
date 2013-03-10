package org.bejug.tictactoe.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import org.bejug.tictactoe.client.websocket.TicTacToeWebsocketClient;
import org.java_websocket.drafts.Draft_17;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

public class TicTacToeActivity extends Activity implements TicTacToeWebsocketClient.TicTacToeWSClientCallback {

    private static String TAG = TicTacToeActivity.class.getSimpleName();

    @Inject TicTacToeGame game;

    private TicTacToeWebsocketClient c;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((TicTacToeApplication) getApplication()).inject(this);

        setContentView(R.layout.main);
    }

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
    public void onWSConnected() {
        Log.d(TAG, "Websocket connected");
    }

    @Override
    public void onWSMessageReceived(String msg) {
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

        //mHandler.sendMessage(getMessage(msg));
    }

    @Override
    public void onWSClose(int code, String reason) {

    }

    @Override
    public void onWSError(Exception e) {

    }
}

