# --- !Ups

CREATE TABLE users (
  user_id  VARCHAR(50) NOT NULL,
  email    VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY  KEY(user_id),
  INDEX (email)
);

# --- !Downs
DROP TABLE users;