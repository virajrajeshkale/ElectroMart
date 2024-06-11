package com.pro.electronic.store.services.Implementation;


import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.dtos.UserDto;
import com.pro.electronic.store.entites.Role;
import com.pro.electronic.store.entites.User;
import com.pro.electronic.store.exceptions.ResourceNotFoundException;
import com.pro.electronic.store.helper.PageableHelper;
import com.pro.electronic.store.repositories.RoleRepository;
import com.pro.electronic.store.repositories.UserRepository;
import com.pro.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private  String imageFolderPath ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${normal.role.id}")
            private  String Normal_role_id;

    @Autowired
            private RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceimpl.class);

    @Override
    public UserDto CreateUser(UserDto userDto) {

        //GENERATE UNIQUE ID STRING FORMAT
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //Now we need USERDTO entity but it is now in data fromat so we use following
        //to convert userdto to->entity chcek the method at last
        User  user = dtoToEntity(userDto);

        //fetch role of normal user and set it to the user
        Role role = roleRepository.findById(Normal_role_id).get();
        user.getRoles().add(role);
        User save = userRepository.save(user);

        //now for returning the UserDto we need to again
        // convert entity -> Dto
        UserDto newdto = entityToDto(save);
       return  newdto;
    }




    @Override
    public void DeleteUser(String UserId) {
        User user = userRepository.findById(UserId).orElseThrow(() -> new ResourceNotFoundException("User Not Found for given Id..!!"));

        //delete user profile image
        String imagepath = imageFolderPath+user.getProfile_pic_name();

        try
        {
            Path path = Paths.get(imagepath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
        logger.info("Image File Not Found for give User..!!");
        e.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }


        userRepository.delete(user);
    }

    @Override
    public UserDto UpdateUser(UserDto userDto, String UserId) {

        User user = userRepository.findById(UserId).orElseThrow(() -> new ResourceNotFoundException("There is no User of given Id..!!"));

        //after successfully find now just upadte it
        //note we cannot update email and userid
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setProfile_pic_name(userDto.getProfile_pic_name());

        User updated_user = userRepository.save(user);
        UserDto userDto1 = entityToDto(updated_user);
        return userDto1;

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int PageNumber, int PageSize, String SortBy, String SortDir) {
        //this is in list of user but we need to return in dto
        Sort sort = (SortDir.equalsIgnoreCase("desc"))?(Sort.by(SortBy).descending()) : (Sort.by(SortBy).ascending());
        Pageable pageable = PageRequest.of(PageNumber,PageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> allUser  = page.getContent();

        //conversion of user to dto
        PageableResponse<UserDto> response = PageableHelper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto GetUserById(String UserId) {

        User user = userRepository.findById(UserId).orElseThrow(() -> new ResourceNotFoundException("User Not Found for given Id..!"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto GetUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User Not Found for given Email address..!!"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> SearchUser(String keyword) {

        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto>  userDtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return userDtoList;
    }


    //to convert userdto to->entity
    private User dtoToEntity(UserDto userdto) {
//       **  Manually conversion ***
//        User user  = User.builder()
//                .gender(userdto.getGender())
//                .about(userdto.getAbout())
//                .email(userdto.getEmail())
//                .name(userdto.getName())
//                .password(userdto.getPassword())
//                .profile_pic_name(userdto.getProfile_pic_name())
//                .UserId(userdto.getUserId()).build();
//
//        return user;
        //model mapper automatically convert
        return mapper.map(userdto,User.class);
    }

    // convert entity -> Dto
    private UserDto entityToDto(User save) {
        // ***** Manually conversion *****
//        UserDto userDto = UserDto.builder()
//                .UserId(save.getUserId())
//                .name(save.getName())
//                .about(save.getAbout())
//                .password(save.getPassword())
//                .profile_pic_name(save.getProfile_pic_name())
//                .gender(save.getGender())
//                .email(save.getEmail()).build();
//
//        return userDto;
        return  mapper.map(save,UserDto.class);
    }
}
