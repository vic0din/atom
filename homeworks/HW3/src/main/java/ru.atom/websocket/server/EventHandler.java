package ru.atom.websocket.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.util.JsonHelper;

import java.util.Random;

import static java.lang.Math.abs;

public class EventHandler extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
        Broker.getInstance().receive(sess, JsonHelper.toJson(new Message(Topic.HELLO,
                "Vlad" + abs(new Random().nextInt()))));
        //имя по идее надо отправлять разок из фронта(оно скорее всего нужно будет для записи резалта в БД)
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
        Broker.getInstance().receive(super.getSession(), JsonHelper.toJson(new Message(Topic.BYE, "")));
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
        Broker.getInstance().receive(super.getSession(), JsonHelper.toJson(new Message(Topic.BYE, "")));
    }
}
