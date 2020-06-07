package com.sas.minesweeper.controller.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String status;
    private String message;

    public ErrorResponse(String message) {
        this.status = "error";
        this.message = message;
    }
}
