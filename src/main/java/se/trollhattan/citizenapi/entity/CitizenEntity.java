package se.trollhattan.citizenapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "citizen", uniqueConstraints = {
        @UniqueConstraint(name = "uk_citizen_municipality_person", columnNames = { "municipality_id", "person_number" })
})
public class CitizenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "municipality_id", nullable = false)
    private String municipalityId;

    @Column(name = "person_number", nullable = false)
    private String personNumber;

    @Column(name = "party_id", nullable = false)
    private String partyId;

    public CitizenEntity() {
    }

    public CitizenEntity(String municipalityId, String personNumber, String partyId) {
        this.municipalityId = municipalityId;
        this.personNumber = personNumber;
        this.partyId = partyId;
    }

    public Long getId() {
        return id;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}