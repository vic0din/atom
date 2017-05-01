package ru.atom.model;

/**
 * Created by Egor Shchurbin on 01.05.2017.
 * Этот класс предназначен для создания действий игровых объектов
 * Возможная реализация: смотри класс Broker (в нем получаем дейсвия от пользователя и заносим
 * их в действия объектов (HashMap Actions в GameSession))
 */
public enum Actions {
    MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT, IDLE, EXPLODE, PLANT_BOMB
}
