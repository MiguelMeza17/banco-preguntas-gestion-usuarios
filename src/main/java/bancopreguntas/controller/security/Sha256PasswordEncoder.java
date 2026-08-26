package bancopreguntas.controller.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Implementación de {@link IPasswordEncoder} con SHA-256 y sal aleatoria
 * por usuario. SHA-256 puro (sin sal) es vulnerable a tablas rainbow: la
 * sal se genera por contraseña y viaja concatenada dentro del propio
 * {@code passwordHash} ("saltHex:hashHex"), así {@code matches} no necesita
 * un parámetro adicional y la interfaz {@link IPasswordEncoder} no cambia.
 */
public class Sha256PasswordEncoder implements IPasswordEncoder {

    private static final int LONGITUD_SAL_BYTES = 16;
    private static final String ALGORITMO = "SHA-256";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    @Override
    public String encode(String passwordPlano) {
        byte[] sal = new byte[LONGITUD_SAL_BYTES];
        RANDOM.nextBytes(sal);
        byte[] hash = hashConSal(passwordPlano, sal);
        return HEX.formatHex(sal) + ":" + HEX.formatHex(hash);
    }

    @Override
    public boolean matches(String passwordPlano, String passwordHash) {
        if (passwordPlano == null || passwordHash == null || !passwordHash.contains(":")) {
            return false;
        }
        String[] partes = passwordHash.split(":", 2);
        byte[] sal = HEX.parseHex(partes[0]);
        byte[] hashEsperado = HEX.parseHex(partes[1]);
        byte[] hashCandidato = hashConSal(passwordPlano, sal);
        return MessageDigest.isEqual(hashEsperado, hashCandidato);
    }

    private byte[] hashConSal(String passwordPlano, byte[] sal) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            digest.update(sal);
            return digest.digest(passwordPlano.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ALGORITMO + " no disponible en esta JVM.", ex);
        }
    }
}
