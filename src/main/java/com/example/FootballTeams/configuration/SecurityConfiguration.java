package com.example.FootballTeams.configuration;

import com.example.FootballTeams.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        var daoAuthenicationProvider = new DaoAuthenticationProvider();
        daoAuthenicationProvider.setUserDetailsService(userDetailsService);
        daoAuthenicationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenicationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf()
                .ignoringAntMatchers("/**")
                .and()
                .cors()
                .disable()
                .authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/fetchAllTeams").permitAll()
                .antMatchers("/fetchAllPages").permitAll()
                .antMatchers("/fetchPagesWithTeamName").permitAll()
                .antMatchers("/findTeamById/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/externalApi").permitAll()
                .antMatchers("/createTeam").hasAuthority("ADMIN")
                .antMatchers("/removeTeam/{id}").hasAuthority("ADMIN")
                .antMatchers("/updateTeam/{id}").hasAuthority("ADMIN")
                .antMatchers("/footballController").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
