# Citizen API

Citizen API är en mikrotjänst som anonymiserar och stabiliserar invånaridentitet genom att omvandla personnummer (eller liknande identifierare) till ett internt GUID/`partyId`.

Tjänsten är byggd för säkra, spårbara och skalbara API-anrop och fungerar som en identitetsbrygga mellan inloggning (SSO/SAML) och interna tjänster som kräver ett stabilt id.

## Syfte

Målet är att översätta en invånares identitet till ett internt GUID som andra system kan använda för ärenden, beslut, dokument och övriga integrationer.

## Översikt

Tjänsten ska kunna:

- ta emot personnummer eller `citizenIdentifier`
- slå upp om invånaren redan finns
- skapa ny invånare vid behov (find-or-create)
- returnera ett stabilt `partyId`
- exponera ett REST-API för uppslagning och koppling

## Planerad funktionalitet

### Version 1
- Health endpoint
- Uppslagning av `partyId` via personnummer
- Uppslagning av citizen via `partyId`
- Skapa ny citizen-post
- Databaslagring av invånare
- Flyway-migreringar
- OpenAPI / Swagger
- Validering och felhantering

## Exempel på API-endpoints

### Health
`GET /health`

Används för att verifiera att tjänsten är igång.

### Hämta GUID/partyId från personnummer
`GET /{municipalityId}/{personNumber}/guid`

Om invånaren inte finns skapas en ny citizen-post med nytt `partyId` (find-or-create).

Exempel:
`GET /1488/199001011234/guid`

Exempel på svar (rå sträng i response body):
```text
550e8400-e29b-41d4-a716-446655440000
```

### Hämta citizen via partyId

`GET /{municipalityId}/party/{partyId}`

Exempel på svar:

```json
{
  "partyId": "550e8400-e29b-41d4-a716-446655440000",
  "personNumber": "199001011234",
  "firstName": "Anna",
  "lastName": "Andersson"
}
```

### Skapa citizen

`POST /{municipalityId}`

Exempel på request:

```json
{
  "personNumber": "199001011234",
  "firstName": "Anna",
  "lastName": "Andersson"
}
```

## Säkerhet och spårbarhet

- **OAuth 2.0** för säkra server-till-server-anrop (Client Credentials Grant)
- **Token caching** för minskade externa anrop och stabil drift
- **API Gateway headers** för autentisering via integrationsplattform
- **Correlation ID** för spårbarhet i upstream/downstream

## Teknisk stack

Projektet är byggt med:

* Java 21
* Spring Boot
* Maven
* Spring Web
* Spring Validation
* Spring Data JPA
* Flyway
* PostgreSQL
* Spring Boot Actuator

## Projektstruktur

```text
src/
├── main/
│   ├── java/se/trollhattan/citizenapi/
│   │   ├── api/
│   │   │   ├── CitizenController.java
│   │   │   ├── HealthController.java
│   │   │   └── model/
│   │   ├── configuration/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   ├── service/
│   │   └── util/
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
```

Detaljerad paketstruktur:
```text
se.trollhattan.citizenapi
├── CitizenApiApplication.java
│
├── api/
│   ├── CitizenController.java
│   ├── HealthController.java
│   └── model/
│       ├── citizen/
│       │   ├── CitizenLookupResult.java
│       │   ├── CitizenRequest.java
│       │   └── CitizenResponse.java
│       └── navet/
│           ├── NavetLookupRequest.java
│           └── NavetLookupResponse.java
│
├── configuration/
│   ├── NavetProperties.java
│   └── RestClientConfig.java
│
├── entity/
│   └── CitizenEntity.java
│
├── exception/
│   ├── ApiError.java
│   ├── CitizenNotFoundException.java
│   ├── GlobalExceptionHandler.java
│   ├── NavetBadRequestException.java
│   ├── NavetRateLimitException.java
│   ├── NavetUnauthorizedException.java
│   └── NavetUnavailableException.java
│
├── repository/
│   └── CitizenRepository.java
│
├── service/
│   ├── CitizenLookupService.java
│   ├── CitizenService.java
│   ├── NavetService.java
│   └── NavetTokenService.java
│
└── util/
    └── PersonNumberValidator.java
```

## Navet-integration

Tjänsten kan använda Skatteverkets REST API (v3/hamta) för verifiering och uppslagning.

### Konfiguration
Alla anslutningsuppgifter sköts via miljövariabler i `application.yml`.

## Lokal utveckling

### Miljövariabler
För att köra mot Navets testmiljö behövs följande variabler:

```bash
export NAVET_BASE_URL=...
export NAVET_TOKEN_URL=...
export NAVET_OAUTH_CLIENT_ID=...
export NAVET_OAUTH_CLIENT_SECRET=...
export NAVET_API_GW_CLIENT_ID=...
export NAVET_API_GW_CLIENT_SECRET=...
export NAVET_ORGANISATIONSNUMMER=...
export NAVET_BESTALLNINGSIDENTITET=...
```

### Starta applikationen

Applikationen kan köras via IntelliJ eller med Maven:

```bash
./mvnw spring-boot:run
```

## Databas

Tjänsten använder PostgreSQL för att lagra citizens och genererade `partyId`.

Exempel på datamodell:

* `id`
* `municipality_id`
* `party_id`
* `person_number`
* `first_name`
* `last_name`
* `created_at`
* `updated_at`

## Nuvarande status

Tjänsten är i en fas där grundläggande infrastruktur för Navet-integration är på plats.

* [x] Projektstruktur uppsatt
* [x] Health endpoint fungerar
* [x] Navet-integration (REST + OAuth) implementerad
* [x] Validering av personnummer (Luhn) klar
* [x] Felmappning av Navet-fel till interna felkoder
* [x] Race-condition skydd vid persistens

## Nästa steg

* [ ] Konfigurera PostgreSQL för produktion
* [ ] Lägga till OpenAPI / Swagger dokumentation
* [ ] Förbättra enhetstester för NavetService
* [ ] Integrera med invånarportal
