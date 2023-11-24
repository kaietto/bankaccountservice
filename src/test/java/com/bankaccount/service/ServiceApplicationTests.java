package com.bankaccount.service;

import com.bankaccount.service.dto.*;
import com.bankaccount.service.repository.sqlitedb.AccountBalanceRepository;
import com.bankaccount.service.repository.sqlitedb.IdGenRepository;
import com.bankaccount.service.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceApplicationTests {

	@Mock
	private RestTemplate restTemplate;
	@Mock
	private AccountBalanceRepository accountBalanceRepository;
	@Mock
	private IdGenRepository idGenRepository;
	@InjectMocks
	private BankAccountService bankAccountService;
	@Autowired
	private MockMvc mockMvc;
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

	@Test
	public void testCreateMoneyTransfer() throws Exception {
		// Mock the behavior of the bankAccountService.createMoneyTransfer method
		CreateMoneyTransferOutputDto outputDto = new CreateMoneyTransferOutputDto();
		when(bankAccountService.createMoneyTransfer(anyLong())).thenReturn(outputDto);

		// Perform the POST request to the controller endpoint
		mockMvc.perform(MockMvcRequestBuilders.post("/createMoneyTransfer/{accountId}", 14537780)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.esito").value("OK"));
		// [ClaudioM] This test returns KO with error "REQ004-Invalid account identifier"
		// due to the fact that 14537780 for the sandbox API is not a valid account identifier
		// (see https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0)

		// other possible unit test for this service:
		// - testCreateMoneyTransfer_Success
		// - testCreateMoneyTransfer_Fail
		// - testCreateMoneyTransfer_Exception
		// - testCreateMoneyTransfer_Timeout
		// - testCreateMoneyTransfer_Unauthorized
		// - testCreateMoneyTransfer_InvalidAccountIdentifier
		// - testCreateMoneyTransfer_InvalidAmount
		// - testCreateMoneyTransfer_InvalidCurrency
		// - testCreateMoneyTransfer_InvalidDescription
		// - testCreateMoneyTransfer_InvalidExecutionDate
		// - testCreateMoneyTransfer_InvalidFeeType
		// - testCreateMoneyTransfer_InvalidFeeAccountId
		// - testCreateMoneyTransfer_InvalidFeeAmount
		// - testCreateMoneyTransfer_InvalidFeeCurrency
		// - so on...
	}

	@Test
	public void testGetCashAccountTransactions_Success() throws Exception {
		Long validAccountId = 14537780L;
		// Mock the bankAccountService object
		BankAccountService bankAccountService = mock(BankAccountService.class);

		// Stub the behavior of the bankAccountService.getCashAccountTransactions method
		GetCashAccountTransactionsOutputDto outputDto = new GetCashAccountTransactionsOutputDto();
		when(bankAccountService.getCashAccountTransactions(validAccountId)).thenReturn(outputDto);

		// Perform the GET request to the controller endpoint
		mockMvc.perform(MockMvcRequestBuilders.get("/getCashAccountTransactions/{accountId}", validAccountId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetCashAccountTransactions_Fail() throws Exception {
		Long invalidAccountId = 0L;
		// Mock the bankAccountService object
		BankAccountService bankAccountService = mock(BankAccountService.class);
		// Mock the behavior of the bankAccountService.getCashAccountTransactions method to return a failure scenario
		when(bankAccountService.getCashAccountTransactions(invalidAccountId)).thenReturn(null);

		// Perform the GET request to the controller endpoint and expect a failure status
		mockMvc.perform(MockMvcRequestBuilders.get("/getCashAccountTransactions/{accountId}", invalidAccountId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
