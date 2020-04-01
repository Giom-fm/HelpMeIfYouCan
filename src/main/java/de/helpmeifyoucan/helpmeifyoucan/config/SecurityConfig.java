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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import de.helpmeifyoucan.helpmeifyoucan.security.AuthenticationProcessingFilter;
import de.helpmeifyoucan.helpmeifyoucan.security.EmailPasswordAuthenticationProvider;
import de.helpmeifyoucan.helpmeifyoucan.security.JwtAuthenticationProvider;
import de.helpmeifyoucan.helpmeifyoucan.security.JwtAuthorizationFilter;

@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider;

    @Autowired
    public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider,
            EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.emailPasswordAuthenticationProvider = emailPasswordAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/auth/signup").permitAll()
                .anyRequest().authenticated().and()
                // Auth
                .addFilterAt(new AuthenticationProcessingFilter(this.authenticationManager(),
                        this.jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new JwtAuthorizationFilter(this.authenticationManager()), BasicAuthenticationFilter.class)
                // Auth Error Handling
                // .exceptionHandling().authenticationEntryPoint(new
                // HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                // Session
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

}