package com.pro.electronic.store.controllers;

import com.pro.electronic.store.Security.JwtHelper;
import com.pro.electronic.store.dtos.JwtRequest;
import com.pro.electronic.store.dtos.JwtResponse;
import com.pro.electronic.store.dtos.UserDto;
import com.pro.electronic.store.exceptions.BadApiRequest;
import com.pro.electronic.store.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "APIs for Authentication!!")
public class AuthController {
    @Autowired
  private    UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e)
        {throw  new BadApiRequest("Invalid UserName and Password..!");
        }

    }
    //to find  current login user
    @GetMapping("/curruser")
    ResponseEntity<UserDto>getCurrentUser(Principal principal)
    {
        String name = principal.getName();

        return  new ResponseEntity<>( modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }
@PostMapping("/login")
    public  ResponseEntity<JwtResponse>login(@RequestBody JwtRequest request)
    {
        this.doAuthenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto = modelMapper.map(userDetails,UserDto.class);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userDto(userDto).build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


}
