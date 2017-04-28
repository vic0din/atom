package ru.atom.websocket.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.util.JsonHelper;

public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private final ConnectionPool connectionPool;

    public static Broker getInstance() {
        return instance;
    }

    // TODO: 28.04.17   тут где то не хватает добавления игроков в connectionPool.
    private Broker() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void receive(@NotNull Session session, @NotNull String msg) {
        log.info("RECEIVED: " + msg);
        Message message = JsonHelper.fromJson(msg, Message.class);

        if (message.getTopic() == Topic.PLANT_BOMB) {
            log.info("RECEIVED: topic {} with data {}", message.getTopic(), message.getData());
            log.info("connectionPool player: {}", connectionPool.getPlayer(session));
        } else if (message.getTopic() == Topic.MOVE) {
            log.info("RECEIVED: topic {} with data {}", message.getTopic(), message.getData());
        }
    }

    public void send(@NotNull String player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        Session session = connectionPool.getSession(player);
        connectionPool.send(session, message);
    }

    //sending messages to everybody
    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        log.info(message);
        connectionPool.broadcast(message);
    }

}
