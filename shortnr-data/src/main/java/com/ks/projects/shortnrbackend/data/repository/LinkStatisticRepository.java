package com.ks.projects.shortnrbackend.data.repository;

import com.ks.projects.shortnrbackend.data.model.entity.LinkStatistic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Pavel on 5/2/2017.
 */
@Repository
public interface LinkStatisticRepository extends CrudRepository<LinkStatistic, Long> {

    List<LinkStatistic> getLinkStatisticsByLink_IdOrderByClickDateAsc(long id);

    List<LinkStatistic> getLinkStatisticsByLink_IdOrderByCountryAsc(long id);

}
