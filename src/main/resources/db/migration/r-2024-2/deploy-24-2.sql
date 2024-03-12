ALTER TABLE person
    ADD COLUMN username varchar NOT NULL UNIQUE DEFAULT '',
    ADD COLUMN password varchar NOT NULL        DEFAULT '',
    ADD COLUMN role     varchar NOT NULL        DEFAULT '';

ALTER TABLE person
    ALTER COLUMN username DROP DEFAULT,
    ALTER COLUMN password DROP DEFAULT,
    ALTER COLUMN role DROP DEFAULT;