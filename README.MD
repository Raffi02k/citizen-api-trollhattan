
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

Exempel:
`GET /1488/199001011234/guid`

Exempel på svar:
```json
{
  "partyId": "550e8400-e29b-41d4-a716-446655440000"
}
````

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
│   │   ├── service/
│   │   ├── integration/
│   │   ├── configuration/
│   │   ├── exception/
│   │   └── util/
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
```

## Hur tjänsten passar in i lösningen

Tjänsten är tänkt att användas av invånarportalen på följande sätt:

1. Invånaren loggar in via SSO/SAML
2. Portalen får ett `citizenIdentifier`
3. Portalens backend anropar Citizen API
4. Citizen API returnerar ett `partyId`
5. Portalens backend sparar detta i användarsessionen
6. Övriga API-anrop använder `partyId` för att hämta rätt data

## Lokal utveckling

### Starta applikationen

Applikationen kan köras via IntelliJ eller med Maven:

```bash
./mvnw spring-boot:run
```

### Bygg projektet

```bash
./mvnw clean install
```

### Kör tester

```bash
./mvnw test
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

Detta projekt är under uppbyggnad och befinner sig i en första uppstartsfas.

Nuvarande fokus är att:

* få Spring Boot-projektet att starta korrekt
* bygga första health endpoint
* skapa första versionen av citizen-uppslagning
* göra tjänsten kompatibel med invånarportalens login-flöde

## Nästa steg

* lägga till `application.yml`
* konfigurera PostgreSQL
* skapa första Flyway-migrering
* bygga `Citizen` entity och repository
* implementera `/guid` endpoint
* koppla portalen mot detta API
* verifiera end-to-end login-flöde

## Ägare

Projektet utvecklas som en del av arbetet med att anpassa invånarportalen till Trollhättans stad.

## Notering

Detta Citizen API är en Trollhättan-specifik implementation och är inte samma tjänst som Sundsvalls kommuns Citizen API, även om syftet och integrationsmönstret är liknande.

````
