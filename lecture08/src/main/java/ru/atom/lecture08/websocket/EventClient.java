package ru.atom.lecture08.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.masks.ZeroMasker;
import ru.atom.lecture08.websocket.message.Message;
import ru.atom.lecture08.websocket.message.Topic;

import java.net.URI;
import java.util.concurrent.Future;

public class EventClient {
    public static void main(String[] args) {
        URI uri = URI.create("ws://localhost:8090/events/");//CHANGE TO wtfis.ru for task

        WebSocketClient client = new WebSocketClient();
        //client.setMasker(new ZeroMasker());
        try {
            try {
                client.start();
                // The socket that receives events
                EventHandler socket = new EventHandler();
                // Attempt Connect
                Future<Session> fut = client.connect(socket, uri);
                // Wait for Connect
                Session session = fut.get();
                // Send a message
                //TODO TASK: implement sending Message with type HELLO and your name as data
                String message = "left";
                String msg = "plant";
                Message mes = new Message(Topic.MOVE, message);
                Gson gson1 = new Gson();
                session.getRemote().sendString(gson1.toJson(mes).toString());
                Message mes1 = new Message(Topic.PLANT_BOMB, msg);
                session.getRemote().sendString(gson1.toJson(mes1).toString());
                session.getRemote().sendString("abrakadabra");
                // Close session
                session.close();
            } finally {
                client.stop();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
