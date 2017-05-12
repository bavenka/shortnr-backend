package com.ks.projects.shortnrbackend.services.services;

import com.ks.projects.shortnrbackend.data.model.dto.LinkStatisticDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkWithUserDto;
import com.ks.projects.shortnrbackend.data.model.entity.Link;
import com.ks.projects.shortnrbackend.data.model.entity.LinkStatistic;
import com.ks.projects.shortnrbackend.services.services.impl.LinkStatisticServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Pavel on 5/2/2017.
 */
@Service
public interface LinkStatisticService {

    void saveLinkStatistic(LinkWithUserDto link);

    List<LinkStatisticServiceImpl.AbstractStatisticsContainer> getLinkStatisticsByLink_Id(long id);

    List<LinkStatisticServiceImpl.AbstractCountryStatistics> getLinkCountryStatsByLink_Id(long id);

}
