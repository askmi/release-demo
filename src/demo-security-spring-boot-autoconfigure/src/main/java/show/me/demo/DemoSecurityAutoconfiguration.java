package show.me.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import show.me.demo.components.CustomMethodSecurityExpressionHandler;
import show.me.demo.components.CustomPermissionEvaluator;

@AutoConfiguration(
        before = SecurityAutoConfiguration.class,
        beforeName = "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration"
)
@ConditionalOnWebApplication
@PropertySource(value = "classpath:security.properties")
@EnableConfigurationProperties(DemoSecurityAutoconfiguration.SecurityConfig.class)
public class DemoSecurityAutoconfiguration {

    @EnableMethodSecurity
    @EnableWebSecurity(debug = true)
    @ImportAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
    static class EnableSecurity {}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated());

        http.oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()));

        http.requestCache(configurer -> configurer.requestCache(new NullRequestCache()));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.application.name}") String name) {
        return token -> Jwt
                .withTokenValue(token)
                .header("kid","12345")
                .claim("scope","openapi")
                .claim("tenant","A")
                .claim("app",name)
                .build();
    }

    @Bean
    static CustomMethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new CustomMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }

    @ConfigurationProperties("security")
    record SecurityConfig (boolean enabled) {
    }
}
