package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.BlockingArrayQueue;
import ru.atom.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class GameSession implements Tickable {
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private static final Object lock = new Object();
    private List<GameObject> gameObjects = new ArrayList<>();
    //Очередь действий для определенных объектов
    //Важно! Смотри методы для BlockQueue! Лучше использовать offer(e), pool() и peek()
    private ConcurrentHashMap<Integer, BlockingArrayQueue<Actions>> actions = new ConcurrentHashMap<>();
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

    public ConcurrentHashMap<Integer, BlockingArrayQueue<Actions>> getActions() {
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
        for (GameObject gameObject : gameObjects) {
            //Если есть действия для текущего объекта, то выполняем их
            if (actions.containsKey(gameObject.getId())) {
                if (actions.get(gameObject.getId()).peek() != null) {
                    gameObject.setAction(actions.get(gameObject.getId()).poll());
                    objectTick(gameObject, dead, born, elapsed);
                    switch (gameObject.getAction()) {
                        case PLANT_BOMB:
                            born.add(new Bomb(id++,
                                    ((Positionable) gameObject).getPosition(),
                                    ((Player) gameObject).getPowerBomb()));
                    }
                }
            } else {
                objectTick(gameObject, dead, born, elapsed);
            }
        }
        gameObjects.removeAll(dead);
        gameObjects.addAll(born);
    }

    private void objectTick(GameObject gameObject, ArrayList<Temporary> dead, ArrayList<Temporary> born, long elapsed) {
        if (gameObject instanceof Tickable) {
            ((Tickable) gameObject).tick(elapsed);
        }
        if (gameObject instanceof Temporary && ((Temporary) gameObject).isDead()) {
            dead.add((Temporary)gameObject);
        }
    }
}
