package com.bankaccount.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230713_1989L;
    private String jwtToken; // Bearer token -> "Bearer ey..."
}
