package com.techsisters.gatherly.response;

import com.techsisters.gatherly.dto.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpResponse extends ResponseDTO {
    private Integer otp;

}
