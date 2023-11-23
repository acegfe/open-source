package jwt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create a JWT token.
 * @implNote Only supports HmacSHA256. (HS256)
 * @implNote Custom header type JWT
 */
public class JWTProvider {
    public static String create(JWTParams params) {
        final String secret = createSHA1Secret(params.getSecretKey(), params.getUsername(), params.getPassword());
        return JWTGenerator.createJwt(secret, params.getSignKey(), JWTGenerator.Algotype.HS256, params.getIssuer());
    }

    private static String createSHA1Secret(String secretKey, String username, String password) {
        String inputString = secretKey + username + password;

        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hash = sha1.digest(inputString.getBytes());

            StringBuilder hexHash = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            //It is not pretty, but it is something :)
            throw new RuntimeException(e);
        }
    }
}
