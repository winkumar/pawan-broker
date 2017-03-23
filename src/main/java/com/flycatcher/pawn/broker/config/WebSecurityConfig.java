package com.flycatcher.pawn.broker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.flycatcher.pawn.broker.security.JwtAuthenticationEntryPoint;
import com.flycatcher.pawn.broker.security.JwtAuthenticationTokenFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .servletApi().and()
                .anonymous().and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/auth/login","/api/v1/auth/refresh").permitAll()
                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.ttf",
                        "/**/*.woff",
                        "/**/*.json",
                        "/api-docs",
                        "/v2/api-docs",
                        "/webjars.*",
                        "/swagger-resources",
                        "/swagger-ui.html"
                ).permitAll()
                
                .anyRequest().authenticated();

        
        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
        
        
/*        
        
        http
        .csrf().disable()
        .headers().cacheControl().disable().and()
        .exceptionHandling().and()
        .servletApi().and()
        .anonymous().and()
        .authorizeRequests()

        //allow all static resources
        .antMatchers("/favicon.ico", "/js/**", "/template/**", "/bower_components/**").permitAll()

        //allow anonymous GETs to index, login, signup page
        .antMatchers(HttpMethod.GET, "/", "index.html", "/api/user/current").permitAll()

        //allow anonymous POSTs to login, signup
        .antMatchers(HttpMethod.POST, "/api/login", "/api/signup").permitAll()

        //authenticate all others
        .anyRequest().authenticated().and()

        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
        .addFilterBefore(new TokenAuthenticationProcessingFilter("/api/login", "POST", tokenAuthenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

        // custom Token based authentication based on the header previously given to the client
        .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);

        */
        
        
        
    }
}