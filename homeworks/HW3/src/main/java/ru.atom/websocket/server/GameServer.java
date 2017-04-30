package ru.atom.websocket.server;

import ru.atom.Ticker;
import ru.atom.geometry.Point;
import ru.atom.model.GameSession;
import ru.atom.model.Player;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;

/**
 * Created by Egor Shchurbin on 30.04.2017.
 */
public class GameServer {
    public static void main(String[] args) {
        GameSession gameSession = new GameSession();
        gameSession.addGameObject(new Player(gameSession.getCurrentId(), new Point(0, 0)));
        //ConnectionPool.getInstance().add("Test User");
        //TODO здесь почему то не видит данного игрока...
        Broker.getInstance().send("Test User", Topic.POSSESS, 0);
        Ticker ticker = new Ticker();
        ticker.setGameSession(gameSession);
        ticker.loop();
        //ConnectionPool.getInstance().add();
    }
}
