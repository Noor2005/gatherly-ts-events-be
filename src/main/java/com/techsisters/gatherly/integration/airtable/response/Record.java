package com.techsisters.gatherly.integration.airtable.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Record {

    @JsonProperty("id")
    private String id;

    @JsonProperty("createdTime")
    private String createdTime;

    @JsonProperty("fields")
    private Field fields;

}
