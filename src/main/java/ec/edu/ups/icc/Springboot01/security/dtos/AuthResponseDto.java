package ec.edu.ups.icc.Springboot01.security.dtos;

import java.util.Set;

public class AuthResponseDto {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String name;
    private String email;

    public AuthResponseDto(String token, Long userId, String name, String email) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}