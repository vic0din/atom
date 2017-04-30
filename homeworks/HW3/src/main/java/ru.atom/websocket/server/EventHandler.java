package ru.atom.websocket.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;

public class EventHandler extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
        //При установлении соединения добавляем новго пользователя
        //TODO где взять его имя?
        ConnectionPool.getInstance().add(sess, "Test User");
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        Broker.getInstance().receive(super.getSession(), message);
        System.out.println("Received TEXT message: " + message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        //После закрытия убираем соединение из пулла
        ConnectionPool.getInstance().remove(this.getSession());
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
        //TODO При возниконовении ошибки сбрасываю все соединения (Возможно излишне)
        ConnectionPool.getInstance().shutdown();
    }
}
