package com.team4.secureit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final Mailing mailing = new Mailing();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private Integer tokenExpirationSeconds;
        private Integer loginAttemptLimit;
        private Integer loginAttemptTimeWindow;
        private Integer lockoutDuration;
    }

    @Getter
    @Setter
    public static class Mailing {
        private String templatesLocation;
        private String senderAddress;
    }

    public Auth getAuth() {
        return auth;
    }

    public Mailing getMailing() { return mailing; }
}
