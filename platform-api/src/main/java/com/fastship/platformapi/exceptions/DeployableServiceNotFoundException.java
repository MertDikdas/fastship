package com.fastship.platformapi.exceptions;

import java.util.UUID;

public class DeployableServiceNotFoundException extends RuntimeException{
    public DeployableServiceNotFoundException(UUID serviceId) {
        super("Deployable Service Not Found with ID: " + serviceId.toString());
    }
}
