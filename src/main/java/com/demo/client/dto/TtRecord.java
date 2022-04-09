package com.demo.client.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Time Tracker Record Dto")
public class TtRecord {
    @ApiModelProperty(value = "Start Time", required = true, example = "24.05.2002 05:00")
    String start;
    @ApiModelProperty(value = "End Time", required = true, example = "24.05.2002 13:00")
    String end;
    @NotEmpty
    @ApiModelProperty(value = "Email of Employee", required = true, example = "abc@def.com")
    String email;
}
