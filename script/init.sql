INSERT INTO
    semester(label)
VALUES
    ('Semester 1'),
    ('Semester 2'),
    ('Semester 3'),
    ('Semester 4');

INSERT INTO
    session(session_date)
VALUES
    ('2025-01-01'),
    ('2025-12-01'),
    ('2026-01-15'),
    ('2026-12-15');

INSERT INTO program_option(label) VALUES
    ('Tronc commun'),
    ('Development'),
    ('Web and Design'),
    ('Network and Database');

INSERT INTO
    students(name, firstname, birth)
VALUES
    ('Doe', 'John', '2002-05-20');

INSERT INTO
    level(label)
VALUES
    ('L1'),
    ('L2'),
    ('L3');

-- SEMESTER 1
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Procedural Programming', 'INF101', 7, 0, 1, 1),
('HTML and Introduction to the Web', 'INF104', 5, 0, 1, 1),
('Basic Computer Science', 'INF107', 4, 0, 1, 1),
('Arithmetic and Numbers', 'MTH101', 4, 0, 1, 1),
('Mathematical Analysis', 'MTH102', 6, 0, 1, 1),
('Communication Techniques', 'ORG101', 4, 0, 1, 1);

-- SEMESTER 2
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Relational Databases', 'INF102', 5, 0, 2, 1),
('System Administration Basics', 'INF103', 5, 0, 2, 1),
('Hardware and Software Maintenance', 'INF105', 4, 0, 2, 1),
('Advanced Programming', 'INF106', 6, 0, 2, 1),
('Vector and Matrix Calculus', 'MTH103', 6, 0, 2, 1),
('Probability and Statistics', 'MTH105', 4, 0, 2, 1);

-- SEMESTER 3
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Object-Oriented Programming', 'INF201', 6, 0, 3, 1),
('Object Databases', 'INF202', 6, 0, 3, 1),
('System Programming', 'INF203', 4, 0, 3, 1),
('Computer Networks', 'INF208', 6, 0, 3, 1),
('Numerical Methods', 'MTH201', 4, 0, 3, 1),
('Management Basics', 'ORG201', 4, 0, 3, 1);

-- SEMESTER 4 - Option Développement
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Geographic Information System', 'INF204', 6, 1, 4, 2),
('Information System', 'INF205', 6, 1, 4, 2),
('Human-Machine Interface', 'INF206', 6, 1, 4, 2),
('Algorithm Elements', 'INF207', 6, 0, 4, 2),
('Mini Development Project', 'INF210', 10, 0, 4, 2),
('Geometry', 'MTH204', 4, 2, 4, 2),
('Differential Equations', 'MTH205', 4, 2, 4, 2),
('Optimization', 'MTH206', 4, 2, 4, 2),
('MAO', 'MTH203', 4, 0, 4, 2);

-- SEMESTER 4 - Option Web et Design
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Geographic Information System', 'INF204', 6, 1, 4, 3),
('Information System', 'INF205', 6, 1, 4, 3),
('Human-Machine Interface', 'INF206', 6, 1, 4, 3),
('Dynamic Web', 'INF209', 6, 0, 4, 3),
('Mini Project: Web and Design', 'INF212', 10, 0, 4, 3),
('Data Analysis', 'MTH202', 4, 2, 4, 3),
('Geometry', 'MTH204', 4, 2, 4, 3),
('Optimization', 'MTH206', 4, 2, 4, 3),
('MAO', 'MTH203', 4, 0, 4, 3);

-- SEMESTER 4 - Option Bases de Données et Réseaux
INSERT INTO subject(title, ue, credit, code, id_semester, id_program_option) VALUES
('Information System', 'INF205', 6, 0, 4, 4),
('Geographic Information System', 'INF204', 6, 1, 4, 4),
('Human-Machine Interface', 'INF206', 6, 1, 4, 4),
('Algorithm Elements', 'INF207', 6, 1, 4, 4),
('Mini Project: Databases/Networks', 'INF211', 10, 0, 4, 4),
('Data Analysis', 'MTH202', 4, 2, 4, 4),
('Differential Equations', 'MTH205', 4, 2, 4, 4),
('Optimization', 'MTH206', 4, 2, 4, 4),
('MAO', 'MTH203', 4, 0, 4, 4);


