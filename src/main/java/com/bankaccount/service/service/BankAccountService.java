package com.bankaccount.service.service;

import com.bankaccount.service.config.SecurityConfig;
import com.bankaccount.service.dto.*;
import com.bankaccount.service.enumeration.BusinessMessage;
import com.bankaccount.service.exception.BusinessException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Claudio Menin
 * @date 16/11/2023
 * Business Logics of Bank Account Service project
 */

@Log4j2
@Service
public class BankAccountService {

    @Value("${endpoints.baseurl}")
    private String baseurl;
    @Value("${endpoints.apikey}")
    private String apikey;
    @Value("${endpoints.schema}")
    private String schema;
    @Value("${jwt-secret}")
    private String secret;

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "bank-account-service-api";
    public static final String TOKEN_AUDIENCE = "bank-account-service-app";
    public static final String ACCOUNTS = "/api/gbs/banking/v4.0/accounts/";


    public String checkHealth() {
        return "Bank Account Service project is operational";
    }

    public TokenResponseDto getToken (){
        TokenResponseDto token = new TokenResponseDto();
        token.setJwtToken("Bearer " + jwtDecoder(secret));
        return token;
    }
    public GetAccountBalanceOutputDto getAccountBalance (Long accountId){
        GetAccountBalanceOutputDto output = new GetAccountBalanceOutputDto();
        String urlStr = baseurl + ACCOUNTS + accountId + "/balance";
        log.info("getAccountBalance API url: {}", urlStr);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", schema);
        httpHeaders.set("Api-Key", apikey);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        // avoid spring encoding
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<GetAccountBalanceResponseDto> responseOk;
        ResponseEntity<ErrorDto> responseKo;
        try {
            responseOk = restTemplate.exchange(urlStr, HttpMethod.GET, request, GetAccountBalanceResponseDto.class);
            GetAccountBalanceResponseDto responseBody = responseOk.getBody();
            if ("OK".equals(Objects.requireNonNull(responseBody).getStatus())) {
                PayloadGetAccountBalanceResponseDto payload = responseBody.getPayload();
                log.info("Data: {}, Saldo: {}, Saldo disponibile: {}, Valuta: {}",
                        payload.getDate(), payload.getBalance(), payload.getAvailableBalance(), payload.getCurrency());
                output.setSaldo(payload.getBalance());
                return output;
            } else {
                responseKo = restTemplate.exchange(urlStr, HttpMethod.GET, request, ErrorDto.class);
                ErrorDto errorResponse = responseKo.getBody();
                if (errorResponse != null && "KO".equals(errorResponse.getStatus())) {
                    String code = errorResponse.getErrors().get(0).getCode();
                    String description = errorResponse.getErrors().get(0).getDescription();
                    throw new BusinessException(BusinessMessage.INVALID_REQUEST, "Errore nella richiesta: " + code + "-" + description);
                } else {
                    log.error("Risposta inaspettata dalla chiamata API quando lo stato è diverso da 'OK': {}", responseKo.getBody());
                    throw new BusinessException(BusinessMessage.UNEXPECTED_RESPONSE, "Risposta inaspettata dalla chiamata API quando lo stato è diverso da 'OK'");
                }
            }
        } catch (ResourceAccessException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.TIMEOUT, "Timeout during service" + urlStr + "call: " + ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("HTTP Client Error: {} - {}", HttpStatus.UNAUTHORIZED, ex.getMessage());
            } else {
                log.error(ex);
                // Extract description from the error response
                String errorResponseJson = ex.getResponseBodyAsString();
                String description = extractDescription(errorResponseJson);
                throw new BusinessException(BusinessMessage.GENERICERROR, description);
            }
        }
        return null;
    }

    private String extractDescription(String errorResponseJson) {
        String descriptionKey = "\"description\":\"";
        int descriptionIndex = errorResponseJson.indexOf(descriptionKey);
        if (descriptionIndex != -1) {
            int startIndex = descriptionIndex + descriptionKey.length();
            int endIndex = errorResponseJson.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return errorResponseJson.substring(startIndex, endIndex);
            }
        }
        return "Unknown error description";
    }

    public static String jwtDecoder(String secret) {
        // Claudio: Provide a dummy JWT (not suitable for Production, only for DEMO simplification purposes)
        Instant expirationInstant = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject("Paolo Rossi")
                .setExpiration(Date.from(expirationInstant))
                .compact();
    }

}

