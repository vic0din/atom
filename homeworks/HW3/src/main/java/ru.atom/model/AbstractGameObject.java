package ru.atom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.geometry.Point;

/**
 * Created by BBPax on 06.03.17.
 */
public class AbstractGameObject implements Positionable {
    private static final Logger log = LogManager.getLogger(AbstractGameObject.class);
    protected String type;
    private int id;
    protected Point position;
    @JsonIgnore
    private Actions action = Actions.IDLE;

    public AbstractGameObject(int id, Point point) {
        if (point.getX() < 0 || point.getY() < 0) {
            log.error("Wrong coordinates of creating objects");
            throw new IllegalArgumentException();
        }
        this.type = "abstractGameObject";
        this.id = id;
        this.position = point;
        log.info("{} was created with coordinates: ( {} ; {} )",
                AbstractGameObject.class.getName(), point.getX(), point.getY());
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getType() {
        return type;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }
}
