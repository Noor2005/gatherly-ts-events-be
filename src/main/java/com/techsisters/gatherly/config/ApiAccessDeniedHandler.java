package com.techsisters.gatherly.config;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.techsisters.gatherly.dto.ResponseDTO;
import com.techsisters.gatherly.util.CommonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    // 403: It's for users who are authenticated but lack the required role or
    // permission

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseDTO jsonResponse = new ResponseDTO();
        jsonResponse.setSuccess(false);
        jsonResponse.setMessage("Error: Access denied");

        // Write the JSON response to the response body
        response.getWriter().write(CommonUtil.convertToJsonString(jsonResponse));

    }

}
