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
