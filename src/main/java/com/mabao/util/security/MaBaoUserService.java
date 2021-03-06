package com.mabao.util.security;

import com.mabao.dao.domain.User;
import com.mabao.dao.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity模块UserDetailsService接口实现
 * 从给定的UserRepository实现中查找用户
 */
public class MaBaoUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public MaBaoUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User mbuser = userRepository.findByName(username);
        if (mbuser != null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            UserInfo userInfo  = new UserInfo(mbuser.getName(),mbuser.getPassword(),authorities);
            userInfo.setUserId(mbuser.getId());
            userInfo.setEmail(mbuser.getEmail());
            userInfo.setPhone(mbuser.getPhone());
            userInfo.setPicture(mbuser.getPicture());
            userInfo.setCreateTime(mbuser.getCreateTime());
            return userInfo;
        }

        throw new UsernameNotFoundException(
                "User '" + username + "' not found.");
    }
}
