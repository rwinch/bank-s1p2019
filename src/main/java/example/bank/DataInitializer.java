/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.bank;

import example.bank.account.Account;
import example.bank.account.AccountRepository;
import example.bank.account.transfer.TransferRequestRepository;
import example.bank.account.transfer.TransferRequest;
import example.bank.user.User;
import example.bank.user.UserRepository;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * @author Rob Winch
 */
@Component
public class DataInitializer implements SmartInitializingSingleton {
	private final UserRepository users;

	private final AccountRepository accounts;

	private final TransferRequestRepository requests;

	public DataInitializer(UserRepository users, AccountRepository accounts,
			TransferRequestRepository requests) {
		this.users = users;
		this.accounts = accounts;
		this.requests = requests;
	}

	@Override
	public void afterSingletonsInstantiated() {
		User ria = this.users
				.save(new User(1L, "Ria", "Stein", "ria@example.com", "password"));
		User rob = this.users
				.save(new User(2L, "Rob", "Winch", "rob@example.com", "password"));

		Account riaAccount = this.accounts.save(new Account(ria, 1_000_000.00));
		Account robAccount = this.accounts.save(new Account(rob, 10.00));

		TransferRequest request = new TransferRequest("I'm broke", robAccount, riaAccount, 100);
		this.requests.save(request);
	}
}
