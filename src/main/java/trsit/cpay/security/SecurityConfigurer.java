/**
 *
 */
package trsit.cpay.security;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author black
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Inject
    private DataSource dataSource;

    @Inject
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication() //
        .dataSource(dataSource) //
        .usersByUsernameQuery("select name, password, enabled from User where name=?") //
        .authoritiesByUsernameQuery("select name, authority from User where name=?");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests() //
        .antMatchers("/**/resource/**").permitAll()
        .antMatchers("/register").permitAll() //
        .antMatchers("/**").access("hasRole('USER')") //
        .and() //
        .formLogin().loginPage("/login")
        .permitAll()
        .and()
        .csrf().disable();

        http.logout() //
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

    }
}
