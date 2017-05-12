package com.ks.projects.shortnrbackend.webapp.controller;


import com.ks.projects.shortnrbackend.data.model.dto.EditingLinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkWithUserDto;
import com.ks.projects.shortnrbackend.data.model.dto.RegistrationLinkDto;
import com.ks.projects.shortnrbackend.services.services.LinkService;
import com.ks.projects.shortnrbackend.services.services.LinkStatisticService;
import com.ks.projects.shortnrbackend.services.services.impl.LinkStatisticServiceImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Created by Pavel on 20.01.2017.
 */
@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkStatisticService linkStatisticService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> createLink(@Valid @RequestBody RegistrationLinkDto registrationLinkDto) throws Exception {
        String token;
        try {
            token = linkService.saveAnonymousLink(registrationLinkDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        JSONObject json = new JSONObject();
        json.put("token", token);
        String jsonString = json.toJSONString();
        return new ResponseEntity<String>(jsonString, HttpStatus.CREATED);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/createuserlink/{username}", method = RequestMethod.POST)
    public ResponseEntity<LinkDto> createUserLink(@Valid @RequestBody RegistrationLinkDto registrationLinkDto,
                                                  @PathVariable String username) throws Exception {
        LinkDto linkDto = null;
        try {
            linkDto = linkService.saveLink(username, registrationLinkDto);
        } catch (Exception e) {
            return new ResponseEntity<>(linkDto, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(linkDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    public void redirect(@PathVariable(name = "token") String token, HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        LinkWithUserDto linkWithUserDto = linkService.getLinkByToken(token);
        if (linkWithUserDto == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            String country = request.getLocale().getCountry();
            linkWithUserDto.setCountry(country);
            linkStatisticService.saveLinkStatistic(linkWithUserDto);
            response.sendRedirect(linkWithUserDto.getLinkDto().getUrl());
        }
    }

    @RequestMapping(value = "/checktoken/{token}", method = RequestMethod.GET)
    public void checktoken(@PathVariable(name = "token") String token, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        LinkWithUserDto linkWithUserDto = linkService.getLinkByToken(token);
        if (linkWithUserDto == null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    @RequestMapping(value = "/users/{username}/links/", method = RequestMethod.GET)
    public ResponseEntity<List<LinkDto>> getUserLinks(@PathVariable String username) {
        List<LinkDto> linkDtos = linkService.getUserLinksByUsername(username);
        if (linkDtos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(linkService.getUserLinksByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(value = "/links/", method = RequestMethod.GET)
    public ResponseEntity<Set<LinkDto>> getLinksByHashTag(@RequestParam(name = "hashtag") String hashTag) {
        Set<LinkDto> linkDtos = linkService.getLinksByHashTag(hashTag);
        if (linkDtos == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(linkService.getLinksByHashTag(hashTag), HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/users/{username}/links/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateLink(@PathVariable String username, @PathVariable(name = "id") long linkId,
                                             @Valid @RequestBody EditingLinkDto editingLinkDto) throws Exception {
        try {
            linkService.updateLink(SecurityContextHolder.getContext().getAuthentication().getName(),
                    linkId, editingLinkDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/users/{username}/links/{id}/statistics/", method = RequestMethod.GET)
    public ResponseEntity<List<LinkStatisticServiceImpl.AbstractStatisticsContainer>> getLinkStatistics(@PathVariable String username,
                                                                                                        @PathVariable long id) {
        List<LinkStatisticServiceImpl.AbstractStatisticsContainer> dtoList = linkStatisticService.getLinkStatisticsByLink_Id(id);
        if (dtoList == null) {
            return new ResponseEntity<>(dtoList, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/users/{username}/links/{id}/statistics/countries", method = RequestMethod.GET)
    public ResponseEntity<List<LinkStatisticServiceImpl.AbstractCountryStatistics>> getLinkStatisticsCountries(@PathVariable String username,
                                                                                                               @PathVariable long id) {
        List<LinkStatisticServiceImpl.AbstractCountryStatistics> st = linkStatisticService.getLinkCountryStatsByLink_Id(id);
        if (st == null) {
            return new ResponseEntity<>(st, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(st, HttpStatus.OK);
    }

    @PreAuthorize("#username == authentication.name")
    @RequestMapping(value = "/users/{username}/links/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLink(@PathVariable String username,
                                             @PathVariable long id) throws Exception {
        try {
            linkService.deleteLink(SecurityContextHolder.getContext().getAuthentication().getName(),
                    id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
