package com.ks.projects.shortnrbackend.services.services;


import com.ks.projects.shortnrbackend.data.model.dto.RegistrationUserDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserEditDto;
import org.springframework.stereotype.Service;

/**
 * Created by Pavel on 05.01.2017.
 */
@Service
public interface UserService {

    void updateUser(String username, UserEditDto userEditDto) throws Exception;
    void registerUser(RegistrationUserDto registrationUserDto) throws Exception;

    UserDto getUserInfo(String username);
}
