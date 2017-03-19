package com.flycatcher.pawn.broker.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.security.JwtTokenUtil;
import com.flycatcher.pawn.broker.security.JwtUser;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(UrlPath.USER_PATH)
public class UserRestController {

    @Value("${jwt.access.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value ="", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Access-Token", required = true, dataType = "string", paramType = "header")})
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token,true);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }

}
