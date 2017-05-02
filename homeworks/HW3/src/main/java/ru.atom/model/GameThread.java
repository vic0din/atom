package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import ru.atom.Ticker;
import ru.atom.geometry.Point;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.ConnectionPool;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BBPax on 02.05.17.
 */
public class GameThread extends Thread {
    private static final Logger log = LogManager.getLogger(GameThread.class);
    Ticker ticker;
    ConnectionPool connectionPool;
    private ConcurrentHashMap<String, Integer> playerPawn = new ConcurrentHashMap<>(4);

    public GameThread() {
        ticker = new Ticker();
        connectionPool = new ConnectionPool();
    }

    public int poolSize() {
        return connectionPool.size();
    }

    public boolean hasConnection(Session session) {
        return connectionPool.getPlayer(session) != null;
    }

    public int addPawn(Session session, String login, Point position) {
        GameSession gameSession = ticker.getGameSession();
        connectionPool.add(session, login);
        log.info("player with login {} has new pawn with id {}", login, gameSession.getCurrentId());
        playerPawn.put(login, gameSession.getCurrentId());
        log.info("tickers GS id: {} | local GS id: {}",
                ticker.getGameSession().getCurrentId(), gameSession.getCurrentId());
        gameSession.addGameObject(new Player(gameSession.getCurrentId(), position));
        return gameSession.getCurrentId() - 1;
    }

    public void plantBomb(Session session) {
        int playerId = playerPawn.get(connectionPool.getPlayer(session));
        Player player = (Player) ticker.getGameSession().getGameObjects().get(playerId);
        log.info("Bomb has been planted by {} in point: ({};{})",
                connectionPool.getPlayer(session), player.getPosition().getX(), player.getPosition().getY());
        // TODO: 02.05.17   надо добавить взаимодействие с GameSession
    }

    public void move(Session session, Movable.Direction direction) {
        int playerId = playerPawn.get(connectionPool.getPlayer(session));
        Player player = (Player) ticker.getGameSession().getGameObjects().get(playerId);
        log.info("Player {} should be moved in direction {}",
                connectionPool.getPlayer(session), direction);
        // TODO: 02.05.17   надо добавить взаимодействие с GameSession
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
