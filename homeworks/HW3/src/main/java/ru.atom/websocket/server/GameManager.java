package ru.atom.websocket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.Nullable;
import ru.atom.Ticker;
import ru.atom.websocket.model.Movable;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.atom.WorkWithProperties.getProperties;

/**
 * Created by BBPax on 04.05.17.
 */
public class GameManager {
    private static final Logger log = LogManager.getLogger(GameManager.class);
    private static final GameManager instance = new GameManager();
    private static final int GAME_MAN_PARALLELISM_LEVEL =
            Integer.valueOf(getProperties().getProperty("GAME_MAN_PARALLELISM_LEVEL"));  // TODO: 06.05.17   MAX_PLAYERS


    private final ConcurrentLinkedQueue<Ticker> games;
    private Ticker currentGame;
    private AtomicInteger currentGameId = new AtomicInteger(0);
    private static final Object gameLock = new Object();

    private GameManager() {
        games = new ConcurrentLinkedQueue<>();
        currentGame = new Ticker().setId(currentGameId.getAndIncrement());
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void addPlayer(Session session, String login) {
        log.info("number of players in currentGame before add: {}", currentGame.numberOfPlayers());
        currentGame.addPawn(session, login);
        log.info("number of players in currentGame after add: {}", currentGame.numberOfPlayers());
        if (currentGame.numberOfPlayers() == GAME_MAN_PARALLELISM_LEVEL) {
            startGame();
            games.offer(currentGame);
            log.info("game is started");
            currentGame = new Ticker().setId(currentGameId.getAndIncrement());
            log.info("next game is ready to add new players");
        }
    }

    public void plantBomb(Session session) {
        Ticker ticker = findBySession(session);
        if (ticker != null) {
            ticker.plantBomb(session);
        } else {
            log.info("ignore plant bomb before game is ready to start");
        }
    }

    public void move(Session session, Movable.Direction direction) {
        Ticker ticker = findBySession(session);
        if (ticker != null) {
            ticker.move(session, direction);
        } else {
            log.info("ignore move before game is ready to start");
        }
    }

    public void removePlayer(Session session) {
        Ticker ticker = findBySession(session);
        if (ticker == null) {
            log.warn("number of players in currentGame before remove: {}", currentGame.numberOfPlayers());
            log.info("{} pawn was killed", currentGame.removePawn(session));
            log.warn("number of players in currentGame after remove: {}", currentGame.numberOfPlayers());
        } else {
            log.info("{} pawn will be killed in game", ticker.killPawn(session));
        }
    }

    private void startGame() {
        currentGame.start();
    }

    @Nullable private Ticker findBySession(Session session) {
        try {
            return games.stream().filter(t -> t.containSession(session))
                    .findAny().get();
        } catch (NoSuchElementException e) {
            log.info("Player with such session is not playing now");
            return null;
        }
    }
}
