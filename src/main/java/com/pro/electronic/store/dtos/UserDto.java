package com.pro.electronic.store.dtos;

import com.pro.electronic.store.Validate.ImageNameValid;
import com.pro.electronic.store.entites.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private  String  UserId;

    @NotNull(message = "NAME IS REQUIRED..!")
    @Size(min = 3 , max = 25)
     private  String name;

   @NotNull (message = "EMAIL IS REQUIRED..!")
   @Email(message = "Valid Email Required..!")
    private  String email;

   @NotNull(message = "PASSWORD IS REQUIRED..!")
    private  String password;

   @NotNull(message = "GENDER IS REQUIRED..!")
   @Size(min = 4,max = 6 ,message = "Valid Gender Required..!")
    private  String gender;

   @NotNull(message = "About is Required..!")
    private String about;

   @ImageNameValid
    private  String  profile_pic_name;

    private Set<RoleDto> roles = new HashSet<>();

}
