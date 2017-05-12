package com.ks.projects.shortnrbackend.services.services.impl;

import com.ks.projects.shortnrbackend.data.model.dto.EditingLinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkWithUserDto;
import com.ks.projects.shortnrbackend.data.model.dto.RegistrationLinkDto;
import com.ks.projects.shortnrbackend.data.model.entity.Link;
import com.ks.projects.shortnrbackend.data.model.entity.User;
import com.ks.projects.shortnrbackend.data.repository.LinkRepository;
import com.ks.projects.shortnrbackend.data.repository.UserRepository;
import com.ks.projects.shortnrbackend.services.constant.Constant;
import com.ks.projects.shortnrbackend.services.converter.Converter;
import com.ks.projects.shortnrbackend.services.converter.LinkTokenConverter;
import com.ks.projects.shortnrbackend.services.services.LinkService;
import com.sun.deploy.util.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Pavel on 20.01.2017.
 */
@Component
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private UserRepository userRepository;

    private boolean urlValid(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
        if (urlValidator.isValid(url)) {
            return true;
        }
        return false;
    }

    private boolean userHasSoLink(String originalUrl, String username) {
        Link link = linkRepository.findLinkByUrlAndUsername(originalUrl, username);
        if (link == null) {
            return false;
        }
        return true;
    }

    @Override
    public String saveAnonymousLink(RegistrationLinkDto registrationLinkDto) throws Exception {
        if (!urlValid(registrationLinkDto.getUrl())) {
            throw new Exception(Constant.MESSAGE_NOT_VALID_URL);
        }
        User user = userRepository.findByUsername("ANONYMOUS");
        Link link = Converter.toRegisterLink(registrationLinkDto);
        link.setCreationDate(new Date());
        link.setUser(user);
        Link savedLink = linkRepository.save(link);

        long id = savedLink.getId();
        String token = LinkTokenConverter.encodeLinkId(id);
        savedLink.setToken(token);
        linkRepository.save(savedLink);
        return savedLink.getToken();
    }

    @Override
    public LinkDto saveLink(String username, RegistrationLinkDto registrationLinkDto) throws Exception {
        if (!urlValid(registrationLinkDto.getUrl())) {
            throw new Exception(Constant.MESSAGE_NOT_VALID_URL);
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception(Constant.MESSAGE_NOT_FOUND_USER);
        }
//        if (userHasSoLink(registrationLinkDto.getUrl(), username)) {
//            throw new Exception(Constant.MESSAGE_LINK_EXIST);
//        }
        Link link = Converter.toRegisterLink(registrationLinkDto);
        link.setCreationDate(new Date());
        link.setUser(user);
        String token = registrationLinkDto.getToken();
        if (token == null) {
            Link savedLink = linkRepository.save(link);
            long id = savedLink.getId();
            String token1 = LinkTokenConverter.encodeLinkId(id);
            savedLink.setToken(token1);
            Link saved = linkRepository.save(savedLink);
            return Converter.toLinkWithoutStatisticsDto(saved);
        } else {
            long l = LinkTokenConverter.decodeToken(token);
            Link linkById = linkRepository.findLinkById(l);
            if (linkById == null) {
                link.setId(l);
                link.setToken(token);
                Long id = linkRepository.insertLink(link.getId(), link.getDescription(), link.getTags(), link.getToken(), link.getUrl(), link.getUser().getId());
                link.setId(id);
                return Converter.toLinkWithoutStatisticsDto(link);
            }else {
                throw new Exception(Constant.MESSAGE_TOKEN_LINK_EXIST);
            }
        }
    }

    @Override
    public LinkWithUserDto getLinkByToken(String token) {

        long id = LinkTokenConverter.decodeToken(token);
        Link link = linkRepository.findLinkById(id);
        if (link == null) {
            link = linkRepository.findLinkByToken(token);
        }

        if (link == null) {
            return null;
        }


        return Converter.toLinkWithUserDto(link);
    }


    @Override
    public void updateLink(String username, long id, EditingLinkDto editingLinkDto) throws Exception {
        Link link = linkRepository.findOne(id);
        if (link != null && link.getUser().getUsername().equals(username)) {
            link.setDescription(editingLinkDto.getDescription());
            link.setTags(StringUtils.join(editingLinkDto.getTags(), ", "));
            link.setToken(editingLinkDto.getToken());
            link.setUrl(editingLinkDto.getUrl());
            linkRepository.save(link);
            return;
        }
        throw new Exception(Constant.MESSAGE_NOT_FOUND_URL);
    }

    @Override
    public List<LinkDto> getUserLinksByUsername(String username) {
        List<Link> links = linkRepository.findLinksByUsername(username);
        List<LinkDto> linkDtos = null;
        if (!links.isEmpty()) {
            linkDtos = new ArrayList<>();
            for (Link link : links) {
                linkDtos.add(Converter.toLinkWithoutStatisticsDto(link));
            }
        }
        return linkDtos;
    }

    @Override
    public Set<LinkDto> getLinksByHashTag(String hashTag) {
        Set<Link> links = linkRepository.findByTagsIgnoreCaseContaining(hashTag);
        Set<LinkDto> linkDtos = null;
        if (!links.isEmpty()) {
            linkDtos = new HashSet<>();
            for (Link link : links) {
                linkDtos.add(Converter.toLinkWithoutStatisticsDto(link));
            }
        }
        return linkDtos;
    }

    @Override
    public LinkDto getUserLinkWithStatistics(String username, long id) {
        Link link = linkRepository.findOne(id);
        if (link == null || !link.getUser().getUsername().equals(username)) {
            return null;
        }
        return Converter.toWorkLinkDto(link);
    }

    @Override
    public void deleteLink(String username, long id) throws Exception {
        Link link = linkRepository.findOne(id);
        if (link == null || !link.getUser().getUsername().equals(username)) {
            throw new Exception(Constant.MESSAGE_NOT_FOUND_URL);
        }
        linkRepository.delete(link.getId());
    }


}
