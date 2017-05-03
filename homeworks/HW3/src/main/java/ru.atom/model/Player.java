package ru.atom.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

/**
 * Created by BBPax on 06.03.17.
 */
public class Player extends AbstractGameObject implements Movable {
    private static final Logger log = LogManager.getLogger(Player.class);
    private int bagSize;
    private int powerBomb;
    private int velocity;
    private long elapsedTime;

    public Player setBagSize(int bagSize) {
        this.bagSize = bagSize;
        return this;
    }

    public Player setPowerBomb(int powerBomb) {
        this.powerBomb = powerBomb;
        return this;
    }

    public Player setVelocity(int velocity) {
        this.velocity = velocity;
        return this;
    }

    public Player(int id, Point position) {
        super(id, position);
        type = "Pawn";
        bagSize = 1;
        powerBomb = 1;
        velocity = 1;
        elapsedTime = 0L;
        log.info("Player(id = {}) is created in ( {} ; {} )", id, position.getX(), position.getY());
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void tick(long elapsed) {
        elapsedTime += elapsed;
        switch (getAction()) {
            case MOVE_UP:
                move(Movable.Direction.UP);
                break;
            case MOVE_DOWN:
                move(Movable.Direction.DOWN);
                break;
            case MOVE_LEFT:
                move(Movable.Direction.LEFT);
                break;
            case MOVE_RIGHT:
                move(Movable.Direction.RIGHT);
                break;
            default:
                break;
        }
    }

    @Override
    public Point getPosition() {
        return super.getPosition();
    }

    public int getPowerBomb() {
        return powerBomb;
    }

    @Override
    public Point move(Direction direction) {
        if (direction == null) {
            log.error("direction shouldn't be null");
            throw new NullPointerException();
        }
        switch (direction) {
            case UP:
                position = new Point(this.getPosition().getX(), this.getPosition().getY() + velocity);
                return position;
            case DOWN:
                position = new Point(this.getPosition().getX(), this.getPosition().getY() - velocity);
                return position;
            case IDLE:
                return getPosition();
            case LEFT:
                position = new Point(this.getPosition().getX() - velocity, this.getPosition().getY());
                return position;
            case RIGHT:
                position = new Point(this.getPosition().getX() + velocity, this.getPosition().getY());
                return position;
            default:
                return getPosition();
        }
    }
}
