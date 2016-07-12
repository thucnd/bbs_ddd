package infrastructure.scalikejdbc

import scalikejdbc._

import scala.util.Try

/**
 * Created by ThucNd on 10/13/2015.
 */
class ArticlePersistent {

  val convertToMap = (rs: WrappedResultSet) => Map(
    "article_id" -> rs.string("article_id"),
    "name" -> rs.string("users.name"),
    "company_name" -> rs.string("users.company_name"),
    "email" -> rs.string("users.email"),
    "user_id" -> rs.string("users.user_id"),
    "password" -> rs.string("users.password"),
    "title" -> rs.string("title"),
    "content" -> rs.string("content"),
    "created" -> rs.jodaDateTime("created")
  )

  def selectAll()(implicit session: DBSession = AutoSession): Try[Seq[Map[String, Any]]] = Try {
    SQL(
      """
        SELECT    articles.article_id,
                  articles.title,
                  articles.content,
                  articles.created,
                  users.user_id,
                  users.name,
                  users.company_name,
                  users.email,
                  users.password
        FROM      articles
        INNER JOIN users
        ON articles.user_id = users.user_id
        ORDER BY  articles.created DESC
      """
    ).map(convertToMap).toList().apply()
  }

  def selectById(articleId: String)(implicit session: DBSession = AutoSession): Try[Map[String, Any]] = Try {
    SQL(
      """
        SELECT    articles.article_id,
                  articles.title,
                  articles.content,
                  articles.created,
                  users.user_id,
                  users.name,
                  users.company_name,
                  users.email,
                  users.password
        FROM      articles
        INNER JOIN users
        ON articles.user_id = users.user_id
        WHERE     articles.article_id = {article_id}
      """
    ).bindByName('article_id -> articleId).map(convertToMap).single().apply().get
  }

  def insert(article: Map[String, Any])(implicit session: DBSession = AutoSession): Try[Int] = Try {
    SQL(
      """
        INSERT INTO articles  (article_id,    user_id,  title,    content,  created)
        VALUES                ({article_id}, {user_id}, {title}, {content}, {created})
      """
    ).bindByName(
        'article_id -> article("article_id"),
        'user_id -> article("user_id"),
        'title -> article("title"),
        'content -> article("content"),
        'created -> article("created")
      )
      .update.apply()
  }
}

