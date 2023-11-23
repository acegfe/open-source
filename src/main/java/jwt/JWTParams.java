package jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTParams {
    private String username;
    private String password;
    private String issuer;
    private String secretKey;
    private String signKey;
    private String otherAuthorization;
    private String rootApi;
}


