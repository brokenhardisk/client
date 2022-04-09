package com.demo.client.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Generic Error Message", description = "A generic error message in case of failures.")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessageDto {

    @ApiModelProperty(value = "The error message.")
    private String message;

    @ApiModelProperty(value = "The field.")
    private String field;

}
