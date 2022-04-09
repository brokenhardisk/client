package com.demo.client.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.demo.client.dto.TtRecord;
import com.demo.client.exception.ApplicationException;
import com.demo.client.service.impl.ApiConnectorServiceImpl;

@ExtendWith(SpringExtension.class)
public class ApiConnectorServiceTest {
    @Mock
    private RestTemplate restTemplate;
    private ApiConnectorService apiConnectorService;

    public static final String EMAIL = "testemail@dummy.com";
    public static final String START_TIME_STAMP = "testemail@dummy.com";
    public static final String END_TIME_STAMP = "testemail@dummy.com";
    public static final String APP_URL = "http://localhost:8080/records";

    @BeforeEach
    public void setUp() {
        apiConnectorService = new ApiConnectorServiceImpl(restTemplate, APP_URL);
        //ReflectionTestUtils.setField(apiConnectorService, "restTemplate", restTemplate);
    }

    @Test
    void testFetchEmpRecords_OK() {
        TtRecord expectedRecord = createTtRecord();
        Mockito.doReturn(createFetchEmpRecordsResponse())
            .when(restTemplate)
            .getForEntity(ArgumentMatchers.any(), ArgumentMatchers.any());
        List<TtRecord> actualRecord = apiConnectorService.fetchEmpRecords(EMAIL, 10);
        assertNotNull(actualRecord);
        assertEquals(actualRecord.size(), 1);
        validateRecord(expectedRecord, actualRecord.get(0));
    }

    @Test
    void testFetchEmpRecords_ConnectionRefused() {
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenThrow(new ResourceAccessException("Connection Refused"));
        Assertions.assertThrows(ResourceAccessException.class, () -> apiConnectorService.fetchEmpRecords(EMAIL, 10));
    }

    @Test
    void testFetchEmpRecords_NoResponse() {
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.any(), ArgumentMatchers.any()))
            .thenReturn(null);
        Assertions.assertThrows(ApplicationException.class, () -> apiConnectorService.fetchEmpRecords(EMAIL, 10));
    }

    @Test
    void testFetchEmpRecords_NoRecordExists() {
        Mockito.doReturn(createEmptyEmpRecordsResponse())
            .when(restTemplate)
            .getForEntity(ArgumentMatchers.any(), ArgumentMatchers.any());
        Assertions.assertThrows(ApplicationException.class, () -> apiConnectorService.fetchEmpRecords(EMAIL, 10));
    }

    @Test
    void testAddEmpRecords_OK() {
        TtRecord rec = createTtRecord();
        Mockito.doReturn(createAddEmpRecordResponse())
            .when(restTemplate)
            .postForEntity(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
        TtRecord actualRecord = apiConnectorService.addEmpRecord(rec);
        assertNotNull(actualRecord);
        validateRecord(rec, actualRecord);
    }

    private void validateRecord(TtRecord expected, TtRecord actual) {
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
    }

    private ResponseEntity<TtRecord[]> createFetchEmpRecordsResponse() {
        TtRecord[] recordsArr = new TtRecord[] { createTtRecord() };
        return ResponseEntity.ok(recordsArr);
    }

    private ResponseEntity<TtRecord[]> createEmptyEmpRecordsResponse() {
        TtRecord[] recordsArr = new TtRecord[] { null };
        return ResponseEntity.ok(recordsArr);
    }

    private ResponseEntity<TtRecord> createAddEmpRecordResponse() {
        return ResponseEntity.ok(createTtRecord());
    }

    private TtRecord createTtRecord() {
        return TtRecord.builder()
            .email(EMAIL)
            .start(START_TIME_STAMP)
            .end(END_TIME_STAMP)
            .build();
    }
}
