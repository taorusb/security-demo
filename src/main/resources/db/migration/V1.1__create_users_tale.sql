CREATE TABLE users
(
    id                        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name                VARCHAR(255),
    last_name                 VARCHAR(255),
    email                     VARCHAR(255) UNIQUE,
    CONSTRAINT pk_users PRIMARY KEY (id)
)