package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "標準回傳格式")
@Data
@Builder
public class ResponseBean {
    @Schema(description = "錯誤代號")
    private String code;
    @Schema(description = "錯誤訊息")
    private String message;
    @Schema(description = "回傳內容")
    private Object data;
}
