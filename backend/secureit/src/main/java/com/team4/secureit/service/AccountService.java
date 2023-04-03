package com.team4.secureit.service;

import com.team4.secureit.config.AppProperties;
import com.team4.secureit.dto.request.LoginRequest;
import com.team4.secureit.dto.request.LoginVerificationRequest;
import com.team4.secureit.dto.request.RegistrationRequest;
import com.team4.secureit.dto.request.VerificationRequest;
import com.team4.secureit.dto.response.LoginResponse;
import com.team4.secureit.exception.EmailAlreadyInUseException;
import com.team4.secureit.exception.EmailAlreadyVerifiedException;
import com.team4.secureit.exception.InvalidVerificationCodeException;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.Role;
import com.team4.secureit.model.User;
import com.team4.secureit.repository.UserRepository;
import com.team4.secureit.security.TokenAuthenticationFilter;
import com.team4.secureit.security.TokenProvider;
import com.team4.secureit.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private MailingService mailingService;

    private final SecureRandom random = new SecureRandom();

    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        Role role = ((User) authentication.getPrincipal()).getRole();
        Integer tokenExpirationSeconds = appProperties.getAuth().getTokenExpirationSeconds();
        CookieUtils.addCookie(
                response,
                TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                tokenExpirationSeconds
        );

        Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
        return new LoginResponse(accessToken, expiresAt, role);
    }

    public boolean verifyLogin(LoginVerificationRequest verificationRequest, User user) {
        return passwordEncoder.matches(verificationRequest.getPassword(), user.getPassword());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME);
    }

    public void registerPropertyOwner(RegistrationRequest registrationRequest) {
        checkEmailAvailability(registrationRequest.getEmail());

        PropertyOwner propertyOwner = new PropertyOwner();
        propertyOwner.setId(UUID.randomUUID());
        propertyOwner.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        propertyOwner.setRole(Role.ROLE_PROPERTY_OWNER);
        propertyOwner.setEmail(registrationRequest.getEmail());
        propertyOwner.setFirstName(registrationRequest.getFirstName());
        propertyOwner.setLastName(registrationRequest.getLastName());
        propertyOwner.setEmailVerified(false);
        propertyOwner.setVerificationCode(generateVerificationCode());

        userRepository.save(propertyOwner);
        mailingService.sendEmailVerificationMail(propertyOwner);
    }

    public void verifyEmail(VerificationRequest verificationRequest) {
        User userToVerify = userRepository.findByVerificationCode(verificationRequest.getCode()).orElseThrow(InvalidVerificationCodeException::new);

        if (userToVerify.isEmailVerified())
            throw new EmailAlreadyVerifiedException();

        userToVerify.setEmailVerified(true);
        userRepository.save(userToVerify);
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyInUseException();
    }

    private String generateVerificationCode() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
