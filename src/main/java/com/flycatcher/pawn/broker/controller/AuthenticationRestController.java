package com.flycatcher.pawn.broker.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.flycatcher.pawn.broker.UrlPath;
import com.flycatcher.pawn.broker.model.UserInfo;
import com.flycatcher.pawn.broker.pojo.JwtAuthenticationResponse;
import com.flycatcher.pawn.broker.security.JwtAuthenticationRequest;
import com.flycatcher.pawn.broker.security.JwtTokenUtil;
import com.flycatcher.pawn.broker.security.JwtUser;
import com.flycatcher.pawn.broker.service.UserInfoService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(UrlPath.AUTH_PATH)
public class AuthenticationRestController {

    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody final JwtAuthenticationRequest authenticationRequest,final Device device) throws AuthenticationException {
    	System.out.println("--- create authenticate token ---");
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final JwtUser jwtUser =(JwtUser) this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        //final String token = jwtTokenUtil.generateToken(userDetails, device);
        
        //userDetails.g
        final UserInfo userInfo=this.userInfoService.getUserInfoUserName(jwtUser.getUsername());
        
        userInfo.setLastLogin(new Date());
        this.userInfoService.updateUserInfo(userInfo);
        
        final JwtAuthenticationResponse tokenObjet=this.jwtTokenUtil.generateAccessTokenAndRefreshToken(jwtUser, device);

        // Return the token
        return ResponseEntity.ok(tokenObjet);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Refresh-Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request,final Device device) {
        final String refreshToken = request.getHeader(refreshTokenHeader);
        final String userName = jwtTokenUtil.getUsernameFromToken(refreshToken,false);
        System.out.println("--- user name ---> "+userName);
        if(userName!=null){
        final JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(userName);

        if (jwtTokenUtil.canTokenBeRefreshed(refreshToken, user.getLastPasswordResetDate(),false)) {
        	final JwtAuthenticationResponse tokenObjet = jwtTokenUtil.generateAccessTokenAndRefreshToken(user,device);
            return ResponseEntity.ok(tokenObjet);
        }/* else {
            return ResponseEntity.badRequest().body(null);
        }*/
        }
        return ResponseEntity.badRequest().body(null);
    }

}
