DROP
DATABASE IF EXISTS nms_lite;

CREATE
DATABASE nms_lite
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE
nms_lite;

DROP TABLE IF EXISTS registered_sim CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS message_history CASCADE;

CREATE TABLE registered_sim
(
    id          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    imsi        VARCHAR(20)  NOT NULL UNIQUE,
    msisdn      VARCHAR(255) NOT NULL UNIQUE,
    tc          VARCHAR(100) NULL,
    address     VARCHAR(255) NULL,
    full_name   VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    user_name   VARCHAR(255) NOT NULL,
    created     DATETIME     NOT NULL,
    deactivated DATETIME NULL
);

CREATE TABLE message_history
(
    id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    body         VARCHAR(255) NOT NULL,
    message_type VARCHAR(125) NOT NULL,
    message_from VARCHAR(255) NOT NULL,
    message_to   VARCHAR(255) NULL,
    created      DATETIME     NOT NULL,
    seen         BOOLEAN NULL
);

CREATE TABLE location
(
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    imsi              VARCHAR(20)  NOT NULL,
    latitude          FLOAT        NOT NULL,
    longitude         FLOAT        NOT NULL,
    altitude          FLOAT NULL,
    accuracy          FLOAT NULL,
    altitude_accuracy FLOAT NULL,
    heading           FLOAT NULL,
    speed             FLOAT NULL,
    reason            VARCHAR(150) NOT NULL
);