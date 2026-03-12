package se.trollhattan.citizenapi.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for a citizen row in the database.
 */
@Entity
@Table(name = "citizens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitizenEntity {

    /**
     * Internal database primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Municipality identifier for the citizen.
     */
    @Column(name = "municipality_id", nullable = false)
    private String municipalityId;

    /**
     * Swedish personal identity number (12 digits).
     */
    @Column(name = "person_number", nullable = false)
    private String personNumber;

    /**
     * Stable party identifier (UUID) for the citizen.
     */
    @Column(name = "party_id", nullable = false, unique = true)
    private UUID partyId;

    /**
     * Timestamp when this record was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when this record was last updated.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
