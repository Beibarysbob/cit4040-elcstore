# Checklist before submission

- [ ] `docker compose --profile api up --build -d` creates PostgreSQL + API without error.
- [ ] `docker compose exec postgres psql -U shop -d shopdb -c "select count(*) from products"` prints 20.
- [ ] `mvn clean test` succeeds with Java 21.
- [ ] `GET /api/products` returns a paginated JSON object containing seeded products.
- [ ] Customer and admin login both produce JWTs.
- [ ] `GET /api/users/me` gives HTTP 401 without an access token.
- [ ] `GET /api/admin/orders` gives HTTP 403 with a CUSTOMER token.
- [ ] Customer cannot open another user's order or address by changing ID.
- [ ] Password hashes/SQL errors/stack traces never appear in responses.
- [ ] Invalid request fields produce HTTP 400 with `details`.
- [ ] Unknown product returns standard HTTP 404 body.
- [ ] Admin can add/modify/soft delete a product.
- [ ] Review can be created, updated, deleted by owner; rating is recalculated.
- [ ] Cart current price, wishlist and promo code are functional.
- [ ] A complete order correctly computes subtotal, discount and shipping.
- [ ] Stock reserves on order, releases on cancel, reduces on deliver.
- [ ] Order status rejects illegal transitions.
- [ ] Four defined test cards and the random fifth scenario behave as designed.
- [ ] Postman collection imports with environment; all useful scenarios reviewed individually.
- [ ] `docs/er_diagram.png` shows relations for all required entities.
- [ ] `.env` is excluded by `.gitignore`; no real emails/passwords/secrets in repository.
- [ ] Fresh clone of repository can be started following README, on a machine other than yours.

**Not yet verified in the generation environment:** Maven test/build, real PostgreSQL schema validation, Spring Boot HTTP integration, Postman end-to-end execution. Tick each only after you test it.
