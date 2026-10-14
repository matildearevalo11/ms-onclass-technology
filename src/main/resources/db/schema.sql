CREATE TABLE IF NOT EXISTS technologies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_ci NOT NULL,
    description VARCHAR(90) CHARACTER SET utf8mb4 NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_technologies_name UNIQUE (name),
    CONSTRAINT ck_technologies_name CHECK (CHAR_LENGTH(TRIM(name)) > 0),
    CONSTRAINT ck_technologies_description CHECK (CHAR_LENGTH(TRIM(description)) > 0)
);
