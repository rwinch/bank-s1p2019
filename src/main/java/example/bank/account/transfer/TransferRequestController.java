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

package example.bank.account.transfer;

import example.bank.account.Account;
import example.bank.account.AccountRepository;
import example.bank.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Rob Winch
 */
@Controller
@RequestMapping("/account/request")
public class TransferRequestController {

	final AccountRepository accounts;

	final TransferRequestRepository requests;

	public TransferRequestController(AccountRepository accounts,
			TransferRequestRepository requests) {
		this.accounts = accounts;
		this.requests = requests;
	}

	@GetMapping
	String request() {
		return "account/request";
	}

	@PostMapping
	String request(@ModelAttribute("currentUser") User currentUser, @ModelAttribute TransferRequestDto transfer, Map<String, Object> model) {
		Account from = this.accounts.findAccountByOwnerEmail(transfer.getFrom());
		if (from == null) {
			model.put("error", "User with email \"" + transfer.getFrom() + "\" not found");
			return "account/request";
		}
		Account to = this.accounts.findAccountByOwnerId(currentUser.getId());
		this.requests.save(new TransferRequest(transfer.getDescription(), to, from, transfer.getAmount()));
		return "redirect:/account";
	}

	@PostMapping("/approve")
	String approve(@RequestParam Long requestId) {
		TransferRequest request = this.requests.findById(requestId).orElse(null);
		double amount = request.getAmount();
		Account to = request.getTransferTo();
		Account from = request.getTransferFrom();
		to.setBalance(to.getBalance() + amount);
		from.setBalance(from.getBalance() - amount);
		this.accounts.save(to);
		this.accounts.save(from);
		this.requests.deleteById(requestId);
		return "redirect:/account";
	}
}
