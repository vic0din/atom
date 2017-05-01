package ru.atom.websocket.server;

import ru.atom.Ticker;
import ru.atom.geometry.Point;
import ru.atom.model.GameSession;
import ru.atom.model.Player;

/**
 * Created by Egor Shchurbin on 30.04.2017.
 */
public class GameServer {
    public static void main(String[] args) {
        GameSession gameSession = new GameSession();
        gameSession.addGameObject(new Player(gameSession.getCurrentId(), new Point(0, 0)));
        Ticker ticker = new Ticker();
        ticker.setGameSession(gameSession);
        ticker.loop();
    }
}
