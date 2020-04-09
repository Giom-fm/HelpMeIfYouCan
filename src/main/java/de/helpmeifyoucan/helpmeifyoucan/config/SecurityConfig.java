package de.helpmeifyoucan.helpmeifyoucan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import de.helpmeifyoucan.helpmeifyoucan.handlers.ApiErrorHandler;
import de.helpmeifyoucan.helpmeifyoucan.security.filters.AuthenticationProcessingFilter;
import de.helpmeifyoucan.helpmeifyoucan.security.filters.JwtAuthorizationFilter;
import de.helpmeifyoucan.helpmeifyoucan.security.providers.EmailPasswordAuthenticationProvider;
import de.helpmeifyoucan.helpmeifyoucan.security.providers.JwtAuthenticationProvider;

@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private JwtAuthenticationProvider jwtAuthenticationProvider;
        private EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider;
        private ApiErrorHandler errorHandler;

        private AuthenticationFailureHandler failureHandler;

        @Autowired
        public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider,
                        EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider,
                        ApiErrorHandler errorHandler, AuthenticationFailureHandler failureHandler) {
                this.jwtAuthenticationProvider = jwtAuthenticationProvider;
                this.emailPasswordAuthenticationProvider = emailPasswordAuthenticationProvider;
                this.errorHandler = errorHandler;
                this.failureHandler = failureHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http
                                // Disbale Cors and CSRF
                                .cors().and().csrf().disable()
                                // Allow only signup
                                .authorizeRequests().antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                                // Make any other Endpoint only reachable with JWT-Token
                                .anyRequest().authenticated().and()
                                // Login Filter
                                .addFilterAt(this.authenticationProcessingFilter(),
                                                UsernamePasswordAuthenticationFilter.class)
                                // JWT Filter
                                .addFilterAt(this.jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                                // Disbale Session
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(this.jwtAuthenticationProvider)
                                .authenticationProvider(this.emailPasswordAuthenticationProvider);
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
                return source;
        }

        JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
                return new JwtAuthorizationFilter(this.authenticationManager(), this.errorHandler);
        }

        AuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
                return new AuthenticationProcessingFilter(this.authenticationManager(), this.jwtAuthenticationProvider,
                                this.failureHandler);
        }

}