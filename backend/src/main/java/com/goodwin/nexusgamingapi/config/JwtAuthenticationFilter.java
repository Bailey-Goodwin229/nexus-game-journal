package com.goodwin.nexusgamingapi.config;

import com.goodwin.nexusgamingapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Calls JwtService and UserDetailsService
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Intercepts users and confirms if user is holding a valid JWT Keycard
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;
        String username = null;

        // 1 & 2. Look for the "nexus_token" cookie instead of the Authorization header
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("nexus_token".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }


        // 3. If no token found in cookies, move to the next filter
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        username = jwtService.extractUsername(jwt);

        // 4. If we have a username and the user isn't "Logged in" yet...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. check if the "Keycard" is valid and not expired
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Officially tell Spring: "This person is allowed in!"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response); // Ensures the quest is pushed along if accepted, if not it kicks them out (Denies)
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // EXPLICIT FIX: Bypass this token filter entirely for ANY browser preflight checks
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String path = request.getServletPath();
        // ONLY skip the filter for public authentication onboarding routes
        return path.equals("/api/auth/login") || path.equals("/api/auth/register");
    }
}
