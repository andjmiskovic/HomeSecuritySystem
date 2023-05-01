package com.team4.secureit.service;

import com.team4.secureit.config.AppProperties;
import com.team4.secureit.dto.request.*;
import com.team4.secureit.dto.response.LoginResponse;
import com.team4.secureit.exception.*;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.Role;
import com.team4.secureit.model.User;
import com.team4.secureit.repository.UserRepository;
import com.team4.secureit.security.AuthenticationManagerWrapper;
import com.team4.secureit.security.TokenAuthenticationFilter;
import com.team4.secureit.security.TokenProvider;
import com.team4.secureit.util.CookieUtils;
import de.taimos.totp.TOTP;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AuthenticationManagerWrapper authenticationManager;

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
        User user = (User) authentication.getPrincipal();

        if (loginRequest.getCode() == null)
            throw new Missing2FaCodeException("Please provide 2FA code along with the credentials to authenticate.");

        if (!verify2FA(loginRequest.getCode(), user))
            throw new Invalid2FaCodeException("Invalid 2FA code.");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        Integer tokenExpirationSeconds = appProperties.getAuth().getTokenExpirationSeconds();
        CookieUtils.addCookie(
                response,
                TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                tokenExpirationSeconds
        );

        Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
        return new LoginResponse(accessToken, expiresAt, user.getRole());
    }

    public boolean verifyLogin(LoginVerificationRequest verificationRequest, User user) {
        return passwordEncoder.matches(verificationRequest.getPassword(), user.getPassword());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, TokenAuthenticationFilter.ACCESS_TOKEN_COOKIE_NAME);
    }

    public void registerPropertyOwner(RegistrationRequest registrationRequest) {
        checkEmailAvailability(registrationRequest.getEmail());

        PropertyOwner propertyOwner = populatePropertyOwner(registrationRequest);
        propertyOwner.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        propertyOwner.setRole(Role.ROLE_PROPERTY_OWNER);
        propertyOwner.lockAccount("Email address for this account has not been verified.");
        userRepository.save(propertyOwner);
        mailingService.sendEmailVerificationMail(propertyOwner);
    }

    public void verifyEmail(VerificationRequest verificationRequest) {
        User userToVerify = userRepository.findByVerificationCode(verificationRequest.getCode()).orElseThrow(InvalidVerificationCodeException::new);

        if (userToVerify.isEmailVerified())
            throw new EmailAlreadyVerifiedException();

        userToVerify.unlockAccount();
        userToVerify.setEmailVerified(true);
        userRepository.save(userToVerify);
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyInUseException();
    }

    public void createPropertyOwner(UserDetailsRequest createUserRequest) {
        checkEmailAvailability(createUserRequest.getEmail());
        PropertyOwner propertyOwner = populatePropertyOwner(createUserRequest);
        propertyOwner.setPasswordSet(false);
        userRepository.save(propertyOwner);
        mailingService.sendEmailVerificationMail(propertyOwner);
    }

    private PropertyOwner populatePropertyOwner(UserDetailsRequest createUserRequest) {
        PropertyOwner propertyOwner = new PropertyOwner();
        propertyOwner.setId(UUID.randomUUID());
        propertyOwner.setRole(Role.ROLE_PROPERTY_OWNER);
        propertyOwner.setEmail(createUserRequest.getEmail());
        propertyOwner.setFirstName(createUserRequest.getFirstName());
        propertyOwner.setLastName(createUserRequest.getLastName());
        propertyOwner.setEmailVerified(false);
        propertyOwner.setVerificationCode(generateVerificationCode());
        propertyOwner.setTwoFactorKey(generateTwoFactorKey());
        propertyOwner.setCity(createUserRequest.getCity());
        propertyOwner.setPhoneNumber(createUserRequest.getPhoneNumber());
        return propertyOwner;
    }

    public boolean isPasswordSet(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow(InvalidVerificationCodeException::new);

        if (user.isEmailVerified())
            throw new EmailAlreadyVerifiedException();
        return user.isPasswordSet();
    }

    public void setPassword(SetPasswordRequest setPasswordRequest) {
        User userToVerify = userRepository.findByVerificationCode(setPasswordRequest.getVerificationCode()).orElseThrow(InvalidVerificationCodeException::new);
        if (!setPasswordRequest.getPassword().equals(setPasswordRequest.getPasswordConfirmation()))
            throw new PasswordsDoNotMatchException();
        if (userToVerify.isEmailVerified())
            throw new EmailAlreadyVerifiedException();
        userToVerify.unlockAccount();
        userToVerify.setEmailVerified(true);
        userToVerify.setPassword(passwordEncoder.encode(setPasswordRequest.getPassword()));
        userToVerify.setPasswordSet(true);
        userRepository.save(userToVerify);
    }

    private String generateVerificationCode() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    private String generateTwoFactorKey() {
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    private String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    private boolean verify2FA(String code, User user) {
        return code.equals(getTOTPCode(user.getTwoFactorKey()));
    }

    private String generateGoogleAuthenticatorLink(User user) {
        String issuer = "Secure IT Inc.";
        String accountName = URLEncoder.encode(user.getFirstName() + " " + user.getLastName(), StandardCharsets.UTF_8);
        String secret = user.getTwoFactorKey();
        String algorithm = "SHA1";
        int digits = 6;
        int period = 30;

        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=%s&digits=%d&period=%d",
                issuer, accountName, secret, issuer, algorithm, digits, period);
    }
}
