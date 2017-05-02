package ru.atom;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;
import ru.atom.model.GameSession;
import ru.atom.model.Movable;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;
import ru.atom.websocket.server.EventHandler;
import ru.atom.websocket.util.JsonHelper;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * Тест для понимания логики программы
 * Created by Egor Shchurbin on 28.04.2017.
 */
public class ConnectionPoolTest {
//    @Before
//    public void eventServerInitializer() {
//        EventServer.startEventServer();
//    }

    @Test
    public void SendingMessageForServer() {
        URI uri = URI.create("ws://localhost:8090/events/");
        ConnectionPool connectionPool = new ConnectionPool();

        WebSocketClient client = new WebSocketClient();
        GameSession gameSession = new GameSession();
        try {
            try {
                client.start();
                // The socket that receives events
                EventHandler socket = new EventHandler();
                // Attempt Connect
                Future<Session> fut = client.connect(socket, uri);
                // Wait for Connect
                Session session = fut.get();

                //TODO Разделить логику клиента и сервера
                // Клиентская часть
                // В игру заходит нужное количество человек
                connectionPool.add(session, "FirstPlayer");
                connectionPool.add(session, "SecondPlayer");
                connectionPool.add(session, "ThirdPlayer");
                connectionPool.add(session, "FourthPlayer");
                // В идеале, на этом этапе брокер получает команды от пользователей из фронтенда
                Message msg = new Message(Topic.MOVE, JsonHelper.toJson(Movable.Direction.UP));
                Broker.getInstance().send("FirstPlayer", msg.getTopic(), msg.getData());

                //TODO Разделить логику клиента и сервера
                // Серверная часть
                // Получаем данные
                //TODO Понять как получить данные от фронта
                Broker.getInstance().receive(session, JsonHelper.toJson(msg));
                // В этом месте происходят всякие серверные штучки
                // Просиходит тик в модели
                gameSession.tick(1000L);
                // Отправка изменений клиентам
                Broker.getInstance().broadcast(Topic.MOVE, gameSession.getGameObjects());

            } finally {
                client.stop();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

//    @After
//    public void eventServerDisabler() {
//        EventServer.startEventServer();
//    }
}
