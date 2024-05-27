package com.senai.labfoods.security.authentication;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.senai.labfoods.exception.AccessDeniedException;
import com.senai.labfoods.exception.AuthorizationFailedException;
import com.senai.labfoods.model.User;
import com.senai.labfoods.repository.UserRepository;
import com.senai.labfoods.security.config.SecurityConfiguration;
import com.senai.labfoods.security.userdetails.UserDetailsImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (checkIfEndpointIsNotPublic(request)) {
                String token = recoveryToken(request);
                if (token != null) {
                    String subject = jwtTokenService.getSubjectFromToken(token);
                    User user = userRepository.findByEmail(subject).get();
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                            null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new AccessDeniedException("O token está ausente.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthorizationFailedException e) {
            handleException(response, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (AccessDeniedException e) {
            handleException(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            handleException(response, "Erro interno do servidor.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"erro\": \"" + message + "\"}");
    }

}