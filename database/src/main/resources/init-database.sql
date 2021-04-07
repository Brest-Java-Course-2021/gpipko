INSERT INTO PROJECT (PROJECT_ID, PROJECT_NAME, CREATION_DATE) VALUES(1, 'MilitaryTech', '2021-01-20');
INSERT INTO PROJECT (PROJECT_ID, PROJECT_NAME, CREATION_DATE) VALUES(2, 'CivilTech', '2021-02-15');
INSERT INTO PROJECT (PROJECT_ID, PROJECT_NAME, CREATION_DATE) VALUES(3, 'EcoTech', '2021-03-24');

INSERT INTO AUTHOR (AUTHOR_ID, FIRSTNAME, LASTNAME, EMAIL, GRANT_SUM, PROJECT_ID)
    VALUES (1, 'Walter', 'Gilman', 'wgilman@mail.com', 50, 1);
INSERT INTO AUTHOR (AUTHOR_ID, FIRSTNAME, LASTNAME, EMAIL, GRANT_SUM, PROJECT_ID)
    VALUES (2, 'Henry', 'Wotton', 'hwotton@mail.com', 100, 1);
INSERT INTO AUTHOR (AUTHOR_ID, FIRSTNAME, LASTNAME, EMAIL, GRANT_SUM, PROJECT_ID)
    VALUES (3, 'Runako', 'Adisa', 'radisa@mail.com', 30, 2);
INSERT INTO AUTHOR (AUTHOR_ID, FIRSTNAME, LASTNAME, EMAIL, GRANT_SUM, PROJECT_ID)
    VALUES (4, 'Ronnie', 'Venereal', 'rvenreal@mail.com', 45, 2);