
# Citizen API - Trollhättans stad

Citizen API är en mikrotjänst för att hantera grundläggande invånaridentitet i Trollhättans stads integrationsmiljö.

Den här tjänsten används som en identitetsbrygga mellan inloggning via SSO/SAML och interna kommunala tjänster som behöver ett stabilt internt id för en invånare, till exempel `partyId`.

## Syfte

Målet med tjänsten är att översätta en invånares identitet, till exempel personnummer eller `citizenIdentifier`, till ett internt och stabilt `partyId` som andra system och API:er kan använda.

Detta behövs för att koppla en inloggad användare i invånarportalen till rätt ärenden, beslut, dokument och andra tjänster.

## Bakgrund

I den klonade lösningen från Sundsvalls kommun används ett externt Citizen API i login-flödet.  
Efter att användaren loggat in via SAML hämtar portalen ett `citizenIdentifier` och anropar Citizen API för att få tillbaka ett internt `partyId/guid`.

Trollhättans stad har i nuläget inte ett motsvarande Citizen API, därför byggs denna mikrotjänst från grunden.

## Ansvar i första versionen

Den första versionen av Citizen API fokuserar på minsta möjliga funktionalitet som krävs för att stödja invånarportalens login-flöde.

Tjänsten ska kunna:

- ta emot ett personnummer eller citizenIdentifier
- slå upp om invånaren redan finns registrerad
- skapa en ny invånare om den inte finns
- returnera ett stabilt `partyId`
- exponera ett enkelt REST-API för uppslagning

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

### Hämta guid/partyId från personnummer
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

Tjänsten mot Navet använder Skatteverkets REST API (v3/hamta).

### Autentisering och säkerhet
- **OAuth 2.0**: Använder Client Credentials Grant (CCG) för att hämta access tokens.
- **Token Caching**: Tokens cachas i minnet med en säkerhetsmarginal för att undvika onödiga anrop till token-tjänsten.
- **API Gateway**: Kräver `client_id` och `client_secret` som headers för anrop via Trollhättans API Gateway.
- **Correlation ID**: Skickar `skv_client_correlation_id` för att möjliggöra spårbarhet hos Skatteverket.

### Konfiguration
Alla anslutningsuppgifter sköts via miljövariabler i `application.yml` (se sektion för lokal utveckling).

## Lokal utveckling

### Miljövariabler
För att köra mot Navets testmiljö behövs följande variabler (kontakta IT-avdelningen för aktuella värden):

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

I senare steg kommer tjänsten att använda PostgreSQL för att lagra citizens och genererade `partyId`.

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

Tjänsten är nu i en fas där grundläggande infrastruktur för Navet-integration är på plats.

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
* [ ] Integrera med Trollhättans portal (Mina sidor)

## Ägare

Projektet utvecklas som en del av arbetet med att anpassa invånarportalen till Trollhättans stad.

## Notering

Detta Citizen API är en Trollhättan-specifik implementation och är inte samma tjänst som Sundsvalls kommuns Citizen API, även om syftet och integrationsmönstret är liknande.
