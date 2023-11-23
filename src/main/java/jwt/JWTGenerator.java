package jwt;

import jwt.model.JWTHeader;
import jwt.model.JWTPayload;
import lombok.Getter;
import util.NotiendoEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static util.NotiendoEncoder.encode;
import static util.ThingMapper.asString;

class JWTGenerator {
    static String createJwt(String sha1Secret, String signKey, Algotype algorithm, String issuer) {
        Instant now = Instant.now();
        Instant exp = now.plus(120, ChronoUnit.SECONDS);

        JWTHeader jwtHeader = new JWTHeader(algorithm.name(), "JWT");
        JWTPayload jwtPayload = new JWTPayload(
                issuer,
                exp.getEpochSecond(),
                now.getEpochSecond(),
                "secret",
                sha1Secret
        );

        String b64Header = encode(asString(jwtHeader));
        String b64Payload = encode(asString(jwtPayload));
        String signature = createSignature(b64Header, b64Payload, signKey, algorithm.getValue());

        return String.format("%s.%s.%s", b64Header, b64Payload, signature);
    }

    private static String createSignature(String b64Header, String b64Payload, String signKey, String algorithm) {
        byte[] signatureBytes = sign(b64Header.getBytes(StandardCharsets.UTF_8),
                b64Payload.getBytes(StandardCharsets.UTF_8), signKey, algorithm);

        return NotiendoEncoder.encode((signatureBytes));
    }

    private static byte[] sign(byte[] headerBytes, byte[] payloadBytes, String signKey, String algorithm) {
        byte[] contentBytes = new byte[headerBytes.length + 1 + payloadBytes.length];

        System.arraycopy(headerBytes, 0, contentBytes, 0, headerBytes.length);
        contentBytes[headerBytes.length] = (byte) '.';
        System.arraycopy(payloadBytes, 0, contentBytes, headerBytes.length + 1, payloadBytes.length);

        try {

            final Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), algorithm));
            return mac.doFinal(contentBytes);

        } catch (Exception e) {
            //It is not pretty, but it is something :)
            throw new RuntimeException(e);
        }
    }

    @Getter
    enum Algotype {
        HS256("HmacSHA256");

        private final String value;

        Algotype(String cryptoMacType) {
            this.value = cryptoMacType;
        }
    }
}
