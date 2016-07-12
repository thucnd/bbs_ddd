package domain.lifecycle.article

import domain.model.article.{ Article, ArticleId }
import domain.model.user.{ UserId, Company, User }
import infrastructure.scalikejdbc.ArticlePersistent
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.WithApplication
import scalikejdbc.DBSession

import scala.util.{ Failure, Try }

class ArticleRepositorySpec extends Specification with Mockito {

  private val mockArticlePersistent = mock[ArticlePersistent]

  private def articleRepository(articlePersistent: ArticlePersistent) =
    new ArticleRepositoryOnDbImpl(articlePersistent)

  val articleIdString: String = "123Abc"
  val articleId = ArticleId(articleIdString)
  val title = "this is title of article"
  val content = "this is content of article"
  val createdDate = DateTime.now()

  val userIdString: String = "userId"
  val name: String = "User Name"
  val companyString: String = "Company Name"
  val email: String = "bbs_ddd@example.com"
  val password: String = "Ha noi mua thu"

  val dummyData: Map[String, Any] = Map(
    "article_id" -> articleIdString,
    "user_id" -> userIdString,
    "name" -> name,
    "company_name" -> companyString,
    "email" -> email,
    "password" -> password,
    "title" -> title,
    "content" -> content,
    "created" -> createdDate
  )

  val userDummy: User = User(UserId(userIdString), name, Company(companyString), email, password)
  val articleDummy = Article(articleId, userDummy, title, content, createdDate)

  "ArticleRepository" should {
    "resolveAll()" should {
      "Found in DB" in new WithApplication {
        mockArticlePersistent.selectAll()(any[DBSession]) returns Try(Seq(dummyData))

        val articles = articleRepository(mockArticlePersistent).resolveAll()
        articles must beSuccessfulTry
        articles.get.head.user must beEqualTo(userDummy)
        articles.get.head.title must beEqualTo(title)
        articles.get.head.content must beEqualTo(content)
      }

      "Not found in DB" in new WithApplication {
        mockArticlePersistent.selectAll()(any[DBSession]) returns Try(Seq())

        val articles = articleRepository(mockArticlePersistent).resolveAll()
        articles must beSuccessfulTry
        articles.get must beEmpty
      }
    }

    "resolveById()" should {
      "Found in DB" in new WithApplication {
        mockArticlePersistent.selectById(any[String])(any[DBSession]) returns Try(dummyData)

        val article = articleRepository(mockArticlePersistent).resolveById(articleId)
        article must beSuccessfulTry
        article.get.user must beEqualTo(userDummy)
        article.get.title must beEqualTo(title)
        article.get.content must beEqualTo(content)
      }

      "Not found in DB" in new WithApplication {
        mockArticlePersistent.selectById(any[String])(any[DBSession]) returns Failure(new Exception("fails"))

        val article = articleRepository(mockArticlePersistent).resolveById(articleId)
        article must beFailedTry
      }
    }

    "store(entity: Article): Try[Article]" should {
      "store success" in new WithApplication {
        mockArticlePersistent.insert(any[Map[String, Any]])(any[DBSession]) returns Try(1)
        val result = articleRepository(mockArticlePersistent).store(articleDummy)
        result must beSuccessfulTry
        result.get.user must beEqualTo(userDummy)
        result.get.title must beEqualTo(title)
        result.get.content must beEqualTo(content)
        result.get.createdDate must beEqualTo(createdDate)
        result.get.id.value must beEqualTo(articleIdString)
      }

      "store fail because of Exception" in new WithApplication {
        mockArticlePersistent.insert(any[Map[String, Any]])(any[DBSession]) returns Failure(new Exception("Failed!"))
        val result = articleRepository(mockArticlePersistent).store(articleDummy)
        result must beFailedTry
      }
    }
  }
}