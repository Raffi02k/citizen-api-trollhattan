package se.trollhattan.citizenapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.trollhattan.citizenapi.entity.CitizenEntity;

import java.util.Optional;

public interface CitizenRepository extends JpaRepository<CitizenEntity, Long> {
    Optional<CitizenEntity> findByMunicipalityIdAndPersonNumber(String municipalityId, String personNumber);
}
