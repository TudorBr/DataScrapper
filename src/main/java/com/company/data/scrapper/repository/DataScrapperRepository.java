package com.company.data.scrapper.repository;

import com.company.data.scrapper.model.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DataScrapperRepository extends CrudRepository<Company, Long> {

    @Query("""
    SELECT c
    FROM Company c
    JOIN c.phone p
    WHERE p = :phone
    """)
    Optional<Company> findByPhone(String phone);

    Optional<Company> findByDomain(String domain);

    Optional<Company> findByFacebook(String facebook);

    @Query("""
    SELECT DISTINCT c
    FROM Company c
    LEFT JOIN c.legalName ln
    LEFT JOIN c.availableNames an
    WHERE c.commercialName = :name
       OR ln = :name
       OR an = :name
    """)
    Optional<Company> findByName(@Param("name") String name);

    @Query("""
        SELECT DISTINCT c
        FROM Company c
          LEFT JOIN c.legalName ln
          LEFT JOIN c.availableNames an
          LEFT JOIN c.phone p
        WHERE (:name     IS NULL OR c.commercialName = :name
                                    OR ln           = :name
                                    OR an           = :name)
          AND (:phone    IS NULL OR p            = :phone)
          AND (:domain   IS NULL OR c.domain     = :domain)
          AND (:facebook IS NULL OR c.facebook   = :facebook)
    """)
    List<Company> searchAll(
            @Param("name")     String name,
            @Param("phone")    String phone,
            @Param("domain")   String domain,
            @Param("facebook") String facebook
    );
}
