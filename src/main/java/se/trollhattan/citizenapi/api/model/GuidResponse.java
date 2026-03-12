package se.trollhattan.citizenapi.api.model;

public class GuidResponse {

        private String partyId;

        public GuidResponse() {
        }

        public GuidResponse(String partyId) {
                this.partyId = partyId;
        }

        public String getPartyId() {
                return partyId;
        }

        public void setPartyId(String partyId) {
                this.partyId = partyId;
        }
}