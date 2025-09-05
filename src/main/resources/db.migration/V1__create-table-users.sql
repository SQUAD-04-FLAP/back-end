CREATE TABLE tb_users (
    id TEXT PRIMARY KEY UNIQUE,
    login TEXT NOT NULL UNIQUE,
    password TEXT,
    role TEXT NOT NULL,
    provider TEXT NOT NULL,
    password_reset_code TEXT,
    password_reset_code_expiration TIMESTAMP WITHOUT TIME ZONE
);