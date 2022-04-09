package com.demo.client.service;

import com.demo.client.dto.TtRecord;

import java.util.List;

public interface ApiConnectorService {

    List<TtRecord> fetchEmpRecords(String email,int length);

    TtRecord addEmpRecord(TtRecord record);

}
