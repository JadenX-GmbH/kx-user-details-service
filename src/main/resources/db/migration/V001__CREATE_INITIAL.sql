CREATE TABLE user
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    uid              char(36) NULL,
    identifier       VARCHAR(150) NULL,
    type             VARCHAR(100) NOT NULL,
    public_address   VARCHAR(100) NULL,
    tag_line         VARCHAR(255) NULL,
    `description`    LONGTEXT NULL,
    user_photo       VARCHAR(255) NULL,
    background_photo VARCHAR(255) NULL,
    is_active        BIT(1) NULL,
    date_created     timestamp    NOT NULL,
    last_updated     timestamp    NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE skillset
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    maturity     VARCHAR(100) NULL,
    user_id      BIGINT     NULL,
    skill_id     BIGINT    NOT NULL,
    date_created timestamp NOT NULL,
    last_updated timestamp NOT NULL,
    CONSTRAINT PK_SKILLSET PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE skill
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(100) NOT NULL,
    category_id  BIGINT       NOT NULL,
    date_created timestamp    NOT NULL,
    last_updated timestamp    NOT NULL,
    CONSTRAINT PK_SKILL PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE category
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(100) NOT NULL,
    date_created timestamp    NOT NULL,
    last_updated timestamp    NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE address
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(100) NOT NULL,
    surname      VARCHAR(150) NOT NULL,
    address      VARCHAR(150) NOT NULL,
    supplement   VARCHAR(100) NOT NULL,
    postal_code  VARCHAR(15)  NOT NULL,
    city         VARCHAR(100) NULL,
    country      VARCHAR(50)  NOT NULL,
    user_id      BIGINT       NOT NULL,
    date_created timestamp    NOT NULL,
    last_updated timestamp    NOT NULL,
    CONSTRAINT PK_ADDRESS PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

CREATE TABLE details
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         VARCHAR(100) NULL,
    surname      VARCHAR(150) NULL,
    email        VARCHAR(150) NOT NULL,
    phone        VARCHAR(50) NULL,
    nationality  VARCHAR(100) NULL,
    education    VARCHAR(255) NULL,
    degree       VARCHAR(255) NULL,
    user_id      BIGINT       NOT NULL,
    date_created timestamp    NOT NULL,
    last_updated timestamp    NOT NULL,
    CONSTRAINT PK_DETAILS PRIMARY KEY (id)
) AUTO_INCREMENT=10000;

ALTER TABLE user
    ADD CONSTRAINT unique_user_uid UNIQUE (uid);

ALTER TABLE user
    ADD CONSTRAINT unique_user_identifier UNIQUE (identifier);

ALTER TABLE skillset
    ADD CONSTRAINT fk_skillset_user_id FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE skillset
    ADD CONSTRAINT fk_skillset_skill_id FOREIGN KEY (skill_id) REFERENCES skill (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE skill
    ADD CONSTRAINT fk_skill_category_id FOREIGN KEY (category_id) REFERENCES category (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE skill
    ADD CONSTRAINT unique_skill_name UNIQUE (name);

ALTER TABLE category
    ADD CONSTRAINT unique_category_name UNIQUE (name);

ALTER TABLE address
    ADD CONSTRAINT fk_address_user_id FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE details
    ADD CONSTRAINT fk_details_user_id FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE details
    ADD CONSTRAINT unique_details_email UNIQUE (email);


