DROP TABLE IF EXISTS USER_TABLE CASCADE;
DROP TABLE IF EXISTS CATEGORY_TABLE CASCADE;
DROP TABLE IF EXISTS EVENT_TABLE CASCADE;
DROP TABLE IF EXISTS REQUEST_TABLE CASCADE;
DROP TABLE IF EXISTS COMPILATION_TABLE CASCADE;
DROP TABLE IF EXISTS EVENT_COMPILATION_TABLE CASCADE;
DROP TABLE IF EXISTS USER_LIKE_EVENT_TABLE CASCADE;
DROP TABLE IF EXISTS LIKE_DISLIKE_TOTAL_TABLE CASCADE;

CREATE TABLE IF NOT EXISTS USER_TABLE
(
    ID    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME  TEXT,
    EMAIL TEXT
);

CREATE TABLE IF NOT EXISTS CATEGORY_TABLE
(
    ID   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME TEXT
);

CREATE TABLE IF NOT EXISTS EVENT_TABLE
(
    ID                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ANNOTATION         TEXT,
    DESCRIPTION        TEXT,
    CATEGORY_ID        BIGINT,
    EVENT_DATE         DATETIME,
    INITIATOR_ID       BIGINT,
    LATITUDE           FLOAT,
    LONGITUDE          FLOAT,
    PAID               BOOLEAN,
    PARTICIPANT_LIMIT  BIGINT,
    PUBLISHED_ON       DATETIME,
    CREATED_ON         DATETIME,
    REQUEST_MODERATION BOOLEAN,
    TITLE              TEXT,
    STATUS             VARCHAR(32),
    CONFIRMED_REQUESTS BIGINT,
    FOREIGN KEY (INITIATOR_ID) REFERENCES USER_TABLE (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REQUEST_TABLE
(
    ID           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    CREATED      DATETIME,
    EVENT_ID     BIGINT,
    REQUESTER_ID BIGINT,
    STATUS       VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS COMPILATION_TABLE
(
    ID     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PINNED BOOLEAN,
    TITLE  VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS EVENT_COMPILATION_TABLE
(
    COMPILATION_ID BIGINT,
    EVENT_ID       BIGINT,

    FOREIGN KEY (COMPILATION_ID) REFERENCES COMPILATION_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT_TABLE (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_LIKE_EVENT_TABLE
(
    EVENT_ID BIGINT,
    USER_ID  BIGINT,
    IS_LIKE  BOOLEAN,
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USER_TABLE (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS LIKE_DISLIKE_TOTAL_TABLE
(
    EVENT_ID BIGINT UNIQUE,
    LIKE_TOTAL BIGINT DEFAULT 0,
    DISLIKE_TOTAL BIGINT DEFAULT 0,
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT_TABLE (ID) ON DELETE CASCADE
);
