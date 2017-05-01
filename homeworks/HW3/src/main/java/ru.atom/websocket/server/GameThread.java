package ru.atom.websocket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import ru.atom.Ticker;
import ru.atom.geometry.Point;
import ru.atom.model.GameSession;
import ru.atom.model.Player;
import ru.atom.model.UnbreakableWall;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;
import ru.atom.websocket.network.ConnectionPool;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BBPax on 01.05.17.
 */
public class GameThread implements Runnable {
    private static final Logger log = LogManager.getLogger(GameThread.class);
    private Ticker ticker = new Ticker();
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private ConcurrentHashMap<Integer, String> playerPawn = new ConcurrentHashMap<>(4);

    public GameThread createMap() {
        GameSession gameSession = new GameSession();
        for (int i = 0; i < 16; i++) {
            gameSession.addGameObject(new UnbreakableWall(gameSession.getCurrentId(), new Point(i, 0)));
            gameSession.addGameObject(new UnbreakableWall(gameSession.getCurrentId(), new Point(0, i)));
            if (i!=0) {
                gameSession.addGameObject(new UnbreakableWall(gameSession.getCurrentId(), new Point(15, i)));
                if (i!=15) {
                    gameSession.addGameObject(new UnbreakableWall(gameSession.getCurrentId(), new Point(i, 15)));
                }
            }
        }
        ticker.setGameSession(gameSession);
        log.info("Map created");
        return this;
    }

    public GameThread addPawn(Session session, String login, Point position) {
        GameSession gameSession = ticker.getGameSession();
        log.info("player with login {} has new pawn with id {}", login, gameSession.getCurrentId());
        playerPawn.put(gameSession.getCurrentId(), login);
        connectionPool.add(session, gameSession.getCurrentId());
        Broker.getInstance().send(login, Topic.POSSESS, gameSession.getCurrentId());
        gameSession.addGameObject(new Player(gameSession.getCurrentId(), position));
        return this;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        log.info("The Game begins!");
        ticker.loop();
    }
}
