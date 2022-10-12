package org.lee.serial.service;

public class AlreadyExistsException extends  RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }

}
