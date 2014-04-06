CREATE TABLE activity
(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(64) NOT NULL,
    type_id INT,
    sub_type_id INT,
    description VARCHAR(2048)
);
CREATE TABLE exercise
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
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
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    birth DATE,
    height INT,
    name VARCHAR(64) NOT NULL
);
CREATE TABLE person_stats
(
    person_id INT NOT NULL,
    measure_date DATETIME NOT NULL,
    weight DOUBLE NOT NULL
);
CREATE TABLE training
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
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
CREATE INDEX FK_ACTIVITY_TRAIN_TYPE ON activity ( type_id );
CREATE INDEX FK_ACTIVITY_SUB_TYPE ON activity ( sub_type_id );
ALTER TABLE exercise ADD FOREIGN KEY ( training_id ) REFERENCES training ( id );
ALTER TABLE exercise ADD FOREIGN KEY ( activity_id ) REFERENCES activity ( id );
CREATE INDEX FK_EXERCISE_TRAINING ON exercise ( training_id );
CREATE INDEX FK_EXERCISE_ACTIVITY ON exercise ( activity_id );
ALTER TABLE exercise_set ADD FOREIGN KEY ( exercise_id ) REFERENCES exercise ( id );
CREATE INDEX FK_EXERCISE_SET_EXERCISE ON exercise_set ( exercise_id );
ALTER TABLE person_stats ADD FOREIGN KEY ( person_id ) REFERENCES person ( id );
CREATE INDEX FK_PERSON_STATS_PERSON ON person_stats ( person_id );
ALTER TABLE training ADD FOREIGN KEY ( type_id ) REFERENCES training_type ( id );
ALTER TABLE training ADD FOREIGN KEY ( person_id ) REFERENCES person ( id );
CREATE INDEX FK_TRAINING_PERSON ON training ( person_id );
CREATE INDEX FK_TRAINING_TRAINING_TYPE ON training ( type_id );
ALTER TABLE training_sub_type ADD FOREIGN KEY ( type_id ) REFERENCES training_type ( id );
CREATE INDEX FK_TRAINING_SUB_TYPE_TYPE ON training_sub_type ( type_id );
