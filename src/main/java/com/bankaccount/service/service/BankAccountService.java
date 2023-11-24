package com.bankaccount.service.service;

import com.bankaccount.service.dto.*;
import com.bankaccount.service.entity.sqlitedb.AccountBalance;
import com.bankaccount.service.entity.sqlitedb.IdGen;
import com.bankaccount.service.enumeration.BusinessMessage;
import com.bankaccount.service.exception.BusinessException;
import com.bankaccount.service.repository.sqlitedb.AccountBalanceRepository;
import com.bankaccount.service.repository.sqlitedb.IdGenRepository;
import com.bankaccount.service.utility.JsonErrorUtility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Claudio Menin
 * @date 16/11/2023
 * Business Logics of Bank Account Service project
 */

@Log4j2
@Service
public class BankAccountService {

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;
    @Autowired
    private IdGenRepository idGenRepository;

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


    public String checkHealth() { return "Bank Account Service project is operational"; }

    /**
     * Generates a JWT token for DEMO purposes.
     * This method generates a JWT token with a hardcoded secret key.
     * It would be used by client applications to authenticate with the API.
     */
    public TokenResponseDto getToken (){
        TokenResponseDto token = new TokenResponseDto();
        token.setJwtToken("Bearer " + jwtDecoder(secret));
        return token;
    }

    public GetAccountBalanceOutputDto getAccountBalance (Long accountId){

        GetAccountBalanceOutputDto output = new GetAccountBalanceOutputDto();
        if (baseurl == null || baseurl.isEmpty()) baseurl = "https://sandbox.platfr.io";
        if (apikey == null || apikey.isEmpty()) apikey = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
        if (schema == null || schema.isEmpty()) schema = "S2S";

        String urlStr = baseurl + ACCOUNTS + accountId + "/balance";
        log.info("getAccountBalance API url: {}", urlStr);

        long startTime = System.currentTimeMillis();

        // Build HTTP headers and create HTTP request entity
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", schema);
        httpHeaders.set("Api-Key", apikey);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        // Avoid Spring encoding for URI
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // Set up RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // Define response entities for success and error cases
        ResponseEntity<GetAccountBalanceResponseDto> responseOk;
        ResponseEntity<ErrorDto> responseKo;

        try {
            responseOk = restTemplate.exchange(new URI(urlStr), HttpMethod.GET, request, GetAccountBalanceResponseDto.class);
            GetAccountBalanceResponseDto responseBody = responseOk.getBody();

            if ("OK".equals(Objects.requireNonNull(responseBody).getStatus())) {

                PayloadGetAccountBalanceResponseDto payload = responseBody.getPayload();
                log.info("Data: {}, Saldo: {}, Saldo disponibile: {}, Valuta: {}",
                        payload.getDate(), payload.getBalance(), payload.getAvailableBalance(), payload.getCurrency());

                // Saving to the database
                saveToAccountBalance(payload);

                output.setSaldo(payload.getBalance());
                return output;
            }
        } catch (ResourceAccessException ex) {
            // Handle timeout exception
            log.error(ex);
            throw new BusinessException(BusinessMessage.TIMEOUT, "Timeout during service" + urlStr + "call: " + ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("HTTP Client Error: {} - {}", HttpStatus.UNAUTHORIZED, ex.getMessage());
                throw new BusinessException(BusinessMessage.UNAUTHORIZED, ex.getMessage());
            } else {
                log.error(ex);
                // Extract description from the error response
                String errorResponseJson = ex.getResponseBodyAsString();
                String description = JsonErrorUtility.extractCodeAndDescription(errorResponseJson);
                throw new BusinessException(BusinessMessage.GENERICERROR, description);
            }
        } catch (URISyntaxException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.MALFORMEDURL, "Malformed URL: " + ex.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            long apiCallTime = endTime - startTime;
            log.info("getAccountBalance call time: {} ms", apiCallTime);
        }
        return output;
    }

