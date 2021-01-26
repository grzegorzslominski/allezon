CREATE TABLE "role"
(
    id   INT,
    role VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

INSERT INTO "role"
VALUES (1, 'admin'),
       (2, 'user');

CREATE TABLE "user"
(
    id       INT,
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
    id   INT,
    name VARCHAR NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE subcategory
(
    id          INT,
    category_id INT     NOT NULL,
    name        VARCHAR NOT NULL UNIQUE,

    PRIMARY KEY (id),
    CONSTRAINT section_fk
        FOREIGN KEY (category_id)
            REFERENCES category (id)
);

CREATE TABLE auction
(
    id          INT,
    author_id   INT     NOT NULL,
    category_id INT     NOT NULL,
    title       VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    price       DECIMAL NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT user_fk
        FOREIGN KEY (author_id)
            REFERENCES "user" (id),
    CONSTRAINT category_fk
        FOREIGN KEY (category_id)
            REFERENCES category (id)
);

CREATE TABLE auction_image
(
    id         INT,
    auction_id INT     NOT NULL,
    url        VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk
        FOREIGN KEY (auction_id)
            REFERENCES auction (id)
);

CREATE TABLE parameter
(
    id   INT,
    name VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE auction_parameter
(
    id           INT,
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