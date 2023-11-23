package jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import suppress.SuppressLombokCoverage;

@AllArgsConstructor
@Getter
@Setter
@SuppressLombokCoverage
public class JWTHeader {
    private String alg;
    private String typ;
}
