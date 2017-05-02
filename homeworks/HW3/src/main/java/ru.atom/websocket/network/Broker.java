package ru.atom.websocket.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import ru.atom.model.GameSession;
import ru.atom.model.GameStorage;
import ru.atom.model.Movable;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.util.JsonHelper;

public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private GameStorage gameStorage;

    public static Broker getInstance() {
        return instance;
    }

    // TODO: 28.04.17   тут где то не хватает добавления игроков в connectionPool.
    private Broker() {
        this.gameStorage = new GameStorage();
    }

    public void receive(@NotNull Session session, @NotNull String msg) {
        Message message = JsonHelper.fromJson(msg, Message.class);
        log.info("RECEIVED: topic {} with data {}", message.getTopic(), message.getData());
        if (message.getTopic() == Topic.PLANT_BOMB) {
            gameStorage.plantBomb(session);
        } else if (message.getTopic() == Topic.MOVE) {
            gameStorage.move(session, JsonHelper.fromJson(message.getData(), Movable.Direction.class));
        } else if (message.getTopic() == Topic.HELLO) {
            String login = message.getData();
            // TODO: 02.05.17   wrong way to send POSSESS, because of absence of Pool
            send(login, Topic.POSSESS, gameStorage.addPlayer(session, login));
        } else if (message.getTopic() == Topic.BYE) {
            // TODO: 02.05.17   надо че то делать
        }
    }

    public void send(@NotNull String player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
//        Session session = connectionPool.getSession(player);
//        connectionPool.send(session, message);
    }

    //sending messages to everybody
    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        log.info(message);
        // TODO: 02.05.17   надо всем в пуле отправлять
//        connectionPool.broadcast(message);
    }
}
