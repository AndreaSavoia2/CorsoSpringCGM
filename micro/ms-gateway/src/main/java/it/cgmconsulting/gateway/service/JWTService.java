package it.cgmconsulting.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${application.security.token}")
    private String jwtSigningKey;

    public JwtUser extractJwtUSer(String token) {
        if(!isTokenExpired(token)) {
            try {
                Claims claim = extractAllClaims(token);
                return new JwtUser(claim.getSubject(), claim.get("role").toString());
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);

        if (expiration != null) {
            try {
                return expiration.before(new Date());
            } catch (ExpiredJwtException ex) {
                return true;
            }
        } else  {
            return true;
        }
    }

    private Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (SignatureException ex) {
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) throws SignatureException {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) throws SignatureException {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
