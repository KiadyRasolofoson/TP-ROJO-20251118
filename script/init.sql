-- =====================================
-- 1️⃣ Insert Semesters
-- =====================================
INSERT INTO semester(label) VALUES
    ('Semester 1'),
    ('Semester 2'),
    ('Semester 3'),
    ('Semester 4');

-- =====================================
-- 2️⃣ Insert Sessions
-- =====================================
INSERT INTO session(session_date) VALUES
    ('2025-01-01'),
    ('2025-12-01'),
    ('2026-01-15'),
    ('2026-12-15');

-- =====================================
-- 3️⃣ Insert Program Options
-- =====================================
INSERT INTO program_option(label) VALUES
    ('Tronc commun'),
    ('Development'),
    ('Web and Design'),
    ('Network and Database');

-- =====================================
-- 4️⃣ Insert Levels
-- =====================================
INSERT INTO level(label) VALUES
    ('L1'),
    ('L2'),
    ('L3');

-- =====================================
-- 5️⃣ Insert Students
-- =====================================
INSERT INTO students(name, firstname, birth) VALUES
    ('Doe', 'John', '2002-05-20'),
    ('Smith', 'Alice', '2003-03-15');

-- =====================================
-- 6️⃣ Insert Subjects
-- SEMESTER 1
-- =====================================
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

-- SEMESTER 4 - Development
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

-- SEMESTER 4 - Web and Design
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

-- SEMESTER 4 - Network and Database
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

-- =====================================
-- 7️⃣ Insert Progress for both students (all semesters)
-- =====================================
-- John Doe (id_student = 1)
INSERT INTO progress(id_option, id_level, id_semester, id_session, id_student) VALUES
(1, 1, 1, 1, 1), -- S1 L1
(1, 1, 2, 2, 1), -- S2 L1
(1, 2, 3, 3, 1), -- S3 L2
(2, 2, 4, 4, 1); -- S4 Development L2

-- Alice Smith (id_student = 2)
INSERT INTO progress(id_option, id_level, id_semester, id_session, id_student) VALUES
(1, 1, 1, 1, 2), -- S1 L1
(1, 1, 2, 2, 2), -- S2 L1
(1, 2, 3, 3, 2), -- S3 L2
(3, 2, 4, 4, 2); -- S4 Web and Design L2

-- =====================================
-- 8️⃣ Insert Grades for all progresses
-- =====================================
-- John Doe S1
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(14.5, 1, 1),
(16.0, 2, 1),
(13.5, 3, 1),
(12.0, 4, 1),
(15.0, 5, 1),
(14.0, 6, 1);

-- John Doe S2
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(15.0, 7, 2),
(14.5, 8, 2),
(13.0, 9, 2),
(16.0, 10, 2),
(14.0, 11, 2),
(13.5, 12, 2);

-- John Doe S3
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(15.0, 13, 3),
(14.0, 14, 3),
(13.5, 15, 3),
(15.5, 16, 3),
(14.0, 17, 3),
(13.0, 18, 3);

-- John Doe S4 Development
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(16.0, 19, 4),
(15.0, 20, 4),
(14.5, 21, 4),
(15.0, 22, 4),
(17.0, 23, 4),
(14.0, 24, 4),
(13.5, 25, 4),
(14.0, 26, 4),
(13.0, 27, 4);

-- Alice Smith S1
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(14.0, 1, 5),
(15.0, 2, 5),
(13.0, 3, 5),
(12.5, 4, 5),
(14.5, 5, 5),
(13.5, 6, 5);

-- Alice Smith S2
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(15.0, 7, 6),
(14.0, 8, 6),
(13.5, 9, 6),
(15.5, 10, 6),
(14.0, 11, 6),
(13.0, 12, 6);

-- Alice Smith S3
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(14.5, 13, 7),
(14.0, 14, 7),
(13.0, 15, 7),
(15.0, 16, 7),
(13.5, 17, 7),
(12.5, 18, 7);

-- Alice Smith S4 Web and Design
INSERT INTO grade(grade, id_subject, id_progress) VALUES
(15.0, 28, 8),
(14.0, 29, 8),
(14.5, 30, 8),
(15.0, 31, 8),
(16.0, 32, 8),
(14.0, 33, 8),
(13.5, 34, 8),
(14.0, 35, 8),
(13.0, 36, 8);