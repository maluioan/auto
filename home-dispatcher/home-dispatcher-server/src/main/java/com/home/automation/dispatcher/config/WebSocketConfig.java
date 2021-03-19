package com.home.automation.dispatcher.config;

import com.home.automation.dispatcher.client.data.TokenData;
import com.home.automation.dispatcher.service.TokenService;
import com.sun.security.auth.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private TokenService tokenService;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.enableSimpleBroker("/command/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/client/message")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
        registry.addEndpoint("/board/message");
    }

    private class CustomHandshakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            Principal principal = null;

            final String dispatcherToken = findDispathcerToken(request);
            if (StringUtils.isNotBlank(dispatcherToken)) {
                final TokenData tokenData = tokenService.findToken(dispatcherToken);
                principal = StringUtils.isNotBlank(tokenData.getUserName()) ? new UserPrincipal(tokenData.getUserName()) : null;
            }
            return principal;
        }

        private String findDispathcerToken(ServerHttpRequest request) {
            final HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            return servletRequest.getParameter("dtk");
        }
    }
}