    public CreateMoneyTransferOutputDto createMoneyTransfer(Long accountId) throws URISyntaxException {

        CreateMoneyTransferOutputDto output = new CreateMoneyTransferOutputDto();

        if (baseurl == null || baseurl.isEmpty()) baseurl = "https://sandbox.platfr.io";
        if (apikey == null || apikey.isEmpty()) apikey = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
        if (schema == null || schema.isEmpty()) schema = "S2S";

        String urlStr = baseurl + ACCOUNTS + accountId + "/payments/money-transfers";
        log.info("createMoneyTransfer API url: {}", urlStr);

        long startTime = System.currentTimeMillis();

        // Build HTTP headers and create HTTP request entity
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", schema);
        httpHeaders.set("Api-Key", apikey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        // Avoid Spring encoding for URI
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // Set up RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // Define response entities for success and error cases
        ResponseEntity<CreateMoneyTransferResponseDto> responseOk;
        ResponseEntity<ErrorDto> responseKo;
        try {
            log.info("Calling responseOk = restTemplate.exchange");
            responseOk = restTemplate.exchange(new URI(urlStr), HttpMethod.POST, request, CreateMoneyTransferResponseDto.class);
            CreateMoneyTransferResponseDto responseBody = responseOk.getBody();

            if ("OK".equals(Objects.requireNonNull(responseBody).getStatus())) {
                log.info("responseBody.getStatus() = {}", responseBody.getStatus());
                PayloadCreateMoneyTransferResponseDto payload = responseBody.getPayload();
                log.info("Data: {}, Saldo: {}, Saldo disponibile: {}, Valuta: {}",
                        payload.getAccountedDatetime(), payload.getAmount(), payload.getCreditor(), payload.getDebtor());
                // Saving to the database // TODO
                output.setEsito("OK");
                return output;
            } else {
                log.info("responseBody.getStatus() = {}", responseBody.getStatus());
                log.info("Calling responseKo = restTemplate.exchange");
                responseKo = restTemplate.exchange(new URI(urlStr), HttpMethod.POST, request, ErrorDto.class);
                ErrorDto errorBody = responseKo.getBody();
                log.error("Error response: {}", errorBody);
                throw new BusinessException(BusinessMessage.GENERICERROR,
                        errorBody == null ? "Errore generico" : errorBody.getErrors().get(0).getDescription());
            }
        } catch (HttpServerErrorException.InternalServerError ex) {
            log.error("Internal Server Error: {} - {}", ex.getStatusCode(), ex.getMessage());
            String errorResponseJson = ex.getResponseBodyAsString();
            String description = JsonErrorUtility.extractCodeAndDescription(errorResponseJson);
            log.error(ex);
            throw new BusinessException(BusinessMessage.GENERICERROR, description);
        } catch (ResourceAccessException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.TIMEOUT, "Timeout during service" + urlStr + "call: " + ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("HTTP Client Error: {} - {}", HttpStatus.UNAUTHORIZED, ex.getMessage());
                throw new BusinessException(BusinessMessage.UNAUTHORIZED, ex.getMessage());
            } else {
                // Extract description from the error response
                String errorResponseJson = ex.getResponseBodyAsString();
                String description = JsonErrorUtility.extractCodeAndDescription(errorResponseJson);
                log.error(ex);
                throw new BusinessException(BusinessMessage.GENERICERROR, description);
            }
        } catch (URISyntaxException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.MALFORMEDURL, "Malformed URL: " + ex.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            long apiCallTime = endTime - startTime;
            log.info("createMoneyTransfer call time: {} ms", apiCallTime);
        }
    }

    public GetCashAccountTransactionsOutputDto getCashAccountTransactions(Long accountId){

        GetCashAccountTransactionsOutputDto output = new GetCashAccountTransactionsOutputDto();

        if (baseurl == null || baseurl.isEmpty()) baseurl = "https://sandbox.platfr.io";
        if (apikey == null || apikey.isEmpty()) apikey = "FXOVVXXHVCPVPBZXIJOBGUGSKHDNFRRQJP";
        if (schema == null || schema.isEmpty()) schema = "S2S";
        //  Suggested dates to find transactions https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0
        String urlStr = baseurl + ACCOUNTS + accountId + "/transactions?fromAccountingDate=2022-01-01&toAccountingDate=2022-04-01";
        log.info("getCashAccountTransactions API url: {}", urlStr);

        long startTime = System.currentTimeMillis();

        // Build HTTP headers and create HTTP request entity
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Auth-Schema", schema);
        httpHeaders.set("Api-Key", apikey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Time-Zone", "Europe/Rome");

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        // Avoid Spring encoding for URI
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // Set up RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // Define response entities for success and error cases
        ResponseEntity<GetCashAccountTransactionsOutputDto> responseOk;

        try {
            responseOk = restTemplate.exchange(new URI(urlStr), HttpMethod.GET, request, GetCashAccountTransactionsOutputDto.class);
            log.info("responseOk = {}", responseOk);
            if (responseOk != null && responseOk.getBody() != null){
                output.setStatus(responseOk.getBody().getStatus());
                output.setPayload(responseOk.getBody().getPayload());
                output.setError(responseOk.getBody().getError());
            }

            log.info("Number of transactions: {}", output.getPayload().getList().size());
            output.getPayload().getList().forEach(transaction
                    -> log.info("TransactionId: {}", transaction.getTransactionId()));
            // Saving to the database // TODO
            return output;
        } catch (HttpServerErrorException.InternalServerError ex) {
            log.error("Internal Server Error: {} - {}", ex.getStatusCode(), ex.getMessage());
            String errorResponseJson = ex.getResponseBodyAsString();
            String description = JsonErrorUtility.extractCodeAndDescription(errorResponseJson);
            log.error(ex);
            throw new BusinessException(BusinessMessage.GENERICERROR, description);
        } catch (ResourceAccessException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.TIMEOUT, "Timeout during service" + urlStr + "call: " + ex.getMessage());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("HTTP Client Error: {} - {}", HttpStatus.UNAUTHORIZED, ex.getMessage());
                throw new BusinessException(BusinessMessage.UNAUTHORIZED, ex.getMessage());
            } else {
                // Extract description from the error response
                String errorResponseJson = ex.getResponseBodyAsString();
                String description = JsonErrorUtility.extractCodeAndDescription(errorResponseJson);
                log.error(ex);
                throw new BusinessException(BusinessMessage.GENERICERROR, description);
            }
        } catch (URISyntaxException ex) {
            log.error(ex);
            throw new BusinessException(BusinessMessage.MALFORMEDURL, "Malformed URL: " + ex.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            long apiCallTime = endTime - startTime;
            log.info("createMoneyTransfer call time: {} ms", apiCallTime);
        }
    }

