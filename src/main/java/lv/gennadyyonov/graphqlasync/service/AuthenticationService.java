package lv.gennadyyonov.graphqlasync.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private static final String ANONYMOUS = "ANONYMOUS";

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(Authentication::getName)
                .map(String::toUpperCase)
                .orElse(ANONYMOUS);
    }
}
