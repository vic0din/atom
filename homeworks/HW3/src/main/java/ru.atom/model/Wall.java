package ru.atom.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by BBPax on 06.03.17.
 */
public class Wall extends AbstractGameObject {
    private static final Logger log = LogManager.getLogger(Wall.class);

    public Wall(int id, Point position) {
        super(id, position);
        type = "Wood";
        log.info("Wall(id = {}) was created in ( {} ; {} )", id, position.getX(), position.getY());
    }

}
