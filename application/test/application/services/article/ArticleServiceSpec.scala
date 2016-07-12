package application.services.article

import java.util.UUID
import domain.lifecycle.article.ArticleRepository
import domain.lifecycle.user.UserRepository
import domain.model.article.{ Article, ArticleId }
import domain.model.user.{ UserDummy, Company, UserId, User }
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import scalikejdbc.DBSession
import services.article.ArticleService
import scala.util.{ Failure, Success }

/**
 * Created by hoai_pt on 10/13/15.
 */
class ArticleServiceSpec extends PlaySpecification with Mockito {
  "ArticleService" should {
    val articleId: ArticleId = ArticleId(UUID.randomUUID().toString)
    val email: String = "email@bbs.jp"
    val title = "this is new article title"
    val content = "this is new article content"
    val password = "password"
    val name: String = "name"
    val company: Company = Company("company")
    val userId = UserId("user_id")

    val mockArticleRepository = mock[ArticleRepository]
    val mockUserRepository = mock[UserRepository]
    val mockUser: User = UserDummy(userId, name, company, email, password)
    val mockArticle: Article = mockUser.createArticle(title, content)

    val articleService = new ArticleService(mockArticleRepository, mockUserRepository)

    "getArticleList()" should {
      "return all article in DB" in {
        val mocSeqResult = Success(Seq(mockArticle))
        mockArticleRepository.resolveAll()(any[DBSession]) returns mocSeqResult
        val result = articleService.getArticleList
        result must equalTo(mocSeqResult)
      }

      "when Server Error appears, return InternalError" in {
        mockArticleRepository.resolveAll()(any[DBSession]) returns Failure(new Exception("Failed!"))
        val result = articleService.getArticleList
        result must beFailedTry
      }
    }

    "getArticleDetail(articleId: String)" should {
      "get article detail in DB" in {
        mockArticleRepository.resolveById(any[ArticleId])(any[DBSession]) returns Success(mockArticle)

        val result = articleService.getArticleDetail("123")
        result must beSuccessfulTry
        result.get.user.email must beEqualTo(email)
        result.get.content must beEqualTo(content)
        result.get.title must beEqualTo(title)
      }

      "when Server Error appears, return InternalError" in {
        mockArticleRepository.resolveById(any[ArticleId])(any[DBSession]) returns Failure(new Exception("Failed!"))
        val result = articleService.getArticleDetail("123")
        result must beFailedTry
      }

    }

    "createArticle(email: String, title: String, content: String)" should {
      "store new article into DB" in {
        mockArticleRepository.store(any[Article])(any[DBSession]) returns Success(mockArticle)
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Success(mockUser)

        val result = articleService.createArticle(mockUser, title, content)
        result must beSuccessfulTry
        result.get.user.email must beEqualTo(email)
        result.get.content must beEqualTo(content)
        result.get.title must beEqualTo(title)
      }

      "when Server Error appears, return InternalError" in {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Success(mockUser)
        mockArticleRepository.store(any[Article])(any[DBSession]) returns Failure(new Exception("Failed!"))

        val result = articleService.createArticle(mockUser, title, content)
        result must beFailedTry
      }

    }

  }
}
