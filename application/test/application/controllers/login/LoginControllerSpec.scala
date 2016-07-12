package application.controllers.login

import java.util.UUID

import controllers.login.LoginController
import domain.exceptions.UserNotFoundException
import domain.model.user.{ UserDummy, Company, User, UserId }
import exceptions.PasswordInvalidException
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.runner.JUnitRunner
import play.api.test.{ FakeRequest, PlaySpecification, WithApplication }
import scalikejdbc.DBSession
import services.login.LoginService

import scala.util.{ Failure, Success }

/**
 * Created by ThucNd on 10/13/15.
 */
@RunWith(classOf[JUnitRunner])
class LoginControllerSpec extends PlaySpecification with Mockito {
  "ArticleController" should {
    val userId: UserId = UserId(UUID.randomUUID().toString)
    val name: String = "name"
    val company: Company = Company("company")
    val email: String = "example@septeni-technology.jp"
    val password = "password"

    val mockLoginService = mock[LoginService]
    val mockUser: User = UserDummy(userId, name, company, email, password)

    "index() render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/login")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
    }

    "authenticate()" should {
      "fails because of enmail format is incorrect" in new WithApplication {
        val loginController: LoginController = new LoginController(loginService = mockLoginService)
        val result = call(loginController.authenticate, FakeRequest(POST, "/login").withFormUrlEncodedBody(
          "email" -> "example@example.com",
          "password" -> password
        ))
        status(result) must equalTo(BAD_REQUEST)
      }

      "fails because of password length is incorrect" in new WithApplication {
        val loginController: LoginController = new LoginController(loginService = mockLoginService)
        val result = call(loginController.authenticate, FakeRequest(POST, "/login").withFormUrlEncodedBody(
          "email" -> email,
          "password" -> "123"
        ))
        status(result) must equalTo(BAD_REQUEST)
      }

      "login success" in new WithApplication {
        mockLoginService.login(any[String], any[String])(any[DBSession]) returns Success(mockUser)
        val loginController: LoginController = new LoginController(loginService = mockLoginService)
        val result = call(loginController.authenticate, FakeRequest(POST, "/login").withFormUrlEncodedBody(
          "email" -> email,
          "password" -> password
        ))
        status(result) must equalTo(SEE_OTHER)
      }

      "login fails because password invalid" in new WithApplication {
        mockLoginService.login(any[String], any[String])(any[DBSession]) returns Failure(new PasswordInvalidException)
        val loginController: LoginController = new LoginController(loginService = mockLoginService)
        val result = call(loginController.authenticate, FakeRequest(POST, "/login").withFormUrlEncodedBody(
          "email" -> email,
          "password" -> password
        ))
        status(result) must equalTo(SEE_OTHER)
      }

      "login fails because password or email invalid" in new WithApplication {
        mockLoginService.login(any[String], any[String])(any[DBSession]) returns Failure(new UserNotFoundException)
        val loginController: LoginController = new LoginController(loginService = mockLoginService)
        val result = call(loginController.authenticate, FakeRequest(POST, "/login").withFormUrlEncodedBody(
          "email" -> email,
          "password" -> password
        ))
        status(result) must equalTo(SEE_OTHER)
      }
    }
  }
}