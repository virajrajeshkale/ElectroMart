package com.pro.electronic.store.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends  RuntimeException{

    public  ResourceNotFoundException()
    {
        super("Resource Not found..!!");
    }

    public  ResourceNotFoundException(String message)
    {
        super(message);
    }
}
