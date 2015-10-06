-- Problem 1
 
CREATE TABLE MOVIES (
    Title       varchar(255),
    Year        int,
    Director    varchar(255),
    Country     varchar(255),
    Rating      real,
    Genre       varchar(255),
    Gross       real,
    Producer    varchar(255),
    PRIMARY KEY (Title, Year)
);
 
CREATE TABLE ACTORS (
    Title               varchar(255),
    Year                int,
    Character_name      varchar(255),
    Actor               varchar(255),
    PRIMARY KEY (Title, Year, Character_name),
    FOREIGN KEY (Title, Year) REFERENCES MOVIES(Title, Year)
);
 
CREATE TABLE AWARDS (
    Title       varchar(255),
    Year        int,
    Award       varchar(255),
    Result      varchar(255),
    CONSTRAINT chk_result CHECK (Result in ('won', 'nominated')),
    PRIMARY KEY (Title, Year, Award),
    FOREIGN KEY (Title, Year) REFERENCES MOVIES(Title, Year)
);
 
-- Simplify Award query through a VIEW
CREATE VIEW awards_view (title, year, award_name, award_desc, result) AS
    --      title    year          Award name                          Award for                    result
    SELECT A.title, A.year, split_part(A.award, ',', 1), substr(A.award, strpos(A.award, ', ')+2), A.result
    FROM AWARDS A;
 
INSERT INTO MOVIES VALUES ('A Beautiful Mind', 2001, 'Ron Howard', 'USA', 8.1, 'Drama', 170708996, 'Universal Pictures');
INSERT INTO ACTORS VALUES ('A Beautiful Mind', 2001, 'John Nash', 'Russell Crowe');
INSERT INTO ACTORS VALUES ('A Beautiful Mind', 2001, 'Alicia Nash', 'Jennifer Connelly');
INSERT INTO ACTORS VALUES ('A Beautiful Mind', 2001, 'Parcher', 'Ed Harris');
INSERT INTO AWARDS VALUES ('A Beautiful Mind', 2001, 'Oscar, Best Picture', 'won');
INSERT INTO AWARDS VALUES ('A Beautiful Mind', 2001, 'Oscar, Best Director', 'won');
INSERT INTO AWARDS VALUES ('A Beautiful Mind', 2001, 'Oscar, Best Film Editing', 'nominated');
INSERT INTO AWARDS VALUES ('A Beautiful Mind', 2001, 'Oscar, Best Makeup', 'nominated');
 
INSERT INTO MOVIES VALUES ('Fight Club', 1999, 'David Fincher', 'USA', 8.8, 'Drama', 37023395, 'Fox 2000 Pictures');
INSERT INTO ACTORS VALUES ('Fight Club', 1999, 'Marla Singer', 'Helena Bonham Carter');
INSERT INTO ACTORS VALUES ('Fight Club', 1999, 'Tyler Durden', 'Brad Pitt');
INSERT INTO ACTORS VALUES ('Fight Club', 1999, 'The Narratorr', 'Edward Norton');
INSERT INTO AWARDS VALUES ('Fight Club', 1999, 'Oscar, Best Effects, Sound Effects Editing', 'nominated');
INSERT INTO AWARDS VALUES ('Fight Club', 1999, 'Brit, Best Effects, Best Soundtrack', 'nominated');
INSERT INTO AWARDS VALUES ('Fight Club', 1999, 'OFCS Award, Best DVD', 'won');
INSERT INTO AWARDS VALUES ('Fight Club', 1999, 'OFCS Award, Best DVD Special Features', 'won');
 
INSERT INTO MOVIES VALUES ('The Godfather', 1972, 'Francis Ford Coppola', 'USA', 9.2, 'Crime', 134821952, 'Paramount Pictures');
INSERT INTO ACTORS VALUES ('The Godfather', 1972, 'Don Vito Corleone', 'Marlon Brando');
INSERT INTO ACTORS VALUES ('The Godfather', 1972, 'Sonny Corleone', 'James Caan');
INSERT INTO ACTORS VALUES ('The Godfather', 1972, 'Michael Corleone', 'Al Pacino');
INSERT INTO ACTORS VALUES ('The Godfather', 1972, 'Tom Hagen', 'Robert Duvall');
INSERT INTO AWARDS VALUES ('The Godfather', 1972, 'Oscar, Best Picture', 'won');
INSERT INTO AWARDS VALUES ('The Godfather', 1972, 'Oscar, Best Actor in a Supporting Role', 'nominated');
INSERT INTO AWARDS VALUES ('The Godfather', 1972, 'Oscar, Best Director', 'nominated');
INSERT INTO AWARDS VALUES ('The Godfather', 1972, 'Oscar, Best Actor in a Leading Role', 'won');
 
