package com.ks.projects.shortnrbackend.data.repository;

import com.ks.projects.shortnrbackend.data.model.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Pavel on 20.01.2017.
 */
@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {


    Link findLinkByToken(String token);

    @Query(value = "SELECT * FROM LINKS L LEFT JOIN USERS U ON L.USER_ID = U.ID WHERE U.USERNAME=?1 ORDER BY L.creation_date DESC",
            nativeQuery = true)
    List<Link> findLinksByUsername(String username);

    @Query(value = "SELECT * FROM LINKS L LEFT JOIN USERS U ON L.USER_ID = U.ID WHERE L.URL=?1 AND U.USERNAME=?2",
            nativeQuery = true)
    Link findLinkByUrlAndUsername(String url, String username);

    Set<Link> findByTagsIgnoreCaseContaining(String hashTag);

    Link findLinkById(long id);

    @Query(value = "INSERT INTO links VALUES (?1,now(),?2,?3,?4,?5,?6) RETURNING id", nativeQuery = true)
    Long insertLink(Long id, String d,  String t, String token, String url, Long userId);
}
