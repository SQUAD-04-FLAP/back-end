CREATE TABLE tb_users (
    id TEXT PRIMARY KEY UNIQUE,
    login TEXT NOT NULL UNIQUE,
    password TEXT,
    role TEXT NOT NULL,
    provider TEXT NOT NULL
);