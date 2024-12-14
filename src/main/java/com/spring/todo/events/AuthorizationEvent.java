package com.spring.todo.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvent {

    @EventListener
    public void deniedEvent(AuthorizationDeniedEvent event) {
        log.info("Denied Authorization for user {} due to {}",
                event.getAuthentication().get().getPrincipal(),
                event.getAuthorizationDecision().toString());
    }
}
