package vavi.net.webhook;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class WebSocketSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String WEBHOOK_USER_PASSWORD = "WEBHOOK_USER_PASSWORD";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().antMatchers("/ws/**").hasRole("USER")
                                .anyRequest().permitAll()
            .and()
            .httpBasic().realmName("vavi-apps-webhook")
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("{noop}" + System.getenv(WEBHOOK_USER_PASSWORD))
            .roles("USER");
    }
}