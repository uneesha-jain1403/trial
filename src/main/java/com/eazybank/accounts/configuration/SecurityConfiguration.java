package com.eazybank.accounts.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    DataSource dataSource;          // h2 Database

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                request -> request
                        .requestMatchers("/api/logout").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/greet").permitAll()
                        .anyRequest().authenticated()
        );
        httpSecurity.logout(logout -> logout
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/logout"));
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.headers(headers -> headers.frameOptions(options -> options.sameOrigin()));
        httpSecurity.csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails monica = User.withUsername("monica").password("{noop}monica123").roles("MANAGER").build();
//        UserDetails joey = User.withUsername("joey").password("{noop}joey123").roles("ADMIN").build();
//        UserDetails ross = User.withUsername("ross").password("{noop}ross123").roles("MANAGER", "ADMIN").build();
//        return new InMemoryUserDetailsManager(monica, joey, ross);
//    }


    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails monica = User.withUsername("monica").password(passwordEncoder().encode("monica123")).roles("MANAGER").build();
        UserDetails joey = User.withUsername("joey").password(passwordEncoder().encode("joey123")).roles("ADMIN").build();
        UserDetails ross = User.withUsername("ross").password(passwordEncoder().encode("ross123")).roles("MANAGER", "ADMIN").build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.createUser(monica);
        jdbcUserDetailsManager.createUser(joey);
        jdbcUserDetailsManager.createUser(ross);
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder encoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);

    }
}