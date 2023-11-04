CREATE TABLE route_history
(
    id     int NOT NULL AUTO_INCREMENT,
    user_id      int NOT NULL,
    origin      varchar(100),
    destination varchar(100),
    length      decimal,
    duration    int,
    date        DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
)