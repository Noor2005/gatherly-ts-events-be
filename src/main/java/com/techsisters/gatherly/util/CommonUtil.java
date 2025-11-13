package com.techsisters.gatherly.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

    public static String convertToJsonString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {

            log.error("Error while converting object to json string: ", e);
        }
        return "";
    }

}
