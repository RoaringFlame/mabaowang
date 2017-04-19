package com.mabao.config;

import com.mabao.dao.repositories.UserRepository;
import com.mabao.util.filter.MyCharacterEncodingFilter;
import com.mabao.util.security.MaBaoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UserRepository userRepository;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(new MyCharacterEncodingFilter(),ChannelProcessingFilter.class);
    http.csrf().disable()
        .formLogin().loginPage("/login").defaultSuccessUrl("/");
    http.logout().logoutSuccessUrl("/");
    http.authorizeRequests().regexMatchers("/user/.*").authenticated()
            .regexMatchers("/pay").authenticated()
            .regexMatchers("/order/.*").authenticated()
            .regexMatchers("/cart/showCart").authenticated()
            .regexMatchers("/cart/changeNum/.*").authenticated()
            .regexMatchers("/cart/deleteGoods/.*").authenticated()
            .regexMatchers("/person/passwordChange").authenticated()
            .regexMatchers("/sell/releaseSelector").authenticated();
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(new MaBaoUserService(userRepository))
            .passwordEncoder(new Md5PasswordEncoder());
  }

}
