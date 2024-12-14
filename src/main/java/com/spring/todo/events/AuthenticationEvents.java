package com.spring.todo.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEvents {

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent authentication) {
        log.info("Authentication success for user {}", authentication.getAuthentication().getName());
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
        log.info("Authentication failed for user {} due to {}",
                authenticationFailureEvent.getAuthentication().getName(),
                authenticationFailureEvent.getException().getMessage());
    }
}
