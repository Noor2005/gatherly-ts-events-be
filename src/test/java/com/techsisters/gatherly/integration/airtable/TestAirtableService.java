package com.techsisters.gatherly.integration.airtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.techsisters.gatherly.integration.airtable.response.ListResponse;
import com.techsisters.gatherly.integration.airtable.response.Record;
import com.techsisters.gatherly.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestAirtableService {

    @Autowired
    private AirtableService airtableService;

    @Test
    public void test_getList() {
        ListResponse response = airtableService.getList();
        log.info("Airtable Response: {}", CommonUtil.convertToJsonString(response));
    }

    @Test
    public void test_findByEmail() {
        Record record = airtableService.findByEmail("noorsuho@gmail.com");
        log.info("Airtable Record: {}", CommonUtil.convertToJsonString(record));
    }

}
