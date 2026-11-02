# TravelApp – Part 04: Auth

A trip-planning REST API built with Spring Boot, Spring Data JPA, PostgreSQL and JWT authentication.
It follows the same structure as FoodApp: **Trip** plays the role of Category and **Activity** plays the role of Recipe.

| FoodApp | TravelApp |
|---|---|
| Category (name, description, image) | Trip (name, destination, description, start/end date, cover photo) |
| Recipe (name, time, portions, ingredients, steps) | Activity (name, day, time, location, cost, notes, booked) |
| `/api/categories/{id}/recipes` | `/api/trips/{tripId}/activities` |

## Setup
1. Create a PostgreSQL database called `travel` (the app expects it on port `5434`, user `postgres`, password `12345678`; change these in `application.properties`).
2. Run `TravelApplication`, then open `GET http://localhost:8080/hello` to check it's alive (this one needs a token too).

## Endpoints

### Auth (public)
| Method | URL | Body |
|---|---|---|
| POST | `/auth/users/register` | `{ "username": "sara", "emailAddress": "sara@mail.com", "password": "secret123" }` |
| POST | `/auth/users/login` | `{ "email": "sara@mail.com", "password": "secret123" }` → returns the JWT in `message` |

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

## Error responses
- `404 Not Found` – trip or activity doesn't exist (or belongs to another user)
- `409 Conflict` – you already have a trip with that name, or the email is already registered
