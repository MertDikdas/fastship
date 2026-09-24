package com.fastship.platformapi.exceptions;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(UUID id){
        super("Project with id " + id + " not found");
    }
}
