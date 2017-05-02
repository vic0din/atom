package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.BlockingArrayQueue;
import ru.atom.geometry.Point;
import ru.atom.websocket.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static ru.atom.model.Actions.*;

public class GameSession implements Tickable {
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private static final Object lock = new Object();
    private List<GameObject> gameObjects = new ArrayList<>();
    //Очередь действий для определенных объектов
    //Важно! Смотри методы для BlockQueue! Лучше использовать offer(e), pool() и peek()
    private HashMap<Integer, BlockingArrayQueue<Actions>> actions = new HashMap<>();
    private int id = 0;

    /**
     * generation of standard map without pawns
     */
    public GameSession() {
        for (int i = 0; i < 16; i++) {
            addGameObject(new UnbreakableWall(getCurrentId(), new Point(i, 0)));
            addGameObject(new UnbreakableWall(getCurrentId(), new Point(0, i)));
            if (i!=0) {
                addGameObject(new UnbreakableWall(getCurrentId(), new Point(15, i)));
                if (i!=15) {
                    addGameObject(new UnbreakableWall(getCurrentId(), new Point(i, 15)));
                }
            }
        }
        log.info("Map created");
    }

    public int getCurrentId() {
        return id;
    }

    public HashMap<Integer, BlockingArrayQueue<Actions>> getActions() {
        return actions;
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public void addGameObject(GameObject gameObject) {
        synchronized (lock) {
            gameObjects.add(gameObject);
            id++;
        }
    }

    @Override
    public void tick(long elapsed) {
        log.info("tick");
        ArrayList<Temporary> dead = new ArrayList<>();
        ArrayList<Temporary> born = new ArrayList<>();
        //Проходимся по всем объектам игровой сессии
        // TODO: 02.05.17   у Игрока тоже есть переменная тик. Лучше всю логику исполнения команд не выносить из объектов.
        for (GameObject gameObject : gameObjects) {
            //Если есть действия для текущего объекта, то выполняем их
            if (actions.containsKey(gameObject.getId())) {
                switch (actions.get(gameObject.getId()).poll()) {
                    case MOVE_UP:
                        ((Movable) gameObject).move(Movable.Direction.UP);
                        break;
                    case MOVE_DOWN:
                        ((Movable) gameObject).move(Movable.Direction.DOWN);
                        break;
                    case MOVE_LEFT:
                        ((Movable) gameObject).move(Movable.Direction.LEFT);
                        break;
                    case MOVE_RIGHT:
                        ((Movable) gameObject).move(Movable.Direction.RIGHT);
                        break;
                    case PLANT_BOMB:
                            born.add(new Bomb(getCurrentId(),
                                    ((Player) gameObject).getPosition(),
                                    ((Player) gameObject).getPowerBomb()));
                        break;
                    //TODO Сделать появление огня от взрыва бомбы
                    case EXPLODE:
                        break;
                    default:
                        // Nothing To Do
                        log.warn("Can't find action for object {}", gameObject);
                        break;
                }
            } else {
                if (gameObject instanceof Tickable) {
                    // TODO: 28.04.17  тут надо все тикабл объекты изменять
                    ((Tickable) gameObject).tick(elapsed);
                }
                if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
                    dead.add((Temporary)gameObject);
                }
            }
        }
        gameObjects.removeAll(dead);
        gameObjects.addAll(born);
    }
}
