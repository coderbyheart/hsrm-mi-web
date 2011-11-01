DELETE FROM blog;

INSERT INTO blog (id, title, content, date) VALUES
(1, 
 'Erster',
 'Das ist der erste Blog-Eintrag. Er besteht nur aus wenigen Sätzen.',
 '2010-01-01');
INSERT INTO blog (id, title, content, date) VALUES
(2,
 'Zweiter',
 'Das ist ein weiterer Blog-Eintrag. Er besteht nur aus mehr Sätzen. Aber auch nicht so viele.',
 '2010-01-31');
INSERT INTO blog (id, title, content, date) VALUES
(3,
 'Letzter automatischer',
 'Das ist der letzte automatisch hinzugefügte Blog-Eintrag.',
 now());

INSERT INTO comment VALUES (1, 1, 'Ein kurzer Kommentar', '2010-01-02');
INSERT INTO comment VALUES (2, 1, 'Ein weiterer kurzer Kommentar', '2010-01-04');
INSERT INTO comment VALUES (3, 2, 'Jo', '2010-02-04');
INSERT INTO comment VALUES (4, 2, 'JoJo', '2010-02-07');

-- Noch das Resetten der Sequenzgeneratoren
ALTER SEQUENCE blog_id_seq RESTART WITH 4;
ALTER SEQUENCE comment_id_seq RESTART WITH 5;
ALTER SEQUENCE image_id_seq RESTART WITH 1;
