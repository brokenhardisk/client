package com.demo.client.controller;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.client.dto.TtRecord;
import com.demo.client.service.ApiConnectorService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class AppController {

    @Autowired
    private ApiConnectorService apiConnectorService;

    @GetMapping(value = "records")
    @ApiOperation(value = "Gets a list of all records",
        response = TtRecord.class, responseContainer = "List",
        produces = "application/json")
    public ResponseEntity<List<TtRecord>> getRecordList(@RequestParam(value = "email") @NotEmpty String email, @RequestParam(value = "length", defaultValue = "10") int length) {
        return ResponseEntity.ok(apiConnectorService.fetchEmpRecords(email, length));
    }

    @PostMapping(value = "records")
    @ApiOperation(value = "Adds a record to the existing list",
        consumes = "application/json", response = TtRecord.class,
        produces = "application/json")
    public ResponseEntity<TtRecord> addRecord(@ApiParam(value = "TimeTracking data") @RequestBody TtRecord item) {
        return new ResponseEntity<>(apiConnectorService.addEmpRecord(item), HttpStatus.CREATED);
    }
}
