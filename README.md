# TravelApp

A trip-planning REST API built with Spring Boot, Spring Data JPA, PostgreSQL and JWT authentication.
Users sign up, log in, plan **trips** (with an optional cover photo) and add **activities** to each trip, day by day.

## Getting started
1. Clone the repo and open it in IntelliJ.
2. Create a PostgreSQL database called `travel`. The app expects it on port `5432`, with user `<your user>` and password `<your password>`. Change these in `src/main/resources/application.properties` if your setup is different.
3. Run `TravelApplication`. The tables are created automatically, and the sample data below is added on the first run.
4. Log in with one of the sample accounts, copy the token, and send it with your requests.

## Sample accounts
The app comes with seed data, so you can start testing straight away:

| Login email | Password | Trips |
|---|---|---|
| sara@mail.com | password123 | Winter in Tokyo (6 activities), Weekend in Dubai (3) |
| ali@mail.com | password123 | Istanbul Food Tour (5), Hiking in Oman (4) |
| noor@mail.com | password123 | Paris with Family (5) |

Each user only sees their own trips. Try logging in as Sara and asking for one of Ali's trips: you'll get a `404`.

To reset the data, drop the tables (or the `travel` database) and restart the app.

## Project structure
```
com.ga.travel
├── controller   → REST endpoints (UserController, TripController, ActivityController)
├── service      → business logic (UserService, TripService, ActivityService)
├── repository   → database access (Spring Data JPA)
├── model        → entities: User, UserProfile, Trip, Activity
├── security     → JWT login and request filter
├── exception    → 404 and 409 errors
└── seed         → DataSeeder (sample data)
```

## Endpoints

### Auth (public)
| Method | URL | Body |
|---|---|---|
| POST | `/auth/users/register` | `{ "username": "sara", "emailAddress": "sara@mail.com", "password": "password123" }` |
| POST | `/auth/users/login` | `{ "email": "sara@mail.com", "password": "password123" }` → returns the JWT in `message` |

Every other request needs the header `Authorization: Bearer <token>`.

### Trips (only your own trips)
| Method | URL | Body |
|---|---|---|
| POST | `/api/trips` | **form-data**: `name`, `destination`, `description`, `startDate` (2026-12-20), `endDate` (2026-12-27), `image` (file, optional) |
| GET | `/api/trips` | – |
| GET | `/api/trips/{tripId}` | – |
| PUT | `/api/trips/{tripId}` | `{ "name": "Winter in Tokyo", "destination": "Tokyo, Japan", "description": "Ramen tour", "startDate": "2026-12-20", "endDate": "2026-12-28" }` |
| DELETE | `/api/trips/{tripId}` | – |

### Activities (inside a trip)
| Method | URL | Body |
|---|---|---|
| POST | `/api/trips/{tripId}/activities` | `{ "name": "Visit Senso-ji Temple", "day": 1, "time": "09:30", "location": "Asakusa", "cost": 0, "notes": "Go early", "booked": false }` |
| GET | `/api/trips/{tripId}/activities` | – (sorted by day, then time) |
| GET | `/api/trips/{tripId}/activities/{activityId}` | – |
| PUT | `/api/trips/{tripId}/activities/{activityId}` | same shape as POST |
| DELETE | `/api/trips/{tripId}/activities/{activityId}` | – |

### Health check
`GET /hello` returns "All good so far, ready for takeoff". It needs a token too, so it's a quick way to check your login works.

## Error responses
- `401 Unauthorized`: missing or invalid token
- `404 Not Found`: the trip or activity doesn't exist, or belongs to another user
- `409 Conflict`: you already have a trip with that name, or the email is already registered
