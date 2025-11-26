CREATE TABLE IF NOT EXISTS program_prioritas (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    program_prioritas   VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
