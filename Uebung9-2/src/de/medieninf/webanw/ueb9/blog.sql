CREATE TABLE blog (
	id INT PRIMARY KEY,
    title TEXT,
    content TEXT,
    date TIMESTAMP,
	version INT DEFAULT 1 NOT NULL -- for JPA
);
CREATE SEQUENCE blog_id_seq START 1;

CREATE TABLE comment (
	id INT PRIMARY KEY,
    blog_id INT REFERENCES blog(id),
    content TEXT,
    date TIMESTAMP,
	version INT DEFAULT 1 NOT NULL -- for JPA
);
CREATE SEQUENCE comment_id_seq START 1;

CREATE TABLE image (
    id INT PRIMARY KEY,
    blog_id INT REFERENCES blog(id),
	image BYTEA,
    type TEXT,
    size INT,
	version INT DEFAULT 1 NOT NULL -- for JPA
);
CREATE SEQUENCE image_id_seq START 1;
