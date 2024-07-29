package com.pettalk.jwt.filter;


import com.pettalk.jwt.token.JwtTokenizer;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer){
        this.jwtTokenizer = jwtTokenizer;

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            String username = (String) claims.get("username");
            String kakaoId = (String) claims.get("sub"); // 카카오 ID 추출
            if (username != null || kakaoId != null) {

                List<GrantedAuthority> authorities = new ArrayList<>();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username != null ? username : kakaoId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
//            String refreshToken = request.getHeader("Refresh-Token");
//            if (refreshToken != null) {
//                String newAccessToken = generateNewAccessToken(refreshToken);
//                if (newAccessToken != null) {
//                    // 클라이언트에게 새로운 Access Token을 반환합니다.
//                    response.setHeader("Authorization", newAccessToken);
//                }
//            }
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        log.error(request.getHeader("Authorization") + " jwt token");
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        log.error(jws + " jwt token");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
        return claims;
    }

    private String generateNewAccessToken(String refreshToken)     {
        // Refresh Token을 사용하여 새로운 Access Token을 생성하는 로직을 구현해야 합니다.
        // 예를 들어, 서버 내부에서 JWT 라이브러리를 사용하여 새로운 Access Token을 생성할 수 있습니다.
        // 또는 OAuth2 프로토콜을 사용하여 인증 서버에 요청하여 새로운 Access Token을 받아올 수도 있습니다.
        // 구체적인 방법은 프로젝트의 요구 사항과 사용하는 라이브러리 또는 프레임워크에 따라 다를 수 있습니다.

        // Access Token이 성공적으로 발급되면 해당 토큰을 반환합니다.
        return "새로운 Access Token";
    }
}
