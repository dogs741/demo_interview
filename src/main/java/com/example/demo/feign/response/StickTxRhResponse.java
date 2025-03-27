package com.example.demo.feign.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StickTxRhResponse implements Serializable {
    private BigDecimal rh;
    private BigDecimal tx;
}
