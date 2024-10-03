package com.spring.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfiguration {

    // Commented to use JdbcUserDetailsManager
    /*
    @Bean
    public InMemoryUserDetailsManager configUserDetails() {
        Function<String, String> passwordEncoder = input -> passwordEncoder().encode(input);
        UserDetails userDetails1 = createNewUser(passwordEncoder, "Supriyo", "hello");
        UserDetails userDetails2 = createNewUser(passwordEncoder, "Harry", "Potter");
        return new InMemoryUserDetailsManager(userDetails1, userDetails2);
    }

    private UserDetails createNewUser(Function<String, String> passwordEncoder, String username, String password) {
        UserDetails userDetails = User.builder()
                .passwordEncoder(passwordEncoder)
                .username(username)
                .password(password)
                .roles("USER", "ADMIN")
                .build();
        return userDetails;
    }
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for H2 console access
                .authorizeHttpRequests()
                .requestMatchers("/todo/admin/**").hasRole("ADMIN")
                .requestMatchers("/todo/**").hasAnyRole("EMPLOYEE", "ADMIN", "MANAGER")
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console access
//                .requestMatchers("/login","/WEB-INF/jsp/**").permitAll()
//                .anyRequest().permitAll() // TODO: with custom login it needs permitAll + Admin View isn't working
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable() // Disable X-Frame-Options for H2 console
                .and()
                .formLogin()
                //                .loginPage("/login")
                .defaultSuccessUrl("/todo/list-todo", true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/todo/access-denied");

        return http.build();
    }

    @Bean
    UserDetailsManager userDetails(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        // query to retrieve user by username
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");

        // query to retrieve authorities/ roles by username
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, a.authority FROM user_authorities ua " +
                        "JOIN users u ON u.id = ua.user_id " +
                        "JOIN authorities a ON a.id = ua.authorities_id " +
                        "WHERE u.username = ?");
        return userDetailsManager;
    }

}