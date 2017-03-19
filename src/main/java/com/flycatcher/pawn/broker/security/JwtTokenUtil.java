package com.flycatcher.pawn.broker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.flycatcher.pawn.broker.pojo.JwtAuthenticationResponse;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.access.secret}")
    private String accessSecret;
    
    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;
    
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public String getUsernameFromToken(String token,boolean isAccess) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token,isAccess);
            if(claims!=null)
            	username = claims.getSubject();
            else
            	username=null;
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token,boolean isAccess) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token,isAccess);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token,boolean isAccess) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token,isAccess);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token,boolean isAccess) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token,isAccess);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token,boolean isAccess) {
        Claims claims;
        try {
        	if(isAccess){
        		claims = Jwts.parser()
                    .setSigningKey(accessSecret)
                    .parseClaimsJws(token)
                    .getBody();
        	}else if(!isAccess){
        		claims = Jwts.parser()
                    .setSigningKey(refreshSecret)
                    .parseClaimsJws(token)
                    .getBody();
        	}else{
        		claims=null;
        	}
        } catch (Exception e) {
        	//e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate(boolean isAccess) {
    	if(isAccess){
    		return new Date(System.currentTimeMillis() + accessExpiration * 1000);
    	}else if(!isAccess){
    		return new Date(System.currentTimeMillis() + refreshExpiration * 1000);
    	}
    	return null;
    }

    private Boolean isTokenExpired(String token,boolean isAccess) {
        final Date expiration = getExpirationDateFromToken(token,isAccess);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token,boolean isAccess) {
        String audience = getAudienceFromToken(token,isAccess);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(boolean isAccess,UserDetails userDetails, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims,isAccess);
    }

    String generateToken(Map<String, Object> claims,boolean isAccess) {
    	if(isAccess){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(true))
                .signWith(SignatureAlgorithm.HS512, accessSecret)
                .compact();
    	}else if(!isAccess){
    		return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate(false))
                    .signWith(SignatureAlgorithm.HS512, refreshSecret)
                    .compact();
    	}
    	return null;
    }
    
    
    public JwtAuthenticationResponse generateAccessTokenAndRefreshToken(JwtUser jwtUser,Device device){
    	final String accessToken=generateToken(true,jwtUser,device);
    	final String refreshToken=generateToken(false,jwtUser,device);
    	if(accessToken!=null && refreshToken!=null){
    		return new JwtAuthenticationResponse(accessToken, refreshToken);
    	}
    	return null;
    	
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset,boolean isAccess) {
        final Date created = getCreatedDateFromToken(token,isAccess);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token,isAccess) || ignoreTokenExpiration(token,isAccess));
    }

/*    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token,false);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims,true);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }*/

    public Boolean validateToken(String token, UserDetails userDetails,boolean isAccess) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token,isAccess);
        final Date created = getCreatedDateFromToken(token,isAccess);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token,isAccess)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }
}