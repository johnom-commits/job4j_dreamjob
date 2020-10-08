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