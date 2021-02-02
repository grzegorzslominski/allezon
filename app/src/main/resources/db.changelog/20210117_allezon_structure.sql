CREATE TABLE "role"
(
    id   SERIAL,
    role VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

INSERT INTO "role"
VALUES (1, 'admin'),
       (2, 'user');

CREATE TABLE "user"
(
    id       SERIAL,
    role_id  INT     NOT NULL,
    email    VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT role_fk
        FOREIGN KEY (role_id)
            REFERENCES "role" (id)
);

CREATE TABLE category
(
    id   SERIAL,
    name VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE subcategory
(
    id          SERIAL,
    category_id INT     NOT NULL,
    name        VARCHAR NOT NULL UNIQUE,

    PRIMARY KEY (id),
    CONSTRAINT category_fk
        FOREIGN KEY (category_id)
            REFERENCES category (id)
);

CREATE TABLE auction
(
    id          SERIAL,
    author_id   INT     NOT NULL,
    subcategory_id INT     NOT NULL,
    title       VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    price       DECIMAL NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT user_fk
        FOREIGN KEY (author_id)
            REFERENCES "user" (id),
    CONSTRAINT subcategory_fk
        FOREIGN KEY (subcategory_id)
            REFERENCES subcategory (id)
);

CREATE TABLE auction_image
(
    id         SERIAL,
    auction_id INT     NOT NULL,
    url        VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk
        FOREIGN KEY (auction_id)
            REFERENCES auction (id)
);

CREATE TABLE parameter
(
    id   SERIAL,
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE auction_parameter
(
    id           SERIAL,
    auction_id   INT     NOT NULL,
    parameter_id INT     NOT NULL,
    value        VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk
        FOREIGN KEY (auction_id)
            REFERENCES auction (id),
    CONSTRAINT parameter_fk
        FOREIGN KEY (parameter_id)
            REFERENCES parameter (id)
);