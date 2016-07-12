package domain.lifecycle.article

import domain.model.article.{ Article, ArticleId }
import domain.exceptions.{ DbAccessException, ArticleNotFoundException }
import domain.model.user.{ Company, UserId, User }
import infrastructure.scalikejdbc.ArticlePersistent
import org.joda.time.DateTime
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
 * Created by ThucNd on 10/13/2015.
 */
private[article] class ArticleRepositoryOnDbImpl(articlePersistent: ArticlePersistent) extends ArticleRepository {

  override def resolveAll()(implicit session: DBSession = AutoSession): Try[Seq[Article]] = Try {
    articlePersistent.selectAll().get.map(convertToEntity)
  }

  override def resolveById(id: ArticleId)(implicit session: DBSession = AutoSession): Try[Article] = Try {
    convertToEntity(articlePersistent.selectById(id.value)
      .getOrElse(throw new ArticleNotFoundException))
  }

  override def store(entity: Article)(implicit session: DBSession = AutoSession): Try[Article] = Try {
    articlePersistent.insert(convertToMap(entity)).map(_ => entity)
      .getOrElse(throw new DbAccessException)
  }

  private def convertToEntity(m: Map[String, Any]): Article =
    Article(
      ArticleId(m("article_id").toString),
      User(UserId(m("user_id").toString), m("name").toString, Company(m("company_name").toString), m("email").toString, m("password").toString),
      m("title").toString,
      m("content").toString,
      m("created").asInstanceOf[DateTime]
    )

  private def convertToMap(entity: Article): Map[String, Any] =
    Map(
      "article_id" -> entity.id.value,
      "user_id" -> entity.user.id.value,
      "title" -> entity.title,
      "content" -> entity.content,
      "created" -> entity.createdDate
    )
}

