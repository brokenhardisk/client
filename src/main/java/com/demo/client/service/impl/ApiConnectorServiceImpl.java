package com.demo.client.service.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.client.dto.TtRecord;
import com.demo.client.exception.ApplicationException;
import com.demo.client.service.ApiConnectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ApiConnectorServiceImpl implements ApiConnectorService {
    private final RestTemplate restTemplate;
    private final String timeTrackerApplicationUri;

    @Override
    public List<TtRecord> fetchEmpRecords(String email, int length) {
        List<TtRecord> results;
        //results=(List.of(TtRecord.builder().email("abc@gmail.com").start("28.11.2016 08:00").end("28.11.2016 09:00").build()));
        URI uri = UriComponentsBuilder.fromHttpUrl(timeTrackerApplicationUri)
            .queryParam("email", email)
            .queryParam("length", length)
            .build()
            .toUri();
		log.info("Accessing URL: {}",uri);	
        ResponseEntity<TtRecord[]> response = restTemplate.getForEntity(uri, TtRecord[].class);
        if (response != null) {
            if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 300) {
                TtRecord[] respBody = response.getBody();
                results = Arrays.stream(respBody)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            } else {
                log.error(response.toString());
                throw new ApplicationException("Error while fetching from server");
            }
        } else {
            throw new ApplicationException("No response received from server");
        }
        if (results == null || results.isEmpty()) {
            throw new ApplicationException("No records found for " + email);
        }
        return results;
    }

    @Override
    public TtRecord addEmpRecord(TtRecord record) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> reqMap = new LinkedMultiValueMap<>();
        reqMap.add("email", record.getEmail());
        reqMap.add("start", record.getStart());
        reqMap.add("end", record.getEnd());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(reqMap, httpHeaders);
        URI uri = UriComponentsBuilder.fromHttpUrl(timeTrackerApplicationUri)
            .build()
            .toUri();
		log.info("Accessing URL: {}",uri);	
        ResponseEntity<TtRecord> response = restTemplate.postForEntity(uri, request, TtRecord.class);
        if (response != null) {
            if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() < 300) {
                return response.getBody();
            } else {
                log.error(response.toString());
                throw new ApplicationException("Record Not Added");
            }
        } else {
            throw new ApplicationException("No response received from server");
        }
    }
}
