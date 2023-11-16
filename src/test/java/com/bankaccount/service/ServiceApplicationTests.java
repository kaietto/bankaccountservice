package com.bankaccount.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

	// @Test
	void testLetturaSaldo() {
		// TODO: Implement the test for saldo reading
		// API: https://docs.fabrick.com/platform/apis/gbs-banking-account-cash-v4.0
		// Input: {accountId}:Long (application parameter)
		// Output: Verify the correct display of the saldo
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
