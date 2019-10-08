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

package example.bank.user;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import static java.util.Optional.ofNullable;

/**
 * @author Rob Winch
 */
@ControllerAdvice
public class UserControllerAdvice {
	final UserRepository users;

	public UserControllerAdvice(UserRepository users) {
		this.users = users;
	}

	@ModelAttribute("currentUser")
	User currentUser(
		@AuthenticationPrincipal Object principal,
		@SessionAttribute(name = "USER", required = false) Long id) {
		if (principal instanceof AuthenticatedPrincipal) {
			AuthenticatedPrincipal p = (AuthenticatedPrincipal) principal;
			User user = this.users.findByEmail(p.getName());
			return user;
		}
		return this.users.findById(ofNullable(id).orElse(1L)).orElse(null);
	}
}
