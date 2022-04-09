package com.demo.client.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.demo.client.dto.TtRecord;
import com.demo.client.service.ApiConnectorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AppControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ApiConnectorService apiConnectorService;

    private MockRestServiceServer mockServer;

    private static final String BASE_PATH = "http://localhost:8080/records";
    private static final String GET_PARAM = "?email=";
    private static final String GET_PARAM_DEFAULT_LENGTH = "&length=10";
    private static final String GET_EMAIL_ID = "foo@bar.com";
    private static final String GET_RESP_START = "24.05.2022T06:09:33.645";
    private static final String GET_RESP_END = "24.05.2022T06:14:33.645";
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void test_getRecord_OK() throws Exception {

        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(BASE_PATH + GET_PARAM + GET_EMAIL_ID + GET_PARAM_DEFAULT_LENGTH))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(mapper.writeValueAsString(getRecArr()), MediaType.APPLICATION_JSON));
        String contentBody = mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + GET_PARAM + GET_EMAIL_ID)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status()
                .isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        mockServer.verify();
        Assertions.assertTrue(StringUtils.hasLength(contentBody));
        List<TtRecord> responseList = mapper.readValue(contentBody, new TypeReference<List<TtRecord>>() {
        });
        Assertions.assertNotNull(responseList);
        Assertions.assertEquals(1, responseList.size());
        validateRecord(createTtRecord(), responseList.get(0));
    }

    @Test
    public void test_getRecord_ERR_NOT_FOUND() throws Exception {
        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(BASE_PATH + GET_PARAM + GET_EMAIL_ID + GET_PARAM_DEFAULT_LENGTH))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(mapper.writeValueAsString(getNullRecArr()), MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + GET_PARAM + GET_EMAIL_ID)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status()
                .isBadRequest());
        mockServer.verify();
    }

    @Test
    void test_createRecord_OK() throws Exception {
        TtRecord rec = createTtRecord();
        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(BASE_PATH))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators.withSuccess(mapper.writeValueAsString(rec), MediaType.APPLICATION_JSON));
        String contentBody = mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(rec)))
            .andExpect(MockMvcResultMatchers.status()
                .isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
        mockServer.verify();
        TtRecord response = mapper.readValue(contentBody, new TypeReference<TtRecord>() {});
        Assertions.assertNotNull(response);
        validateRecord(rec, response);
    }

    private void validateRecord(TtRecord expected, TtRecord actual) {
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
    }

    private TtRecord[] getRecArr() {
        return new TtRecord[] { createTtRecord() };
    }

    private TtRecord[] getNullRecArr() {
        return new TtRecord[] { null };
    }

    private TtRecord createTtRecord() {
        return TtRecord.builder()
            .email(GET_EMAIL_ID)
            .start(GET_RESP_START)
            .end(GET_RESP_END)
            .build();
    }

}

