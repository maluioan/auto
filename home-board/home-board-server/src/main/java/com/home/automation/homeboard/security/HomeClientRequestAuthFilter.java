package com.home.automation.homeboard.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

// TODO: put this in a common place
public class HomeClientRequestAuthFilter extends GenericFilterBean {

    private final String token;

    public HomeClientRequestAuthFilter(final String clientId, final String password) {
        byte[] encode = Base64.getEncoder().encode(String.format("%sm:%sc", clientId, password).getBytes(StandardCharsets.ISO_8859_1));
        token = "Basic " + new String(encode);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

        final String authorization = httpServletRequest.getHeader("Authorization");
        if (Objects.isNull(authorization)) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please provide some credentials");

        } else if (!token.equals(authorization)) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());

        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
