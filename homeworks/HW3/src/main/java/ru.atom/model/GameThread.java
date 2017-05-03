package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.websocket.api.Session;
import ru.atom.Ticker;
import ru.atom.geometry.Point;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.ConnectionPool;

import java.util.concurrent.ConcurrentHashMap;

import static ru.atom.WorkWithProperties.getProperties;

/**
 * Created by BBPax on 02.05.17.
 */
public class GameThread extends Thread {
    private static final Logger log = LogManager.getLogger(GameThread.class);
    public static final int MAX_CAPACITY_FOR_ACTIONS = Integer.valueOf(getProperties().getProperty("MAX_CAPACITY_FOR_ACTIONS"));
    Ticker ticker;
    ConnectionPool connectionPool;
    private ConcurrentHashMap<String, Integer> playerPawn = new ConcurrentHashMap<>(Integer.valueOf(getProperties().getProperty("PARALLELISM_LEVEL")));

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
        crateActionForObject(playerId, Actions.PLANT_BOMB);
    }

    public void move(Session session, Movable.Direction direction) {
        int playerId = playerPawn.get(connectionPool.getPlayer(session));
        Player player = (Player) ticker.getGameSession().getGameObjects().get(playerId);
        log.info("Player {} should be moved in direction {}",
                connectionPool.getPlayer(session), direction);
        // TODO: 02.05.17   надо добавить взаимодействие с GameSession
        switch (direction) {
            case UP:
                crateActionForObject(playerId, Actions.MOVE_UP);
                break;
            case DOWN:
                crateActionForObject(playerId, Actions.MOVE_DOWN);
                break;
            case LEFT:
                crateActionForObject(playerId, Actions.MOVE_LEFT);
                break;
            case RIGHT:
                crateActionForObject(playerId, Actions.MOVE_RIGHT);
                break;
            case IDLE:
                crateActionForObject(playerId, Actions.IDLE);
                break;
        }
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

    private void crateActionForObject(Integer id, Actions action){
        if (ticker.getGameSession().getActions().containsKey(id)) {
            ticker.getGameSession().getActions().get(id).offer(action);
        } else {
            BlockingArrayQueue<Actions> newActions = new BlockingArrayQueue<>(MAX_CAPACITY_FOR_ACTIONS);
            newActions.offer(action);
            ticker.getGameSession().getActions().put(id, newActions);
        }
    }
}
