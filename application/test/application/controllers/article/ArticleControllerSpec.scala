package application.controllers.article

import java.util.UUID

import controllers.article.ArticleController
import domain.lifecycle.user.UserRepository
import domain.model.article.{ Article, ArticleId }
import domain.model.user.{ UserDummy, Company, UserId, User }
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.runner.JUnitRunner
import play.api.test.{ FakeRequest, PlaySpecification, WithApplication }
import scalikejdbc.DBSession
import services.article.ArticleService
import scala.util.{ Try, Failure, Success }

/**
 * Created by hoai_pt on 10/13/15.
 */
@RunWith(classOf[JUnitRunner])
class ArticleControllerSpec extends PlaySpecification with Mockito {
  "ArticleController" should {
    val articleId: ArticleId = ArticleId(UUID.randomUUID().toString)
    val userId: UserId = UserId(UUID.randomUUID().toString)
    val name: String = "name"
    val company: String = "company"
    val email: String = "example@septeni-technology.jp"
    val password: String = "password"
    val title = "this is new article title"
    val content = "this is new article content"
    val mockArticleService = mock[ArticleService]
    val mockUserRepository = mock[UserRepository]
    val user = UserDummy(userId, name, Company(company), email, password)
    val mockArticle: Article = user.createArticle(title, content)

    "showListPage()" should {
      "get list from DB then return status 200" in new WithApplication {
        mockArticleService.getArticleList() returns Success(Seq(mockArticle))
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.showListPage, FakeRequest(GET, "/"))
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
        contentAsString(result) must contain(name)
        contentAsString(result) must contain(title)
      }

      "when Server Error appears, return InternalError" in new WithApplication {
        mockArticleService.getArticleList() returns Failure(new Exception("Failed!"))

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.showListPage, FakeRequest(GET, "/"))
        status(result) must throwA[Exception]
      }
    }

    "showCreatePage()" should {
      "render page for create article" in new WithApplication {
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.showCreatePage, FakeRequest(GET, "/article").withSession("email" -> email, "name" -> name))
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
      }

      "user don't login, return UNAUTHORIZED" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Failure(new Exception)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.showCreatePage, FakeRequest(GET, "/article"))
        status(result) must equalTo(UNAUTHORIZED)
      }
    }

    "view(articleId: String)" should {
      "view article detail" in new WithApplication {
        mockArticleService.getArticleDetail(any[String])(any[DBSession]) returns Success(mockArticle)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.view("123"), FakeRequest(GET, "/article/123"))
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
        contentAsString(result) must contain(name)
        contentAsString(result) must contain(title)
        contentAsString(result) must contain(content)
      }

      "when Server Error return InternalError" in new WithApplication {
        mockArticleService.getArticleDetail(any[String])(any[DBSession]) returns Failure(new Exception("Failed!"))

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.view("123"), FakeRequest(GET, "/article/123"))
        status(result) must throwA[Exception]
      }

    }

    "create" should {
      "receive request data,save to DB then redirect to list article page" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        mockArticleService.createArticle(any[User], any[String], any[String])(any[DBSession]) returns Success(mockArticle)

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> title,
          "content" -> content
        ).withSession("email" -> email, "name" -> name))
        status(result) must equalTo(SEE_OTHER)
        flash(result).data.get("success").toString must contain("Create Article Successful")
      }

      "when Server Error return InternalError" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        mockArticleService.createArticle(any[User], any[String], any[String])(any[DBSession]) returns Failure(new Exception("Failed!"))

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> title,
          "content" -> content
        ).withSession("email" -> email, "name" -> name))
        status(result) must throwA[Exception]
      }

      "receive invalid data,content is empty,return ValidationError(BadRequest)" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> title,
          "content" -> ""
        ).withSession("email" -> email, "name" -> name))
        status(result) must equalTo(BAD_REQUEST)
      }

      "receive invalid data,title is empty,return ValidationError(BadRequest)" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> "",
          "content" -> content
        ).withSession("email" -> email, "name" -> name))
        status(result) must equalTo(BAD_REQUEST)
      }

      "receive invalid data,title have special character,return ValidationError(BadRequest)" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> "this is title with あ",
          "content" -> content
        ).withSession("email" -> email, "name" -> name))
        status(result) must equalTo(BAD_REQUEST)
      }

      "receive invalid data,content have special character,return ValidationError(BadRequest)" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(user)
        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> title,
          "content" -> "this is content with あ"
        ).withSession("email" -> email, "name" -> name))
        status(result) must equalTo(BAD_REQUEST)
      }

      "user don't login, return UNAUTHORIZED" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Failure(new Exception("Failed!"))

        val articleController: ArticleController = new ArticleController(userRepository = mockUserRepository, articleService = mockArticleService)
        val result = call(articleController.create, FakeRequest(POST, "/article").withFormUrlEncodedBody(
          "title" -> title,
          "content" -> "this is content with あ"
        ))
        status(result) must equalTo(UNAUTHORIZED)
      }

    }
  }
}