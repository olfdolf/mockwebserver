Sql database creation:
---------------------
```sql
CREATE TABLE restmock (
    id bigserial primary key,
    status_code int,
    request_headers varchar,
    request_body bytea,
    response_headers varchar,
    response_body bytea,
    request_date timestamp,
    request_method varchar,
    remote_address varchar,
    request_path varchar,
    protocol varchar
)

CREATE TABLE user_defined_response (
    id bigserial primary key,
    status_code int,
    response_body bytea,
    mapping varchar UNIQUE,
    content_type varchar
)
```
