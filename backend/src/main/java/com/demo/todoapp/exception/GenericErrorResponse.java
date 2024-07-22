package com.demo.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericErrorResponse {
    private int statusCode;
    private String message;

    public GenericErrorResponse(String message) {
        this.message = message;
    }
}
