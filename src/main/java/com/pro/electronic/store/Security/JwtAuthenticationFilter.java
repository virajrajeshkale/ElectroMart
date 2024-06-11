package com.pro.electronic.store.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class  JwtAuthenticationFilter  extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestheader = request.getHeader("Authorization");
        logger.info("Header :{}",requestheader);
        //header contain token value like bearer 4373653bjrrr6565hssf

        String Username = null;
        String Token = null;


        //in token need substring value of header token value

        if(requestheader!=null && requestheader.startsWith("Bearer"))
        {
            Token = requestheader.substring(7);


                    try {

                        Username=this.jwtHelper.getUsernameFromToken(Token);

                    } catch (IllegalArgumentException e) {
                        logger.info("Illegal Argument Exception occur..!!");
                        e.printStackTrace();
                    } catch (ExpiredJwtException e) {
                        logger.info("JWT token is Expired");
                        e.printStackTrace();
                    } catch (MalformedJwtException e) {
                        logger.info("Some Changed has done in token ..!! Invalid Token..");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
          }else
        {
            logger.info("Invalid Header Value..!!");
        }


        if(Username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            //fetch user
            UserDetails userDetails = userDetailsService.loadUserByUsername(Username);
            Boolean validateToken = this.jwtHelper.validateToken(Token, userDetails);

            if(validateToken)
            {
                //set authentication

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else
            {
                logger.info("Validation Failed..!");
            }
        }

        filterChain.doFilter(request,response);

    }
}
