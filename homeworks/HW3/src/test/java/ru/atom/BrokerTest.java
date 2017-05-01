package ru.atom;

import org.junit.Before;
import org.junit.Test;
import ru.atom.geometry.Point;
import ru.atom.model.Bomb;
import ru.atom.model.GameSession;
import ru.atom.model.Wall;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;

/**
 * Created by BBPax on 28.04.17.
 */
public class BrokerTest {
    private GameSession gameSession = new GameSession();
    private Broker broker = Broker.getInstance();

    @Before
    public void setup() {
        gameSession.addGameObject(new Bomb(2, new Point(1, 1), 10));
    }

    @Test
    public void ConnectionPoolTest() {
    }

    @Test
    public void replicaTest() {
        broker.broadcast(Topic.REPLICA, gameSession.getGameObjects());
    }
}
