package es.uma.informatica.notificacion.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;


public class JwtUtils {

    /**
     * Extrae todos los atributos del token.
     *
     * @param bearerToken Token completo en formato String, con codificación BASE64.
     * @param secretKey Clave simétrica usada para la generacion del MAC del token.
     * @return Diccionario de atributos del token
     */
    public static Claims getClaims(String bearerToken, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Decoders.BASE64.decode(secretKey))
                .build()
                .parseClaimsJws(bearerToken)
                .getBody();
    }

    /**
     * Comprueba la validez del código HMAC contenido en el toquen (recalculado igual al recivido)
     *
     * @param secretKey Clave simétrica usada para la generacion del MAC del token.
     * @param plainToken Token completo en formato String, con codificación BASE64.
     * @return Diccionario de atributos del token
     */
    public static Jwt<?, ?> verifyToken(String secretKey, String plainToken) {
        return verifyToken(Decoders.BASE64.decode(secretKey), plainToken);
    }

    /**
     * Comprueba la validez del código HMAC contenido en el toquen (recalculado igual al recivido)
     *
     * @param secretKey Clave simétrica usada para la generacion del MAC del token.
     * @param plainToken Token completo en formato String, con codificación BASE64.
     * @return Diccionario de atributos del token
     */
    public static Jwt<?, ?> verifyToken(byte[] secretKey, String plainToken) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parse(plainToken);

    }
}
