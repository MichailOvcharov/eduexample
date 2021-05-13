package ru.education.exceptions;

/*
   Исключение выбрасывается при конфликте с существующимим данными
 */
public class EntityConflictException extends BaseException {

    public EntityConflictException(String message) {
        super(message);
    }
}
