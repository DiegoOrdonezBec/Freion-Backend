package mx.edu.uttt.Freion.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(Date.from(Instant.now().plusSeconds(36000)))
                .compact();

    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean validateToken(String token){
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return true;
    }

}
