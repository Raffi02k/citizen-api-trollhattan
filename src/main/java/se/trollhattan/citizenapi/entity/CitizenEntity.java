package se.trollhattan.citizenapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "citizen")
public class CitizenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String municipalityId;
    private String personNumber;
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
