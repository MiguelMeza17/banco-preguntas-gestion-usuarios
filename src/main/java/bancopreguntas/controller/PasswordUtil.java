package bancopreguntas.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Cifra y verifica contraseñas con SHA-256 y sal aleatoria por usuario.
 * La sal viaja concatenada dentro del propio hash ("saltHex:hashHex").
 */
public class PasswordUtil {

    private static final int LONGITUD_SAL_BYTES = 16;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    private PasswordUtil() {
    }

    public static String encode(String passwordPlano) {
        byte[] sal = new byte[LONGITUD_SAL_BYTES];
        RANDOM.nextBytes(sal);
        byte[] hash = hashConSal(passwordPlano, sal);
        return HEX.formatHex(sal) + ":" + HEX.formatHex(hash);
    }

    public static boolean matches(String passwordPlano, String passwordHash) {
        if (passwordPlano == null || passwordHash == null || !passwordHash.contains(":")) {
            return false;
        }
        String[] partes = passwordHash.split(":", 2);
        byte[] sal = HEX.parseHex(partes[0]);
        byte[] hashEsperado = HEX.parseHex(partes[1]);
        byte[] hashCandidato = hashConSal(passwordPlano, sal);
        return MessageDigest.isEqual(hashEsperado, hashCandidato);
    }

    private static byte[] hashConSal(String passwordPlano, byte[] sal) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(sal);
            return digest.digest(passwordPlano.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
