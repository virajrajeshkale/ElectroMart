package com.pro.electronic.store.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

//lombok annotation for automatically code generation
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Hibernate jpa
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    private  String  UserId;

    @Column(name = "User_Name",nullable = false)
    private  String name;

    @Column(name = "User_Email",nullable = false,unique = true)
    private  String email;

    @Column(name = "User_Password",nullable = false,length = 500)
    private  String password;

    @Column(nullable = false)
    private  String gender;

    @Column(length = 1000)
    private String about;

    @Column(nullable = false)
    private  String  profile_pic_name;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order> orderList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set < SimpleGrantedAuthority > authorities =  this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        return authorities;

    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public  String getPassword()
    {
        return  this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
