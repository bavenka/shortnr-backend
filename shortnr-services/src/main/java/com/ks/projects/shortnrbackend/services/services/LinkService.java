package com.ks.projects.shortnrbackend.services.services;


import com.ks.projects.shortnrbackend.data.model.dto.EditingLinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkWithUserDto;
import com.ks.projects.shortnrbackend.data.model.dto.RegistrationLinkDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Pavel on 20.01.2017.
 */
@Service
public interface LinkService {

    String saveAnonymousLink(RegistrationLinkDto registrationLinkDto) throws Exception;

    LinkDto saveLink(String username, RegistrationLinkDto registrationLinkDto) throws Exception;

    LinkWithUserDto getLinkByToken(String linkToken);

    void updateLink(String username, long linkId, EditingLinkDto editingLinkDto) throws Exception;

    List<LinkDto> getUserLinksByUsername(String username);

    Set<LinkDto> getLinksByHashTag(String hashTag);

    LinkDto getUserLinkWithStatistics(String username, long id);

    void deleteLink(String username, long id) throws Exception;


}
