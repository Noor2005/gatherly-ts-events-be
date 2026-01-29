package com.techsisters.gatherly.response;

import java.util.List;

import com.techsisters.gatherly.dto.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse extends ResponseDTO {

    private String accessToken;
    private String name;
    private String email;
    private String country;
    private List<String> roles;

}
