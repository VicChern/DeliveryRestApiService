package com.softserve.itacademy.kek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAuthenticationService;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthController extends DefaultController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IAuthenticationService authenticationService;

    @GetMapping(path = "/login")
    protected void login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Performing login, request = {}", request);

        final String authorizeUrl = authenticationService.createRedirectUrl(request, response);

        try {
            logger.debug("trying to redirect to authorizeUrl = {}", authorizeUrl);
            response.sendRedirect(authorizeUrl);

        } catch (IOException e) {
            logger.error("Failed to redirect to authorizeUrl {}, {}", authorizeUrl, e);
        }
    }

    @RequestMapping(path = "/callback")
    protected void getCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Entered to getCallBack method req = {}", request);

        authenticationService.authenticateUser(request, response);
    }

    @GetMapping(path = "/profile")
    @PreAuthorize("hasRole('TENANT') or hasRole('USER') or hasRole('ACTOR')")
    protected ResponseEntity<String> profile(Authentication authentication) {

        final IUser user = (IUser) authentication.getPrincipal();

        return ResponseEntity.ok(user.toString());
    }


}