CREATE TABLE activity
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    type_id INT,
    sub_type_id INT,
    description VARCHAR(2048)
);
CREATE TABLE exercise
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    activity_id INT,
    training_id BIGINT
);
CREATE TABLE exercise_set
(
    exercise_id BIGINT NOT NULL,
    duration VARCHAR(255),
    reps INT,
    result DOUBLE,
    note VARCHAR(255)
);
CREATE TABLE person
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    birth DATE,
    height INT,
    name VARCHAR(64) NOT NULL
);
CREATE TABLE person_stats
(
    person_id INT NOT NULL,
    measureDate DATETIME NOT NULL,
    weight DOUBLE NOT NULL
);
CREATE TABLE training
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    training_start DATETIME,
    training_stop DATETIME,
    person_id INT,
    type_id INT
);
CREATE TABLE training_sub_type
(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(64) NOT NULL,
    type_id INT
);
CREATE TABLE training_type
(
    id INT PRIMARY KEY NOT NULL,
    code VARCHAR(32) NOT NULL,
    name VARCHAR(64) NOT NULL
);
ALTER TABLE activity ADD FOREIGN KEY ( type_id ) REFERENCES training_type ( id );
ALTER TABLE activity ADD FOREIGN KEY ( sub_type_id ) REFERENCES training_sub_type ( id );
CREATE INDEX FK_pqxm9f3ypyey3wrq7n44e2ya8 ON activity ( type_id );
CREATE INDEX FK_qqvx1tfkgi33uj3ex1pem697b ON activity ( sub_type_id );
ALTER TABLE exercise ADD FOREIGN KEY ( training_id ) REFERENCES training ( id );
ALTER TABLE exercise ADD FOREIGN KEY ( activity_id ) REFERENCES activity ( id );
CREATE INDEX FK_j230itlb7ojhj1bmsk07aycy1 ON exercise ( training_id );
CREATE INDEX FK_npoqmm5wvjjxdgx4eewdqhk40 ON exercise ( activity_id );
ALTER TABLE exercise_set ADD FOREIGN KEY ( exercise_id ) REFERENCES exercise ( id );
CREATE INDEX FK_essc6n801kwd7n9y4oce1twhv ON exercise_set ( exercise_id );
ALTER TABLE person_stats ADD FOREIGN KEY ( person_id ) REFERENCES person ( id );
CREATE INDEX FK_9gog3a7vqh1jm67cg8ijk7b3m ON person_stats ( person_id );
ALTER TABLE training ADD FOREIGN KEY ( type_id ) REFERENCES training_type ( id );
ALTER TABLE training ADD FOREIGN KEY ( person_id ) REFERENCES person ( id );
CREATE INDEX FK_37j18w1l539w27ynfi17dbe4n ON training ( person_id );
CREATE INDEX FK_hwfldqahsq3f7wv8k29wpfwkq ON training ( type_id );
ALTER TABLE training_sub_type ADD FOREIGN KEY ( type_id ) REFERENCES training_type ( id );
CREATE INDEX FK_97gvlac52tdhbofn3chjl5sjn ON training_sub_type ( type_id );
