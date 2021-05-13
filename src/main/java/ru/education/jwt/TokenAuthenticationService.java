package ru.education.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.education.security.SecurityUserDetailsManager;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Service
public class TokenAuthenticationService {

    private static final String SECRET = "Secret"; // ключ шифрования
    private static final long EXPIRATION_TIME = 864_000_000; // 10 дней, время через которое токен перестанет действовать
    private static final String TOKEN_PREFIX = "Bearer"; //
    private static final String HEDER_STRING = "Authorization"; // заголовок

    private static Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private static SecurityUserDetailsManager securityUserDetailsManager;

    @Autowired
    public TokenAuthenticationService(SecurityUserDetailsManager securityUserDetailsManager) {
        this.securityUserDetailsManager = securityUserDetailsManager;
    }

    static void addAuthentication(HttpServletResponse response, String username) {
        response.addHeader(HEDER_STRING, TOKEN_PREFIX + " " + generateToken(username));
    }

    static Authentication getAuthentication(HttpServletRequest request) {

        String token = getToken(request);

        if (!hasText(token)) {
            return null;
        }
        /* проверка наличия сессии по токену */
        String userName = getUsername(token);

        UserDetails user = securityUserDetailsManager.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                null, user.getAuthorities());
        return authentication;
    }

    private static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    private static String getToken(HttpServletRequest request) {
        if (request.getHeader(HEDER_STRING) != null) {
            return request.getHeader(HEDER_STRING).replace(TOKEN_PREFIX + " ", "");
        }

        return null;
    }

    public static String getUsername(String token) {
        try {
            return token != null ? Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject() : null;
        }  catch (JwtException e) {
            LOG.info("Ошибка обработки токена {}", token);
            return null;
        }
    }

    public static void main(String[] args) {
        String token;
        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTkyOTAxMjMxfQ.ab_nj11fwCgDJH12Df-Zt_Fdta_pT3pQK4tmF1EA7ly8IyD2dAYtIVk8u_20kl7G-BAlpuBIinKONRrvP8wXUA";
        System.out.println(token);
        String username;
        try {
            username = token != null ? Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject()
                     : null;
        }  catch (JwtException e) {
            LOG.info("Ошибка обработки токена {}", token);
            username = null;
        }
        System.out.println(username);
    }

}
