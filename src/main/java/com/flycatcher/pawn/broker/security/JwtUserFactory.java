package com.flycatcher.pawn.broker.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.flycatcher.pawn.broker.model.Role;
import com.flycatcher.pawn.broker.model.UserInfo;


public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(UserInfo user) {
//        return new JwtUser(
//                user.getUserId(),
//                user.getUserName(),
//                user.getFirstName(),
//                user.getLastName(),
//                //user.getEmail(),
//                user.getPassword(),
//                mapToGrantedAuthorities(user.getRoles()),
//                user.getIsEnable(),
//                user.getLastPasswordUpdate()
//        );
    	return null;
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(null))
                .collect(Collectors.toList());
    }
}
