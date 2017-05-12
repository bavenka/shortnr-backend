package com.ks.projects.shortnrbackend.services.services.impl;

import com.ks.projects.shortnrbackend.data.model.dto.LinkStatisticDto;
import com.ks.projects.shortnrbackend.data.model.dto.LinkWithUserDto;
import com.ks.projects.shortnrbackend.data.model.entity.Link;
import com.ks.projects.shortnrbackend.data.model.entity.LinkStatistic;
import com.ks.projects.shortnrbackend.data.repository.LinkRepository;
import com.ks.projects.shortnrbackend.data.repository.LinkStatisticRepository;
import com.ks.projects.shortnrbackend.services.converter.Converter;
import com.ks.projects.shortnrbackend.services.services.LinkStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Pavel on 5/2/2017.
 */
@Component
public class LinkStatisticServiceImpl implements LinkStatisticService {

    @Autowired
    private LinkStatisticRepository linkStatisticRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public void saveLinkStatistic(LinkWithUserDto linkWithUserDto) {

        long linkId = linkWithUserDto.getLinkDto().getId();
        Link linkById = linkRepository.findLinkById(linkId);
        LinkStatistic ls = new LinkStatistic();
        ls.setLink(linkById);
        ls.setClickDate(new Date());
        ls.setCountry(linkWithUserDto.getCountry());

        LinkStatistic save = linkStatisticRepository.save(ls);
    }

    @Override
    public List<AbstractStatisticsContainer> getLinkStatisticsByLink_Id(long id) {

        List<LinkStatistic> statistics = linkStatisticRepository.getLinkStatisticsByLink_IdOrderByClickDateAsc(id);

        List<LinkStatisticDto> statisticDtos = new ArrayList<>();
        if (!statistics.isEmpty()) {
            statisticDtos = new ArrayList<>();
            for (LinkStatistic ls : statistics) {
                statisticDtos.add(Converter.toLinkStatisticDto(ls));
            }
        }


        LocalDate currentDate = LocalDate.now().plusDays(2);
        LocalDate lastMonthDate = currentDate.minusMonths(1);
        List<LocalDate> totalDates = new ArrayList<>();
        while (currentDate.isAfter(lastMonthDate)) {
            totalDates.add(lastMonthDate);
            lastMonthDate = lastMonthDate.plusDays(1);
        }

        List<AbstractStatisticsContainer> asc = new ArrayList<>();

        for (int i = 0; i < totalDates.size() - 1; i++) {
            LocalDate startLDate = totalDates.get(i);
            LocalDate finishLDate = totalDates.get(i + 1);
            AbstractStatisticsContainer container = new AbstractStatisticsContainer();
            container.setTime(startLDate.getDayOfMonth() + " " + startLDate.getMonth().name().substring(0,3));
            container.setClickCount(new Integer(0));

            for (int j = 0; j < statisticDtos.size(); j++) {
                Date clickDate = statisticDtos.get(j).getClickDate();
                Date start = Date.from(startLDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date finish = Date.from(finishLDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (!(clickDate.before(start) || clickDate.after(finish))) {
                    container.setClickCount(container.getClickCount() + 1);
                }
            }

            asc.add(container);
        }

        return asc;
    }

    public List<AbstractCountryStatistics> getLinkCountryStatsByLink_Id(long id) {

        List<LinkStatistic> ls = linkStatisticRepository.getLinkStatisticsByLink_IdOrderByCountryAsc(id);
        Map<String, Integer> countries = new HashMap<>();
        for (LinkStatistic s : ls) {
            String country = s.getCountry();
            if (!countries.containsKey(country)) {
                countries.put(country, 1);
            } else {
                Integer integer = countries.get(country);
                countries.put(country, integer + 1);
            }

        }
        ArrayList<AbstractCountryStatistics> countryStatistics = new ArrayList<>();

        for (Map.Entry<String, Integer> kv : countries.entrySet()) {
            countryStatistics.add(new AbstractCountryStatistics(kv.getKey(), kv.getValue()));
        }

        return countryStatistics;
    }

    public class AbstractStatisticsContainer {
        private String time;
        private Integer clickCount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getClickCount() {
            return clickCount;
        }

        public void setClickCount(Integer clickCount) {
            this.clickCount = clickCount;
        }
    }

    public class AbstractCountryStatistics {
        private String label;
        private Integer value;

        public AbstractCountryStatistics(String label, Integer value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

}
