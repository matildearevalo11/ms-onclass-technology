package com.pragma.powerup.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessages {
    TECHNOLOGY_REQUIRED("Technology is required."),
    TECHNOLOGY_NAME_REQUIRED("Technology name is required."),
    TECHNOLOGY_DESCRIPTION_REQUIRED("Technology description is required."),
    TECHNOLOGY_NAME_TOO_LONG("The name must not exceed 50 characters."),
    TECHNOLOGY_DESCRIPTION_TOO_LONG("The description must not exceed 90 characters."),
    TECHNOLOGY_ALREADY_EXISTS("A technology with this name already exists."),
    INVALID_REQUEST("The request body is invalid."),
    AUTHENTICATION_REQUIRED("A valid token is required to access this resource."),
    ACCESS_DENIED("The user does not have permission to perform this action."),
    DATABASE_UNAVAILABLE("The persistence operation could not be completed."),
    INTERNAL_ERROR("The request could not be completed.");

    private final String message;
}