    private static String jwtDecoder(String secret) {
        // [Claudio] Provide a dummy JWT (not suitable for Production, only for DEMO simplification purposes)
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

    /**
     * Saves account balance details to the database
     * based on the payload received from an account balance query.
     */
    public void saveToAccountBalance(PayloadGetAccountBalanceResponseDto payload) {

        AccountBalance accountBalance = new AccountBalance();

        // Generate a new ID for the new record (since SQLite doesn't support IDENTITY columns)
        Long newId = Math.max(1L, idGenRepository.findNextIdPlusOne("accountbalance"));
        accountBalance.setId(newId);

        log.info("payload.getDate() = {}", payload.getDate());
        accountBalance.setDate(payload.getDate());

        accountBalance.setBalance(payload.getBalance());
        accountBalance.setAvailableBalance(payload.getAvailableBalance());
        accountBalance.setCurrency(payload.getCurrency());

        // Check if account balance with same date already exists in the database table
        Optional<AccountBalance> existingBalance = accountBalanceRepository.findByDate(accountBalance.getDate());
        log.info("existingBalance = {}", existingBalance);

        // Update or insert
        if (existingBalance.isPresent()) {
            // Updates existing record if found, else saves new record
            AccountBalance existingRecord = existingBalance.get();
            existingRecord.setBalance(accountBalance.getBalance());
            existingRecord.setAvailableBalance(accountBalance.getAvailableBalance());
            existingRecord.setCurrency(accountBalance.getCurrency());
            log.info("updating existingRecord = {}", existingRecord);
            accountBalanceRepository.save(existingRecord);
        } else {
            // Save a new record
            log.info("saving accountBalance = {}", accountBalance);
            accountBalanceRepository.save(accountBalance);

            // Saving the new ID for the next record
            IdGen idGen = new IdGen("accountbalance", newId);
            log.info("saving idgen = {}", idGen);
            idGenRepository.save(idGen);
        }
    }

    /**
     * Saves account balance details to the database
     * based on the money transfer payload received.
     */
    public void saveToAccountBalance(PayloadCreateMoneyTransferResponseDto payload) {

        AccountBalance accountBalance = new AccountBalance();

        // Generate a new ID for the new record (SQLite doesn't support IDENTITY)
        Long newId = Math.max(1L, idGenRepository.findNextIdPlusOne("accountbalance"));
        accountBalance.setId(newId);

        log.info("payload.getDate() = {}", payload.getAccountedDatetime());
        accountBalance.setDate(payload.getAccountedDatetime());

        // accountBalance.setBalance(payload.getAmount().getDebtorAmount());
        // accountBalance.setAvailableBalance(payload.getAmount());
        accountBalance.setCurrency("EUR");

        // Check if account balance with same date already exists in the database table
        Optional<AccountBalance> existingBalance = accountBalanceRepository.findByDate(accountBalance.getDate());
        log.info("existingBalance = {}", existingBalance);

        // Update or insert
        if (existingBalance.isPresent()) {
            // Update existing record with the latest values
            AccountBalance existingRecord = existingBalance.get();
            existingRecord.setBalance(accountBalance.getBalance());
            existingRecord.setAvailableBalance(accountBalance.getAvailableBalance());
            existingRecord.setCurrency(accountBalance.getCurrency());
            log.info("updating existingRecord = {}", existingRecord);
            accountBalanceRepository.save(existingRecord);
        } else {
            // Save a new record if it doesn't exist
            log.info("saving accountBalance = {}", accountBalance);
            accountBalanceRepository.save(accountBalance);

            // Saving the new ID for the next record
            IdGen idGen = new IdGen("accountbalance", newId);
            log.info("saving idgen = {}", idGen);
            idGenRepository.save(idGen);
        }
    }
}

