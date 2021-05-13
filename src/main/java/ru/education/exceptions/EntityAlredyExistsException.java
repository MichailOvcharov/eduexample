package ru.education.exceptions;

import org.springframework.util.Assert;

/*
  Исключение выбрасывается при повторном создании сущности с заданным ключом.
 */
public class EntityAlredyExistsException extends BaseException {

    public EntityAlredyExistsException(String message) {
        super(message);
    }

    public EntityAlredyExistsException(String type, Object id) {
        this(formatMessage(type, id));
    }

    public static String formatMessage(String type, Object id) {
        Assert.hasText(type, "Тип не может быть пустым.");
        Assert.notNull(id, "Идентификатор объекта не может быть null");
        Assert.hasText(id.toString(), "Идентификатор объекта не может быть пустым");
        return String.format("%s с ключом %s уже существует", type, id);
    }
}
