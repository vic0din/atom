package ru.atom;

import org.junit.Before;
import org.junit.Test;
import ru.atom.geometry.Point;
import ru.atom.model.Bomb;
import ru.atom.model.GameSession;
import ru.atom.model.Movable;
import ru.atom.model.Wall;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;
import ru.atom.websocket.util.JsonHelper;

/**
 * Created by BBPax on 28.04.17.
 */
public class BrokerTest {

    @Before
    public void setup() {

    }

    @Test
    public void ConnectionPoolTest() {
        System.out.println(JsonHelper.toJson(Movable.Direction.DOWN));
    }

    @Test
    public void replicaTest() {

    }
}
