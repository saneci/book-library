UPDATE person
SET username = '', password = ''
WHERE username IS NULL OR password IS NULL;

ALTER TABLE person
    ALTER COLUMN username
        SET NOT NULL;

ALTER TABLE person
    ALTER COLUMN password
        SET NOT NULL;

ALTER TABLE person
    ALTER COLUMN username
        SET NOT NULL;