package com.example.apigateway.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class HttpErrorInfo {

    private final ZonedDateTime timestamp;
    private final String path;
    private final HttpStatus httpStatus;
    private final String message;

    @JsonCreator // This annotation tells Jackson to use this constructor for deserialization
    public HttpErrorInfo(@JsonProperty("httpStatus") HttpStatus httpStatus,
                         @JsonProperty("path") String path,
                         @JsonProperty("message") String message) {
        this.timestamp = ZonedDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }
}
