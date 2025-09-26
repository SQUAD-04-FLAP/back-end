CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    permissao VARCHAR(50) NOT NULL,
    provedor VARCHAR(50) NOT NULL,
    reset_code VARCHAR(15),
    reset_code_expiry TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

ALTER SEQUENCE usuarios_id_usuario_seq RESTART WITH 1000;