INSERT INTO MOVIES VALUES ('Psycho', 1960, 'Alfred Hitchcock', 'USA', 8.6, 'Horror', 32000000, 'Shamley Productions');
INSERT INTO ACTORS VALUES ('Psycho', 1960, 'Norman Bates', 'Anthony Perkins');
INSERT INTO ACTORS VALUES ('Psycho', 1960, 'Lila Crane', 'Vera Miles');
INSERT INTO ACTORS VALUES ('Psycho', 1960, 'Sam Loomis', 'John Gavin');
INSERT INTO ACTORS VALUES ('Psycho', 1960, 'Marion Crane', 'Janet Leigh');
INSERT INTO AWARDS VALUES ('Psycho', 1960, 'Oscar, Best Actress in a Supporting Role', 'won');
INSERT INTO AWARDS VALUES ('Psycho', 1960, 'Oscar, Best Director', 'won');
INSERT INTO AWARDS VALUES ('Psycho', 1960, 'Oscar, Best Cinematography, Black-and-White', 'won');
INSERT INTO AWARDS VALUES ('Psycho', 1960, 'Golden Globe, Best Supporting Actress', 'won');
 
INSERT INTO MOVIES VALUES ('A Clockwork Orange', 1971, 'Stanley Kubrick', 'USA', 8.4, 'Sci-Fi', 2707000, 'Warner Bros.');
INSERT INTO ACTORS VALUES ('A Clockwork Orange', 1971, 'Alex', 'Malcolm McDowell');
INSERT INTO ACTORS VALUES ('A Clockwork Orange', 1971, 'Mr Alexander', 'Patrick Magee');
INSERT INTO ACTORS VALUES ('A Clockwork Orange', 1971, 'Chief Guard', 'Michael Bates');
INSERT INTO ACTORS VALUES ('A Clockwork Orange', 1971, 'Dim', 'Warren Clarke');
INSERT INTO AWARDS VALUES ('A Clockwork Orange', 1971, 'Oscar, Best Picture', 'nominated');
INSERT INTO AWARDS VALUES ('A Clockwork Orange', 1971, 'Oscar, Best Director', 'nominated');
INSERT INTO AWARDS VALUES ('A Clockwork Orange', 1971, 'Oscar, Best Film Editing', 'nominated');
INSERT INTO AWARDS VALUES ('A Clockwork Orange', 1971, 'Golden Globe, Best Motion Picture - Drama', 'nominated');
 
 
-- Problem 2
-- (1)
SELECT A.actor
FROM ACTORS A
WHERE A.actor in (SELECT A1.actor
                  FROM MOVIES M, ACTORS A1
                  WHERE M.title = A1.title AND M.year = A1.year AND A1.character_name <> A.character_name)
GROUP BY A.actor;
 
-- (2)
SELECT M.director, AVG(M.gross)
FROM MOVIES M
WHERE M.director IN (SELECT M1.director
                     FROM MOVIES M1, awards_view A1
                     WHERE A1.award_name = 'Oscar' AND
                           A1.title = M1.title AND A1.year = M1.year AND A1.result = 'won')
GROUP BY M.director;
-- (3)
SELECT M.producer, SUM(M.gross)
FROM MOVIES M
GROUP BY M.producer;
-- (4)
SELECT DISTINCT M.producer
FROM MOVIES M
WHERE M.gross > 50000000 AND (SELECT COUNT(*)
                              FROM MOVIES M2
                              WHERE M.producer = M2.producer) > 2;
 
-- (5) -- REVISE
SELECT *
FROM MOVIES M
WHERE M.rating > 8 AND M.gross > 50000000
ORDER BY M.gross DESC;
 
-- (6) Find movies made in the 90s that were nominated for at least two diﬀerent awards (e.g, Oscar and Golden Globe) in the best actor category.
SELECT DISTINCT M.title, M.year, M.director, M.country, M.rating, M.genre, M.gross, M.producer
FROM MOVIES M, awards_view A1, awards_view A2
WHERE M.title = A1.title AND
      M.year = A1.year AND
      M.title = A2.title AND
      M.year = A2.year AND
      A1.result = 'nominated' AND
      A2.result = 'nominated' AND
      A1.award_desc = 'Best Actor' AND
      A2.award_desc = 'Best Actor' AND
      A1.award_name <> A2.award_name;
 
