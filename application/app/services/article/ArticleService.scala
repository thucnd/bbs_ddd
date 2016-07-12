package services.article

import domain.lifecycle.article.ArticleRepository
import domain.lifecycle.user.UserRepository
import domain.model.article.{ ArticleId, Article }
import domain.model.user.User
import exceptions.EmailNotMatchException
import scalikejdbc.{ AutoSession, DBSession }
import scala.util.{ Failure, Try }

/**
 * Created by hoai_pt on 10/13/15.
 */
class ArticleService(articleRepository: ArticleRepository, userRepository: UserRepository) {
  def getArticleList()(implicit session: DBSession = AutoSession): Try[Seq[Article]] = {
    articleRepository.resolveAll()
  }

  def getArticleDetail(articleId: String)(implicit session: DBSession = AutoSession): Try[Article] = {
    articleRepository.resolveById(ArticleId(articleId))
  }

  def createArticle(user: User, title: String, content: String)(implicit session: DBSession = AutoSession): Try[Article] = {
    articleRepository.store(user.createArticle(title, content))
  }
}

object ArticleService {
  def apply(): ArticleService = new ArticleService(ArticleRepository(), UserRepository())
}

