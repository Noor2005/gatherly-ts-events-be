package com.techsisters.gatherly.integration.airtable.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponse {

    @JsonProperty("records")
    private List<Record> records;

}
