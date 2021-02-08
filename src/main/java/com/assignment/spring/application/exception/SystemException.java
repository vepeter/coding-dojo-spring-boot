package com.assignment.spring.application.exception;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 8684675082618769006L;

    public SystemException(String message) {
        super(message);
    }

}
