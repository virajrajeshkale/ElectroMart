package com.pro.electronic.store.configuration;

import com.pro.electronic.store.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final String[] PUBLIC_URLS = {

            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v2/api-docs",
            "/test"

    };
    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;


//******* HARDCODE user *****
//@Bean
//    public UserDetailsService userDetailsService()
//    {
//            //create user
//        UserDetails adminuser = User.builder()
//                .username("Admin")
//                .password(passwordEncoder().encode("Admin@123"))
//                .roles("Normal User")
//                .build();
//
//        UserDetails virajuser = User.builder()
//                .username("Viraj")
//                .password(passwordEncoder().encode("pass@123"))
//                .roles("Admin")
//                .build();
//
//        return new InMemoryUserDetailsManager(virajuser,adminuser);
//    }


    //for customize login page
@Bean
   public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//                    httpSecurity.authorizeRequests().anyRequest().authenticated().and()
//                            .formLogin()
//                            .loginPage("/login.html")
//                            .loginProcessingUrl("process-url")
//                            .defaultSuccessUrl("dashboard")
//                            .failureUrl()
//                            .and().logout().logoutUrl();
//
//                    return httpSecurity.build();

    http.csrf((csrf)->csrf.disable())
//   	.cors((cors)->cors.disable()) //-> if you want to use default cors then enable this 
            .authorizeHttpRequests(authorize-> authorize.requestMatchers("/auth/login").permitAll())
            .authorizeHttpRequests(authorize-> authorize.requestMatchers(PUBLIC_URLS).permitAll())
            .authorizeHttpRequests(authorize-> authorize.requestMatchers("/auth/google").permitAll())
            .authorizeHttpRequests(authorize-> authorize.requestMatchers(HttpMethod.POST,"/users").permitAll())
            .authorizeHttpRequests(authorize-> authorize.requestMatchers(HttpMethod.GET,"/users").permitAll())
//		.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN"))
            .authorizeHttpRequests(authorize->authorize.anyRequest().authenticated())
            .exceptionHandling(exceptionHandling->exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();


    //changes
//         httpSecurity .csrf()
//            .disable()
//            .cors()
//            .disable()
//            .authorizeRequests()
//            .antMatchers("/auth/login")
//            .permitAll()
//            .antMatchers("/auth/google")
//            .permitAll()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(authenticationEntryPoint)
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


//                 .csrf()
//                 .disable()
//                 .cors()
//                 .disable()
//                 .authorizeRequests()
//                 .anyRequest()
//                 .authenticated()
//                 .and()
//                 .exceptionHandling()
//                 .authenticationEntryPoint(authenticationEntryPoint)
//                 .and()
//                 .sessionManagement()
//                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//    chnages


//         httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//         return  httpSecurity.build();
   }

@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //for encoding password
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {  // Changed the return type to CorsFilter
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//	        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:4200"));
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);  // Return CorsFilter directly
    }


}
