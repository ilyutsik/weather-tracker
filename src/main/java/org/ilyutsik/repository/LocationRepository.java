package org.ilyutsik.repository;

import org.ilyutsik.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByUserId(Long userId);

    void deleteByLatitudeAndLongitudeAndName(BigDecimal latitude, BigDecimal longitude, String name);

}
