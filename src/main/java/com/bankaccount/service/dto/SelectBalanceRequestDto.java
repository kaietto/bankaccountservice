package com.bankaccount.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectBalanceRequestDto implements Serializable {
    @Serial private static final long serialVersionUID = 20230713_1999L;
    //@NotNull
    @JsonProperty(required = true, value="Numero conto di riferimento")
    private Long accountId;
}
