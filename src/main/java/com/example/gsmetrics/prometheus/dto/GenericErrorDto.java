package com.example.gsmetrics.prometheus.dto;

import lombok.Value;

@Value
public class GenericErrorDto {

    int code;

    String detail;

}
