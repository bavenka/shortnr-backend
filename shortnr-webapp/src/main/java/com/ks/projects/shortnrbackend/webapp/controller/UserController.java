package com.ks.projects.shortnrbackend.webapp.controller;

import com.ks.projects.shortnrbackend.data.model.dto.EditingLinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserDto;
import com.ks.projects.shortnrbackend.data.model.dto.UserEditDto;
import com.ks.projects.shortnrbackend.data.repository.UserRepository;
import com.ks.projects.shortnrbackend.services.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by kamaz on 09.05.2017.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateUser(@PathVariable String username,
                                             @Valid @RequestBody UserEditDto userEditDto) throws Exception {
        try {
            userService.updateUser(SecurityContextHolder.getContext().getAuthentication().getName(), userEditDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/{username}/info", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) throws Exception {

        UserDto userDto = userService.getUserInfo(username);
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }



}
