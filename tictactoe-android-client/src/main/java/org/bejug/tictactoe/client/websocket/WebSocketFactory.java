package org.bejug.tictactoe.client.websocket;


import java.net.URI;
import java.util.Random;



import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: filip
 * Date: 06/03/13
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketFactory {

    /** The app view. */
    WebView appView;

    /**
     * Instantiates a new web socket factory.
     *
     * @param appView
     *            the app view
     */
    public WebSocketFactory(WebView appView) {
        this.appView = appView;
    }

    public WebSocket getInstance(String url) {
        // use Draft75 by default
        return getInstance(url, WebSocket.Draft.DRAFT75);
    }

    public WebSocket getInstance(String url, WebSocket.Draft draft) {
        WebSocket socket = null;
        Thread th = null;
        try {
            //socket = new WebSocket(appView, new URI(url), draft, getRandonUniqueId());
            socket = new WebSocket(new URI(url), draft, getRandonUniqueId());
            th = socket.connect();
            return socket;
        } catch (Exception e) {
            //Log.v("websocket", e.toString());
            if(th != null) {
                th.interrupt();
            }
        }
        return null;
    }

    /**
     * Generates random unique ids for WebSocket instances
     *
     * @return String
     */
    private String getRandonUniqueId() {
        return "WEBSOCKET." + new Random().nextInt(100);
    }
}
