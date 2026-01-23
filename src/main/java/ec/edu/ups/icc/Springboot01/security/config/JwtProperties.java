package ec.edu.ups.icc.Springboot01.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "mySecretKeyForJWT2024MustBeAtLeast256BitsLongForHS256Algorithm";
    private Long expiration = 1800000L; 
    private String header = "Authorization";
    private String prefix = "Bearer ";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public Long getExpiration() { return expiration; }
    public void setExpiration(Long expiration) { this.expiration = expiration; }
    public String getHeader() { return header; }
    public void setHeader(String header) { this.header = header; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
}