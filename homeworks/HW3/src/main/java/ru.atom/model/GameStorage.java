package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import ru.atom.geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BBPax on 02.05.17.
 */
public class GameStorage {
    private static final Logger log = LogManager.getLogger(GameStorage.class);
    private static final int PARALLELISM_LEVEL = 4;
    private static final Point[] startpoint = {new Point(1, 1), new Point(1, 14),
            new Point(14, 14), new Point(14, 1)};
    private GameThread currentNewGameThread;
    private List<GameThread> games;

    public GameStorage() {
        this.currentNewGameThread = new GameThread();
        games = new ArrayList<>();
    }

    public int addPlayer(Session session, String login) {
        int playerId = currentNewGameThread.addPawn(session, login, startpoint[currentNewGameThread.poolSize()]);
        if (currentNewGameThread.poolSize() == PARALLELISM_LEVEL) {
            log.info("game is ready to start");
            // TODO: 02.05.17   надо будет еще реализовать старт после полной реализации общения с игровыми сессиями
            games.add(currentNewGameThread);
            currentNewGameThread = new GameThread();
        }
        return playerId;
    }

    public void plantBomb(Session session) {
        findBySession(session).plantBomb(session);
    }

    public void move(Session session, Movable.Direction direction) {
        findBySession(session).move(session, direction);
    }

    private GameThread findBySession (Session session) {
        return games.stream().filter(x -> x.hasConnection(session)).findFirst().get();
    }
}
