CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT
);

CREATE TABLE phote (
    id SERIAL PRIMARY KEY
);

ALTER TABLE candidate ADD COLUMN photoid integer;

CREATE TABLE users(
   id SERIAL PRIMARY KEY,
   login TEXT UNIQUE NOT NULL,
   email TEXT UNIQUE NOT NULL,
  password TEXT UNIQUE NOT NULL
);

CREATE TABLE city(
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

ALTER TABLE candidate ADD COLUMN city_id integer;

INSERT INTO city (name) VALUES ('Moscow');
INSERT INTO city (name) VALUES ('Sochi');
INSERT INTO city (name) VALUES ('Vladivostok');

