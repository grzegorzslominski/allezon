package pl.edu.pjwstk.jaz.allezon.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigure extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/allezon/login").permitAll()
                .antMatchers("/allezon/register").permitAll()
                .antMatchers("/allezon/categories").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }
}
