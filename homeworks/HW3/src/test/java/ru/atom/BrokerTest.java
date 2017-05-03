package ru.atom;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.atom.geometry.Point;
import ru.atom.model.GameSession;
import ru.atom.model.UnbreakableWall;
import ru.atom.model.Wall;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.util.JsonHelper;

/**
 * Created by BBPax on 28.04.17.
 */
public class BrokerTest {
    private static final Logger log = LogManager.getLogger(BrokerTest.class);
    private GameSession gameSession;

    @Before
    public void setup() {
        gameSession = new GameSession();
    }

    @Test
    public void connectionPoolTest() {
    }

    @Test
    public void serializationTest() {
        Point point = new Point(1,1);
        Wall wood = new Wall(1, point);
        UnbreakableWall wall = new UnbreakableWall(2, point);
        log.info(JsonHelper.toJson(point));
        log.info(JsonHelper.toJson(wood));
        log.info(JsonHelper.toJson(wall));
        log.info("gs' objects: {}",JsonHelper.toJson(gameSession.getGameObjects()));
        log.info("gs' objects: {}",JsonHelper.toJson(JsonHelper.getJsonNode(JsonHelper.toJson(gameSession.getGameObjects()))));
        Message msg = new Message(Topic.REPLICA, JsonHelper.getJsonNode(JsonHelper.toJson(gameSession.getGameObjects())));
        log.info(JsonHelper.toJson(msg));
    }

    @Test
    public void replicaTest() {

    }
}
