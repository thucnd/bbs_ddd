# --- !Ups
CREATE TABLE articles (
  article_id VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  title VARCHAR(50) NOT NULL,
  content TEXT NOT NULL,
  created datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (article_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE articles;
