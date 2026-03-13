MERGE INTO citizen (municipality_id, person_number, party_id)
KEY (municipality_id, person_number)
VALUES ('1488', '199001011234', 'test-party-id-123');

MERGE INTO citizen (municipality_id, person_number, party_id)
KEY (municipality_id, person_number)
VALUES ('1488', '198512121212', 'test-party-id-456');