package com.bankaccount.service;

import com.bankaccount.service.dto.GetAccountBalanceOutputDto;
import com.bankaccount.service.dto.GetAccountBalanceResponseDto;
import com.bankaccount.service.dto.PayloadGetAccountBalanceResponseDto;
import com.bankaccount.service.repository.sqlitedb.AccountBalanceRepository;
import com.bankaccount.service.repository.sqlitedb.IdGenRepository;
import com.bankaccount.service.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class ServiceApplicationTests {

	@Mock
	private RestTemplate restTemplate;
	@Mock
	private AccountBalanceRepository accountBalanceRepository;
	@Mock
	private IdGenRepository idGenRepository;
	@InjectMocks
	private BankAccountService bankAccountService;
	@BeforeEach
	void setUp() { MockitoAnnotations.openMocks(this); } //  [ClaudioM] Set up the test env and prepare required resources or dependencies

	@Test
	void testGetAccountBalance_Success() {
		// Mocking the response from the external API
		GetAccountBalanceResponseDto mockResponse = createMockResponse("OK", "2023-11-21", 1000.0, 800.0, "USD");
		ResponseEntity<GetAccountBalanceResponseDto> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

		// Mocking the restTemplate exchange method
		when(restTemplate.exchange(eq("https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/14537780/balance"),
				eq(HttpMethod.GET), any(HttpEntity.class), eq(GetAccountBalanceResponseDto.class)))
				.thenReturn(mockResponseEntity);

		// Mocking the existing balance check and the new ID generation
		when(accountBalanceRepository.findByDate(String.valueOf(any(Date.class)))).thenReturn(Optional.empty());
		when(idGenRepository.findNextIdPlusOne(eq("accountbalance"))).thenReturn(1L);

		// Performing the actual test
		GetAccountBalanceOutputDto result = bankAccountService.getAccountBalance(14537780L);

		// Assertions
		assertNotNull(result);
		assertEquals(-21.59, result.getSaldo());
		// [ClaudioM] Could be added more assertions based on the expected result

		// Verify that saveToAccountBalance is called
		// verify(bankAccountService, times(1)).saveToAccountBalance(any());
	}

	// [ClaudioM] Could be added more test methods for different scenarios (timeout, unauthorized, etc.)

	private GetAccountBalanceResponseDto createMockResponse(String status, String date, double balance, double availableBalance, String currency) {
		GetAccountBalanceResponseDto response = new GetAccountBalanceResponseDto();
		response.setStatus(status);
		PayloadGetAccountBalanceResponseDto payload = new PayloadGetAccountBalanceResponseDto();
		payload.setDate(date);
		payload.setBalance(balance);
		payload.setAvailableBalance(availableBalance);
		payload.setCurrency(currency);
		response.setPayload(payload);
		return response;
	}

	// @Test
	void testBonifico() {
		// TODO: Implement the test for bonifico operation
		// API: https://docs.fabrick.com/platform/apis/gbs-banking-payments-moneyTransfers-v4.0
		// Input: Populate all required fields (refer to documentation)
		// Output: Verify the operation status, handle the case of KO outcome as described
	}

	// @Test
	void testLetturaTransazioni() {
		// TODO: Implement the test for transazioni reading
		// API: https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0
		// Input: {accountId}:Long (application parameter); {fromAccountingDate} and {toAccountingDate} as indicated
		// Output: Verify the correct display of the list of transazioni
	}
}
