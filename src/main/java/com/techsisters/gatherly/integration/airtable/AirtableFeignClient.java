package com.techsisters.gatherly.integration.airtable;

import com.techsisters.gatherly.integration.airtable.response.ListResponse;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface AirtableFeignClient {

    @RequestLine("GET /{baseId}/{tableName}")
    @Headers({ "Authorization: Bearer {bearerAuth}" })
    ListResponse getList(@Param("bearerAuth") String bearerAuth, @Param("baseId") String baseId,
            @Param("tableName") String tableName);
}
