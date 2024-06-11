package com.pro.electronic.store.controllers;

import com.pro.electronic.store.dtos.ApiResponseMessage;
import com.pro.electronic.store.dtos.ImageResponse;
import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.dtos.UserDto;
import com.pro.electronic.store.services.FileService;
import com.pro.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    //dynamically getting file path for uploading image
    @Value("${user.profile.image.path}")
    private  String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto user = userService.CreateUser(userDto);
        return new  ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{UserId}")
    public  ResponseEntity<UserDto> updateUser(@Valid @PathVariable String UserId,@RequestBody   UserDto userDto)
    {
        UserDto updateduser = userService.UpdateUser(userDto, UserId);
        return  new ResponseEntity<>(updateduser,HttpStatus.OK);
    }



    //delete
    @DeleteMapping("/{UserId}")
    public  ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("UserId") String UserId)
    {
        userService.DeleteUser(UserId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Deleted Successfully..!!")
                .success(true)
                .status(HttpStatus.OK).build();
        return  new ResponseEntity<>(message,HttpStatus.OK);
    }

    //get single user
    @GetMapping("/{UserId}")
    public  ResponseEntity<UserDto> getSingleUser(@PathVariable("UserId") String UserId)
    {
        UserDto user = userService.GetUserById(UserId);
        return  new ResponseEntity<>(user,HttpStatus.FOUND);
    }

    //get All user
    @GetMapping()
    public  ResponseEntity<PageableResponse<UserDto>> getAllUsers(
                @RequestParam(value = "PageNumber",defaultValue = "0",required = false) int PageNumber,@RequestParam(value = "pageSize",defaultValue = "10",required = false) int PageSize,
               @RequestParam(value = "SortBy",defaultValue = "name",required = false) String SortBy,
          @RequestParam(value = "SortDir",defaultValue = "asc",required = false) String SortDir)

    {
       PageableResponse<UserDto> allUser = userService.getAllUser(PageNumber,PageSize,SortBy,SortDir);
        return  new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    //get by email id
    @GetMapping("/email/{Email}")
    public  ResponseEntity<UserDto> getByEmailId(@PathVariable String Email)
    {
        UserDto userDto = userService.GetUserByEmail(Email);
        return  new ResponseEntity<>(userDto,HttpStatus.FOUND);
    }

    //search user by keyword
    @GetMapping("/search/{keywords}")
    public  ResponseEntity<List<UserDto>> searchUsers(@PathVariable String keywords)
    {
        List<UserDto> userDtoList = userService.SearchUser(keywords);
        return  new ResponseEntity<>(userDtoList,HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("Image")MultipartFile Image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(Image, imageUploadPath);

        //update user image name
        UserDto user = userService.GetUserById(userId);
        user.setProfile_pic_name(imageName);
        UserDto updatedUser = userService.UpdateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image Uploaded SuccessFully..!!").success(true).status(HttpStatus.CREATED).build();
        return  new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serve user image

    @GetMapping("/image/{userId}")
    public  void ServeUserImage (@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.GetUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getProfile_pic_name());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


}
