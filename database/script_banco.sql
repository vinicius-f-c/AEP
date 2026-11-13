CREATE DATABASE IF NOT EXISTS reciclagem_eletronicos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE reciclagem_eletronicos;

DROP TABLE IF EXISTS item_eletronico;
DROP TABLE IF EXISTS doador;

CREATE TABLE doador (
    id            BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    nome_completo VARCHAR(150)     NOT NULL,
    email         VARCHAR(150)     NOT NULL,
    telefone      VARCHAR(20)      NOT NULL,
    cep           CHAR(8)          NOT NULL,
    logradouro    VARCHAR(150)     NOT NULL,
    numero        VARCHAR(10)      NOT NULL,
    complemento   VARCHAR(80)      NULL,
    bairro        VARCHAR(80)      NOT NULL,
    cidade        VARCHAR(80)      NOT NULL,
    uf            CHAR(2)          NOT NULL,
    data_cadastro DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_doador PRIMARY KEY (id),
    CONSTRAINT uq_doador_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE item_eletronico (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    doador_id           BIGINT UNSIGNED NOT NULL,
    tipo_equipamento    VARCHAR(60)     NOT NULL,
    marca               VARCHAR(60)     NOT NULL,
    modelo              VARCHAR(80)     NOT NULL,
    estado_conservacao  ENUM('OTIMO','BOM','REGULAR','RUIM','SUCATA') NOT NULL,
    status_rastreamento ENUM(
                            'AGUARDANDO_COLETA',
                            'COLETADO',
                            'EM_TRIAGEM',
                            'EM_RECICLAGEM',
                            'RECICLADO'
                        ) NOT NULL DEFAULT 'AGUARDANDO_COLETA',
    data_cadastro       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_item_eletronico PRIMARY KEY (id),
    CONSTRAINT fk_item_doador FOREIGN KEY (doador_id)
        REFERENCES doador (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    INDEX idx_item_doador (doador_id),
    INDEX idx_item_status (status_rastreamento)
) ENGINE=InnoDB;
