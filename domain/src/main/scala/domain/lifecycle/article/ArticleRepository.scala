package domain.lifecycle.article

import domain.model.article.{ ArticleId, Article }
import infrastructure.scalikejdbc.ArticlePersistent
import scalikejdbc.{ AutoSession, DBSession }
import scala.util.Try

trait ArticleRepository {
  def resolveAll()(implicit session: DBSession = AutoSession): Try[Seq[Article]]
  def resolveById(articleId: ArticleId)(implicit session: DBSession = AutoSession): Try[Article]
  def store(entity: Article)(implicit session: DBSession = AutoSession): Try[Article]
}

object ArticleRepository {
  def apply(): ArticleRepository = new ArticleRepositoryOnDbImpl(new ArticlePersistent)
}
