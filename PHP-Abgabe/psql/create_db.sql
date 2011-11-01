SET client_encoding = 'UTF8';
SET client_min_messages = warning;



CREATE TABLE modul (
    id integer NOT NULL,
    bezeichnung character varying(80)
);



ALTER TABLE ONLY modul
    ADD CONSTRAINT modul_pkey PRIMARY KEY (id);




CREATE TABLE orte (
    plz integer,
    ort character varying(80),
    zusatz character varying(80),
    vorwahl character varying(80),
    bundesland character varying(80)
);

CREATE TABLE studiengang (
    id integer NOT NULL,
    bezeichnung character varying(80)
);

ALTER TABLE ONLY studiengang
    ADD CONSTRAINT studiengang_pkey PRIMARY KEY (id);

CREATE TABLE liste (
    id integer NOT NULL,
    bez character varying(80),
    studiengang_id integer
);

ALTER TABLE ONLY liste
    ADD CONSTRAINT liste_pkey PRIMARY KEY (id);

ALTER TABLE ONLY liste
    ADD CONSTRAINT liste_studiengang_id_fkey FOREIGN KEY (studiengang_id) REFERENCES studiengang(id);

CREATE TABLE modul_studiengang (
    modul_id integer NOT NULL,
    studiengang_id integer NOT NULL,
    fachsem integer
);



ALTER TABLE ONLY modul_studiengang
    ADD CONSTRAINT modul_studiengang_pkey PRIMARY KEY (modul_id, studiengang_id);


ALTER TABLE ONLY modul_studiengang
    ADD CONSTRAINT modul_studiengang_modul_id_fkey FOREIGN KEY (modul_id) REFERENCES modul(id);

ALTER TABLE ONLY modul_studiengang
    ADD CONSTRAINT modul_studiengang_studiengang_id_fkey FOREIGN KEY (studiengang_id) REFERENCES studiengang(id);


CREATE TABLE studierende (
    matnr integer NOT NULL,
    vorname character varying(80),
    nachname character varying(80),
    fachsem integer,
    studiengang_id integer,
    adresse1 character varying(80),
    adresse2 character varying(80),
    plz integer,
    stadt character varying(80),
    land character varying(80),
    email character varying(80),
    tel character varying(80)
);

ALTER TABLE ONLY studierende
    ADD CONSTRAINT studierende_pkey PRIMARY KEY (matnr);

ALTER TABLE ONLY studierende
    ADD CONSTRAINT studierende_studiengang_id_fkey FOREIGN KEY (studiengang_id) REFERENCES studiengang(id);






CREATE TABLE veranstaltungstyp (
    id integer NOT NULL,
    bezeichnung character varying(80),
    max_teiln_def integer,
    max_dupl_def integer,
    dauer_def integer
);

ALTER TABLE ONLY veranstaltungstyp
    ADD CONSTRAINT veranstaltungstyp_pkey PRIMARY KEY (id);

CREATE TABLE veranstaltung (
    id integer NOT NULL,
    modul_id integer,
    typ_id integer,
    dauer integer,
    max_teiln integer
);

ALTER TABLE ONLY veranstaltung
    ADD CONSTRAINT veranstaltung_modul_id_fkey FOREIGN KEY (modul_id) REFERENCES modul(id);

ALTER TABLE ONLY veranstaltung
    ADD CONSTRAINT veranstaltung_typ_id_fkey FOREIGN KEY (typ_id) REFERENCES veranstaltungstyp(id);


ALTER TABLE ONLY veranstaltung
    ADD CONSTRAINT veranstaltung_pkey PRIMARY KEY (id);

CREATE TABLE veranstaltung_gruppe (
    id_veranstaltung integer NOT NULL,
    gid character(1) NOT NULL,
    wochentag integer,
    uhrzeit time without time zone
);

ALTER TABLE ONLY veranstaltung_gruppe
    ADD CONSTRAINT veranstaltung_gruppe_pkey PRIMARY KEY (id_veranstaltung, gid);

ALTER TABLE ONLY veranstaltung_gruppe
    ADD CONSTRAINT veranstaltung_gruppe_id_veranstaltung_fkey FOREIGN KEY (id_veranstaltung) REFERENCES veranstaltung(id);


CREATE TABLE studierende_veranstaltung_gruppe (
    matnr integer NOT NULL,
    v_id integer NOT NULL,
    v_gid character(1) NOT NULL
);


ALTER TABLE ONLY studierende_veranstaltung_gruppe
    ADD CONSTRAINT studierende_veranstaltung_gruppe_matnr_fkey FOREIGN KEY (matnr) REFERENCES studierende(matnr);

ALTER TABLE ONLY studierende_veranstaltung_gruppe
    ADD CONSTRAINT studierende_veranstaltung_gruppe_v_id_fkey FOREIGN KEY (v_id, v_gid) REFERENCES veranstaltung_gruppe(id_veranstaltung, gid);

ALTER TABLE ONLY studierende_veranstaltung_gruppe
    ADD CONSTRAINT studierende_veranstaltung_gruppe_pkey PRIMARY KEY (matnr, v_id, v_gid);
