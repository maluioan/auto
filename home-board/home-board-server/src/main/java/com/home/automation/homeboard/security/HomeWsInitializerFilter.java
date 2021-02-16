package com.home.automation.homeboard.security;

import com.home.automation.homeboard.websocket.config.BoardWsConfiguration;
import com.home.automation.homeboard.websocket.config.BoardWsConfigurationManager;
import org.apache.tomcat.websocket.server.UpgradeUtil;
import org.apache.tomcat.websocket.server.WsServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class HomeWsInitializerFilter extends GenericFilter {

    private static final Logger logger = LoggerFactory.getLogger(HomeWsInitializerFilter.class);

    private WsServerContainer wsServerContainer;

    private String token;

    private BoardWsConfigurationManager boardWsConfigurationManager;

    public void init() {
        wsServerContainer = (WsServerContainer)this.getServletContext().getAttribute("javax.websocket.server.ServerContainer");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!UpgradeUtil.isWebSocketUpgradeRequest(request, response)) {
            filterChain.doFilter(request, response);

        } else {
            performUpgrade(request, response);
        }
    }

    private void performUpgrade(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        final HttpServletRequest req = (HttpServletRequest)request;
        final HttpServletResponse res = (HttpServletResponse)response;

        // TODO; validate request with a token
        final Optional<BoardWsConfiguration> boardWsConfigurationOpt = findBoardWsConfiguration(req);
        if (boardWsConfigurationOpt.isPresent()) {
            logger.info("Upgrading request to web sockets");

            final BoardWsConfiguration boardWsConfiguration = boardWsConfigurationOpt.get();
            UpgradeUtil.doUpgrade(wsServerContainer, req, res, boardWsConfiguration.getServerEnpointConfig(), Collections.emptyMap());
        }
    }

    private Optional<BoardWsConfiguration> findBoardWsConfiguration(final HttpServletRequest request) {
        final String path = findPath(request);
        return boardWsConfigurationManager.findEndpointByPath(path);
    }

    private String findPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        // TODO: de ce e asa?
        if (pathInfo == null) {
            pathInfo = request.getServletPath();

        } else {
            pathInfo = request.getServletPath() + pathInfo;
        }

        return pathInfo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setBoardWsConfigurationManager(BoardWsConfigurationManager boardWsConfigurationManager) {
        this.boardWsConfigurationManager = boardWsConfigurationManager;
    }
}
