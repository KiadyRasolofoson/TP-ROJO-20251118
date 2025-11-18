CREATE TABLE students (
    id_student COUNTER,
    name VARCHAR(50),
    firstname VARCHAR(50),
    birth DATE,
    PRIMARY KEY(id_student)
);

CREATE TABLE semester (
    id_semester COUNTER,
    label VARCHAR(50) NOT NULL,
    PRIMARY KEY(id_semester),
    UNIQUE(label)
);

CREATE TABLE session (
    id_session COUNTER,
    session_date DATE,
    PRIMARY KEY(id_session)
);

CREATE TABLE subject (
    id_subject COUNTER,
    title VARCHAR(50),
    ue VARCHAR(50),
    credit INT,
    code INT,
    id_semester INT NOT NULL,
    PRIMARY KEY(id_subject),
    UNIQUE(ue),
    FOREIGN KEY(id_semester) REFERENCES semester(id_semester)
);

CREATE TABLE level (
    id_level COUNTER,
    label VARCHAR(50),
    PRIMARY KEY(id_level),
    UNIQUE(label)
);

CREATE TABLE program_option (
    id_option COUNTER,
    label VARCHAR(50) NOT NULL,
    PRIMARY KEY(id_option),
    UNIQUE(label)
);

CREATE TABLE progress (
    id_progress COUNTER,
    id_option INT NOT NULL,
    id_level INT,
    id_semester INT NOT NULL,
    id_session INT NOT NULL,
    id_student INT NOT NULL,
    PRIMARY KEY(id_progress),
    FOREIGN KEY(id_option) REFERENCES program_option(id_option),
    FOREIGN KEY(id_level) REFERENCES level(id_level),
    FOREIGN KEY(id_semester) REFERENCES semester(id_semester),
    FOREIGN KEY(id_session) REFERENCES session(id_session),
    FOREIGN KEY(id_student) REFERENCES students(id_student)
);

CREATE TABLE grade (
    id_grade COUNTER,
    grade DECIMAL(15, 2),
    id_subject INT NOT NULL,
    id_progress INT NOT NULL,
    PRIMARY KEY(id_grade),
    FOREIGN KEY(id_subject) REFERENCES subject(id_subject),
    FOREIGN KEY(id_progress) REFERENCES progress(id_progress)
);