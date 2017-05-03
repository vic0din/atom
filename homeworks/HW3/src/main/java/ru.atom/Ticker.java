package ru.atom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.model.GameSession;
import ru.atom.websocket.message.Message;
import ru.atom.websocket.message.Topic;
import ru.atom.websocket.network.Broker;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Ticker {
    private static final Logger log = LogManager.getLogger(Ticker.class);
    private static final int FPS = 60;
    // TODO: 03.05.17   time of tick was increased to see all changes
    private static final long FRAME_TIME = 100000 / FPS;
    private long tickNumber = 0;
    private static Object lock = new Object();
    private GameSession gameSession;

    public Ticker() {
        gameSession = new GameSession();
    }

    public void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            long started = System.currentTimeMillis();
            act(FRAME_TIME);
            long elapsed = System.currentTimeMillis() - started;
            if (elapsed < FRAME_TIME) {
                log.info("All tick finish at {} ms", elapsed);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }
            log.info("{}: tick ", tickNumber);
            tickNumber++;
            // TODO: 28.04.17   надо проверить работу вот этой вот строчки
            Broker.getInstance().broadcast(Topic.REPLICA, gameSession.getGameObjects());
        }
    }

    private void act(long time) {
        //Your logic here
        synchronized (lock) {
            gameSession.tick(time);
        }
    }

    public long getTickNumber() {
        return tickNumber;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}