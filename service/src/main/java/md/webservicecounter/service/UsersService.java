/*
 * The MIT License
 *
 * Copyright 2022 Marcin Drozdziński.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package md.webservicecounter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.webservicecounter.exception.RegistrationException;
import md.webservicecounter.model.type.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    public InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public void register(
            String username,
            String password
    ) {
        log.info("Registering new user...");
        if (!inMemoryUserDetailsManager.userExists(username)) {
            inMemoryUserDetailsManager.createUser(
                    User
                            .withUsername(username)
                            .password("{noop}" + password)
                            .roles(UserRole.USER.name())
                            .build());
            log.info("New user registered");
        } else {
            log.warn("Cannot create user, because it already exists");
            throw new RegistrationException();
        }
    }
}
