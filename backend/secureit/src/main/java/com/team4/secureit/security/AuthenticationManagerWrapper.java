package com.team4.secureit.security;

import com.team4.secureit.config.AppProperties;
import com.team4.secureit.model.User;
import com.team4.secureit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@Service
@Transactional(value = REQUIRES_NEW, dontRollbackOn = {BadCredentialsException.class, LockedException.class})
public class AuthenticationManagerWrapper implements AuthenticationManager {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppProperties appProperties;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();

        // Invalid email => Bad credentials
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials."));

        try {
            Authentication retVal = authenticationManager.authenticate(authentication);

            // Valid email + valid password + locked => Lock reason
            if (user.isLocked())
                throw new LockedException(user.getLockReason());

            // Valid email + valid password => Authenticate
            user.setLoginAttempts(0);
            userRepository.save(user);
            return retVal;
        } catch (BadCredentialsException badCredentials) {
            // Valid email + invalid password + locked => Bad credentials
            if (user.isLocked())
                throw badCredentials;

            updateLoginTracking(user);
            userRepository.save(user);

            if (user.getLoginAttempts() >= appProperties.getAuth().getLoginAttemptLimit()) {
                user.lockAccount(appProperties.getAuth().getLockoutDuration(), ChronoUnit.MINUTES, "Too many failed login attempts.");
                userRepository.save(user);
            }

            // Valid email + invalid password => Bad credentials
            throw badCredentials;
        }
    }

    private void updateLoginTracking(User user) {
        Instant now = Instant.now();
        Instant windowStart = now.minus(appProperties.getAuth().getLoginAttemptTimeWindow(), ChronoUnit.MINUTES);

        // Reset the counter if there was no activity during the observed time window
        if (user.getLastLoginAttempt().isBefore(windowStart))
            user.setLoginAttempts(0);

        user.setLoginAttempts(user.getLoginAttempts() + 1);
        user.setLastLoginAttempt(now);
    }
}
