# --- !Ups
ALTER TABLE articles MODIFY created datetime NOT NULL;
ALTER TABLE users ADD name VARCHAR(50) NOT NULL;
ALTER TABLE users ADD company_name VARCHAR(50) NOT NULL;
ALTER TABLE articles ADD user_id VARCHAR(50) NOT NULL;
ALTER TABLE articles DROP email;


