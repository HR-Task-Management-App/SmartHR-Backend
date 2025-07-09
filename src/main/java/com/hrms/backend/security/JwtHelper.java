package com.hrms.backend.security;

import com.hrms.backend.entities.User;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private UserRepository userRepository;

    private static final String CLAIM_USERID = "userId";
    private static final String CLAIM_ROLE = "role";

    public static final long TOKEN_VALIDITY = 5*24*60*60*1000; // 5 days in ms

//    //retrieve username(email here) from token
//    public String getUsernameFromToken(String token){
//       String userId = getUserIdFromToken(token);
//       User user = userRepository.findByUserId(userId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
//       return user.getUsername();
//    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims ->  claims.get(CLAIM_USERID,String.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Set the signing key
                .build() // Build the JwtParser
                .parseSignedClaims(token) // Parse the token
                .getPayload(); // Retrieve the Claims
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    //put claims to token // also it return token
    public String generateToken(UserDetails userDetails, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_ROLE,role);
        // Cast to your actual User object to get userId
        if (userDetails instanceof User user) {
            claims.put(CLAIM_USERID, user.getId());
        } else {
            throw new IllegalArgumentException("Expected UserDetails to be instance of User");
        }
        return doGenerateToken(claims);
    }


    private String doGenerateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String userId = getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadApiRequestException("User not found"));
        String username = user.getUsername();
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


}
