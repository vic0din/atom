package ru.atom.lecture09.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.masks.ZeroMasker;
import sun.plugin2.message.JavaObjectOpMessage;

import java.util.concurrent.Future;

/**
 * Created by BBPax on 22.04.17.
 */
public class MyNewClient {
    public static void main(String[] args) {
        Packet p = new Packet("BBPax");
        try (Socket socket = new Socket("wtfis.ru", 12345);
             OutputStream os = socket.getOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(os)
        ) {
                oos.writeObject(p);
        } catch (IOException e) {
            System.out.println("smth goes wrong");
        }

    }

}
