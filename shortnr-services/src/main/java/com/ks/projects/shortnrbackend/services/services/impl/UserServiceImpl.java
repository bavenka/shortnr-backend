package com.ks.projects.shortnrbackend.services.services.impl;


import com.ks.projects.shortnrbackend.data.model.dto.RegistrationUserDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserEditDto;
import com.ks.projects.shortnrbackend.data.model.entity.User;
import com.ks.projects.shortnrbackend.data.repository.UserRepository;
import com.ks.projects.shortnrbackend.services.constant.Constant;
import com.ks.projects.shortnrbackend.services.converter.Converter;
import com.ks.projects.shortnrbackend.services.services.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * Created by Pavel on 05.01.2017.
 */
@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private boolean usernameExist(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }


    @Override
    public void registerUser(RegistrationUserDto registrationUserDto) throws Exception {
        if (emailExist(registrationUserDto.getEmail())) {
            JSONObject json = new JSONObject();
            json.put("email", Constant.MESSAGE_EMAIL_EXIST);
            String jsonString = json.toJSONString();
            throw new Exception(jsonString);
        }
        if (usernameExist(registrationUserDto.getUsername())) {
            JSONObject json = new JSONObject();
            json.put("username", Constant.MESSAGE_USERNAME_EXIST);
            String jsonString = json.toJSONString();
            throw new Exception(jsonString);
        }
        User user = Converter.toRegisteredUser(registrationUserDto);
        userRepository.save(user);
    }

    @Override
    public void updateUser(String username, UserEditDto userEditDto) throws Exception {
        User user = userRepository.findByUsername(username);
        if (!Objects.equals(user.getEmail(), userEditDto.getEmail())) {
            if (emailExist(userEditDto.getEmail())) {
                JSONObject json = new JSONObject();
                json.put("emailError", Constant.MESSAGE_EMAIL_EXIST);
                String jsonString = json.toJSONString();
                throw new Exception(jsonString);
            }
        }
        if (!Objects.equals(username, userEditDto.getUsername())) {
            if (usernameExist(userEditDto.getUsername())) {
                JSONObject json = new JSONObject();
                json.put("usernameError", Constant.MESSAGE_USERNAME_EXIST);
                String jsonString = json.toJSONString();
                throw new Exception(jsonString);
            }
        }

        if (userEditDto.getEmail() != null) {
            user.setEmail(userEditDto.getEmail());
        }
        if (userEditDto.getUsername() != null) {
            user.setUsername(userEditDto.getUsername());
        }
        if (userEditDto.getPassword() != null) {
            user.setPassword(userEditDto.getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public UserDto getUserInfo(String username) {
        return Converter.toUserDto(userRepository.findByUsername(username));
    }
}
