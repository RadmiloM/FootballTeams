CREATE TABLE IF NOT EXISTS users (
id SERIAL PRIMARY KEY,
name VARCHAR UNIQUE,
password VARCHAR,
role VARCHAR,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL
);