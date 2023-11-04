CREATE TABLE user_info (
    id         int          NOT NULL AUTO_INCREMENT,
    first_name   varchar(20)  NOT NULL,
    last_name   varchar(100) NOT NULL,
    email   varchar(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user (
    id         int          NOT NULL AUTO_INCREMENT,
    username   varchar(20)  NOT NULL,
    password   varchar(100) NOT NULL,
    user_info_id int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (username),
    FOREIGN KEY (user_info_id) REFERENCES user_info (id)
);

CREATE TABLE user_role (
    id        int         NOT NULL AUTO_INCREMENT,
    role_name varchar(50) NOT NULL,
    user_id   int         NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (role_name, user_id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);