package jwt.model;
import lombok.*;
import suppress.SuppressLombokCoverage;

@AllArgsConstructor
@Getter
@Setter
@SuppressLombokCoverage
public class JWTPayload {
    private String iss;
    private Long exp;
    private Long iat;
    private String sub;
    private String secret;
}
