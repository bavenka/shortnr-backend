package com.ks.projects.shortnrbackend.services.converter;

import com.ks.projects.shortnrbackend.data.model.dto.*;
import com.ks.projects.shortnrbackend.data.model.entity.Link;
import com.ks.projects.shortnrbackend.data.model.entity.LinkStatistic;
import com.ks.projects.shortnrbackend.data.model.entity.User;
import com.sun.deploy.util.StringUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pavel on 20.01.2017.
 */
public class Converter {

    public static User toRegisteredUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setPassword(registrationUserDto.getPassword());
        user.setEmail(registrationUserDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAccountNonExpired(user.isAccountNonExpired());
        userDto.setCredentialsNonExpired(user.isCredentialsNonExpired());
        userDto.setEnabled(user.isEnabled());
        userDto.setAccountNonLocked(user.isAccountNonLocked());
        return userDto;
    }
    public static LinkStatisticDto toLinkStatisticDto(LinkStatistic ls){
        LinkStatisticDto lsd= new LinkStatisticDto();
        lsd.setId(ls.getId());
        lsd.setClickDate(ls.getClickDate());
        lsd.setCountry(ls.getCountry());
        return lsd;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAccountNonExpired(userDto.isAccountNonExpired());
        user.setCredentialsNonExpired(userDto.isCredentialsNonExpired());
        user.setEnabled(userDto.isEnabled());
        user.setAccountNonLocked(userDto.isAccountNonLocked());
        return user;
    }
    //TODO change registration method
    public static Link toRegisterLink(RegistrationLinkDto registrationLinkDto) {
        Link link = new Link();
        link.setUrl(registrationLinkDto.getUrl());
        String token = registrationLinkDto.getToken();
        if (token == null) {
            link.setToken("");
        }
        link.setToken(registrationLinkDto.getToken());
        String linkDescription = registrationLinkDto.getDescription();
        if (linkDescription != null) {
            link.setDescription(registrationLinkDto.getDescription());
        }
        Set<String> tags = registrationLinkDto.getTags();
        if (tags != null) {
            link.setTags(StringUtils.join(tags, ", "));
        }
        return link;
    }

    public static Link toLink(LinkDto linkDto) {
        Link link = new Link();
        link.setId(linkDto.getId());
        link.setUrl(linkDto.getUrl());
        link.setCreationDate(linkDto.getCreationDate());
        String linkDescription = linkDto.getDescription();
        if (linkDescription != null) {
            link.setDescription(linkDto.getDescription());
        }
        Set<String> tags = linkDto.getTags();
        if (tags != null) {
            link.setTags(StringUtils.join(tags, ", "));
        }
        link.setToken(linkDto.getToken());
        return link;
    }


    public static LinkDto toWorkLinkDto(Link link) {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(link.getId());
        linkDto.setUrl(link.getUrl());
        linkDto.setToken(link.getToken());
        linkDto.setCreationDate(link.getCreationDate());
        String linkDescription = link.getDescription();
        if (linkDescription != null) {
            linkDto.setDescription(link.getDescription());
        }
        String tags = link.getTags();
        if (tags != null) {
            linkDto.setTags(new HashSet<String>(Arrays.asList(tags.split(", "))));
        }
        return linkDto;
    }

    public static LinkDto toLinkWithoutStatisticsDto(Link link) {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(link.getId());
        linkDto.setUrl(link.getUrl());
        linkDto.setToken(link.getToken());
        String linkDescription = link.getDescription();
        if (linkDescription != null) {
            linkDto.setDescription(link.getDescription());
        }
        String tags = link.getTags();
        if (tags != null) {
            linkDto.setTags(new HashSet<String>(Arrays.asList(tags.split(", "))));
        }
        return linkDto;
    }

    public static LinkWithUserDto toLinkWithUserDto(Link link) {
        LinkWithUserDto linkWithUserDto = new LinkWithUserDto();
        UserDto userDto = Converter.toUserDto(link.getUser());
        LinkDto linkDto = Converter.toWorkLinkDto(link);
        linkWithUserDto.setLinkDto(linkDto);
        linkWithUserDto.setUserDto(userDto);
        return linkWithUserDto;
    }

    public static Link toLinkWithUser(LinkWithUserDto linkWithUserDto) {
        Link link = Converter.toLink(linkWithUserDto.getLinkDto());
        User user = Converter.toUser(linkWithUserDto.getUserDto());
        link.setUser(user);
        return link;
    }
}
