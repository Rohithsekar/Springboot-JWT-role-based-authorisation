package com.rohi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rohi.utils.RSAKeyProperties;

@Configuration
public class SecurityConfiguration {

    private final RSAKeyProperties keys;

    public SecurityConfiguration(RSAKeyProperties keys){
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/auth/**").permitAll();
                auth.requestMatchers("/admin/**").hasRole("ADMIN");
                auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
                auth.anyRequest().authenticated();
            });
            
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
                
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }
    
}

/*
In the provided SecurityConfiguration class, the OAuth2ResourceServer is configured using the
http.oauth2ResourceServer() method chain. Let's take a closer look at how it is configured:


http.oauth2ResourceServer()
    .jwt()
    .jwtAuthenticationConverter(jwtAuthenticationConverter());

This configuration specifies that the resource server should expect JWT tokens for authentication. The jwt() method is
used to configure JWT-specific settings for the resource server.

The jwtAuthenticationConverter() method sets the JwtAuthenticationConverter to extract and convert the authorities from
the JWT token. In this case, the converter uses a JwtGrantedAuthoritiesConverter to handle the conversion. It maps the
"roles" claim in the JWT token to authorities, prefixing them with "ROLE_" to follow the Spring Security conventions.

By configuring the JwtAuthenticationConverter, the resource server is able to extract the authorities from the JWT
token and use them for access control decisions.

Additionally, the other parts of the SecurityConfiguration class handle different aspects of security, such as
authentication, authorization, password encoding, and session management.

The code utilizes RSA keys for JWT encoding and decoding,
as indicated by the jwtDecoder() and jwtEncoder() methods. These methods create instances of NimbusJwtDecoder
and NimbusJwtEncoder, respectively, using the provided RSA keys.

Overall, this configuration sets up the resource server to expect and validate JWT tokens for authentication, extract
authorities from the tokens, and handle other security aspects defined in the class.
 */

/*
 If you don't use OAuth2ResourceServer, you would need to implement your own filter or mechanism to extract claims
 from the JWT token manually.

The OAuth2ResourceServer in Spring Security provides a convenient way to handle JWT token validation and extraction of
claims. It abstracts away the complexity of validating the token's signature, checking its expiration, and extracting
the necessary information like authorities or user details.

By configuring the OAuth2ResourceServer, you delegate the responsibility of JWT validation and extraction to the
framework. The OAuth2ResourceServer performs these tasks transparently, allowing you to focus on defining access
control rules and handling authenticated requests.

If you decide not to use OAuth2ResourceServer, you would need to implement a custom filter or interceptor to manually
handle the validation and extraction of claims from the JWT token. This involves verifying the token's signature,
 checking its expiration, and extracting relevant information like user details or authorities.

Implementing your own filter or mechanism can be more error-prone and time-consuming compared to using the
OAuth2ResourceServer, as you would need to handle various security concerns and ensure the correct extraction of claims.

In summary, the use of OAuth2ResourceServer in Spring Security helps simplify the process of JWT token validation and
claims extraction, relieving you from the manual implementation of these tasks. It provides a standardized and secure
way to protect your resources using JWT tokens.
 */