package ru.education.exceptions;

/*
  Исключение выбрасывается при вызове метода сервиса с некоректным параметрами
 */
public class EntityIllegalArgumentException extends BaseException {

    public EntityIllegalArgumentException(String message) {
        super(message);
    }
}
