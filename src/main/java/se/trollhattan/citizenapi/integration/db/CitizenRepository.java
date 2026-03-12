package se.trollhattan.citizenapi.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.trollhattan.citizenapi.integration.db.model.CitizenEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing citizen data.
 */
@Repository
public interface CitizenRepository extends JpaRepository<CitizenEntity, Long> {

    /**
     * Finds a citizen by municipality and person number.
     *
     * @param municipalityId the municipality identifier
     * @param personNumber   the Swedish personal identity number
     * @return an Optional containing the citizen if found, or empty if not
     */
    Optional<CitizenEntity> findByMunicipalityIdAndPersonNumber(String municipalityId, String personNumber);

    /**
     * Finds a citizen by municipality and partyId.
     *
     * @param municipalityId the municipality identifier
     * @param partyId        the stable UUID assigned to the citizen
     * @return an Optional containing the citizen if found, or empty if not
     */
    Optional<CitizenEntity> findByMunicipalityIdAndPartyId(String municipalityId, UUID partyId);
}