-- (7)
SELECT *
FROM MOVIES M, AWARDS A
WHERE M.year BETWEEN 1990 AND 2000 AND
      M.title = A.title AND
      (SELECT COUNT(A.result)
       FROM AWARDS A
       WHERE A.title = M.title AND
             A.year = M.year AND
             A.result = 'nominated') = 0;
 
-- (8) 
SELECT M.title
FROM MOVIES M, awards_view AV
WHERE M.title = AV.title AND
      M.year = AV.year AND
      AV.award_name = 'Oscar' AND
      AV.award_desc IN ('Best Picture', 'Best Director') AND
      (M.year > 1990 OR M.year < 1960);
 
-- (9)
SELECT M1.director
FROM MOVIES M1, MOVIES M2
WHERE M1.director = M2.director AND
      M1.genre = 'Comedy' AND
      M2.genre = 'Drama' AND
      M1.rating > M2.rating;
  
-- (10) Find actors who only act in high grossing (more than $50 million) movies.
SELECT DISTINCT A.actor
FROM ACTORS A, MOVIES M
WHERE A.title = M.title AND
      A.year = M.year AND
      M.gross > 50000000;
 
-- (11) For each award category, ﬁnd the average rating of movies that won that award.
SELECT A.award_desc, avg(M.rating)
FROM awards_view A, MOVIES M
WHERE A.title = M.title AND
      A.year = M.year
GROUP BY A.award_desc;
 
-- (12) Find the award category whose winners have the highest average rating.
CREATE VIEW avg_award_rating (award_desc, avg) AS
    SELECT A.award_desc, avg(M.rating)
    FROM awards_view A, MOVIES M
    WHERE A.title = M.title AND
          A.year = M.year
    GROUP BY A.award_desc;
 
-- If there are multiple entries that are max, returns all
SELECT K.award_desc
FROM avg_award_rating K
WHERE k.avg = (SELECT max(A.avg)
               FROM avg_award_rating A);
 
-- (13) Find all pairs of movies (m1, m2) nominated for the same award, such that m1 has higher rating than m2, but m2 won the award.
SELECT DISTINCT M1.title AS m1, M2.title AS m2
FROM MOVIES M1, MOVIES M2, AWARDS A1, AWARDS A2
WHERE M1.title = A1.title AND
      M2.title = A2.title AND
      M1.year = A1.year AND
      M2.year = A2.year AND
      A1.award = A2.award AND
      M1.rating > M2.rating AND
      A1.result = 'nominated' AND
      A2.result = 'won' AND
      M1.title <> M2.title;
 
-- (14) Find character names that appear in two movies produced in two diﬀerent countries.
SELECT DISTINCT A1.character_name
FROM ACTORS A1, ACTORS A2, MOVIES M1, MOVIES M2
WHERE M1.country <> M2.country AND
      M1.title = A1.title AND
      M1.year = A1.year AND
      M2.year = A2.year AND
      M2.title = A2.title AND
      M1.title <> M2.title AND
      M1.year <> M2.year;
 
-- (15) For every decade starting with 1950-59, calculate the percentage of all the awards won by US movies.--
 
CREATE VIEW decades (year_from, year_to) AS
    SELECT * 
    FROM generate_series(1950, 2010, 10) AS A, generate_series(1960, 2020, 10) AS B 
    WHERE A = B - 10;
 
 
 
SELECT M.year_from, M.year_to, N.count * 100 / M.count
FROM (
    SELECT * 
    FROM decades D, (
        SELECT M.year, COUNT(M) 
        FROM MOVIES M, AWARDS A
        WHERE M.title = A.title AND
              M.year = A.year AND
              M.country = 'USA'
        GROUP BY M.year) B
    WHERE D.year_from < B.year AND
          D.year_to > B.year) M, (
     
    SELECT *
    FROM decades D, (
        SELECT M.year, COUNT(M)
        FROM MOVIES M, AWARDS A
        WHERE M.title = A.title AND
              M.year = A.year AND
              M.country = 'USA' AND
              A.result = 'won'
        GROUP BY M.year) B
    WHERE D.year_from < B.year AND D.year_to > B.year) N
WHERE N.year = M.year;
