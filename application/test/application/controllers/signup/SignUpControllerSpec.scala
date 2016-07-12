package application.controllers.signup

import java.util.UUID

import controllers.signup.SignUpController
import domain.lifecycle.user.UserRepository
import domain.model.user.{ UserDummy, User, Company, UserId }
import exceptions.EmailExistingException
import org.specs2.mock.Mockito
import play.api.test.{ FakeRequest, WithApplication, PlaySpecification }
import scalikejdbc.DBSession
import services.signup.SignUpService

import scala.util.{ Failure, Try }

/**
 * Created by thuc_nd on 10/27/15.
 */
class SignUpControllerSpec extends PlaySpecification with Mockito {
  "SignUpControllerSpec" should {
    val userId: UserId = UserId(UUID.randomUUID().toString)
    val name: String = "name"
    val company: Company = Company("company")
    val email: String = "example@septeni-technology.jp"
    val password = "password"

    val mockSignUpService = mock[SignUpService]
    val mockUserRepository = mock[UserRepository]
    val mockUser: User = UserDummy(userId, name, company, email, password)

    "index()" should {
      "render signup page" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(mockUser)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.index, FakeRequest(GET, "/signup"))
        status(result) must equalTo(OK)
        contentType(result) must beSome.which(_ == "text/html")
      }

      "don't login yet" in new WithApplication {
        val home = route(FakeRequest(GET, "/signup")).get
        status(home) must equalTo(UNAUTHORIZED)
      }
    }

    "register()" should {
      "don't login yet" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Failure(new Exception)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.register, FakeRequest(POST, "/signup").withFormUrlEncodedBody(
          "email" -> "example",
          "name" -> "name",
          "company" -> "1",
          "password" -> password,
          "repassword" -> password
        ))
        status(result) must equalTo(UNAUTHORIZED)
      }

      "create a new user success" in new WithApplication {
        mockSignUpService.createUser(any[User], any[String], any[String], any[String], any[String])(any[DBSession]) returns Try(mockUser)
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(mockUser)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.register, FakeRequest(POST, "/signup").withFormUrlEncodedBody(
          "emailName" -> "example",
          "name" -> "name",
          "companyType" -> "1",
          "password" -> password,
          "repassword" -> password
        ))
        status(result) must equalTo(SEE_OTHER)
      }

      "user existing" in new WithApplication {
        mockSignUpService.createUser(any[User], any[String], any[String], any[String], any[String])(any[DBSession]) returns Failure(new EmailExistingException)
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(mockUser)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.register, FakeRequest(POST, "/signup").withFormUrlEncodedBody(
          "emailName" -> "example",
          "name" -> "name",
          "companyType" -> "1",
          "password" -> password,
          "repassword" -> password
        ))
        status(result) must equalTo(SEE_OTHER)
      }

      "password not match" in new WithApplication {
        mockSignUpService.createUser(any[User], any[String], any[String], any[String], any[String])(any[DBSession]) returns Failure(new EmailExistingException)
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(mockUser)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.register, FakeRequest(POST, "/signup").withFormUrlEncodedBody(
          "emailName" -> "example",
          "name" -> "name",
          "companyType" -> "1",
          "password" -> password,
          "repassword" -> "123456"
        ))
        status(result) must equalTo(BAD_REQUEST)
      }

      "name incorrect" in new WithApplication {
        mockSignUpService.createUser(any[User], any[String], any[String], any[String], any[String])(any[DBSession]) returns Failure(new EmailExistingException)
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Try(mockUser)
        val signUpController: SignUpController = new SignUpController(userRepository = mockUserRepository, signUpService = mockSignUpService)
        val result = call(signUpController.register, FakeRequest(POST, "/signup").withFormUrlEncodedBody(
          "emailName" -> "example",
          "name" -> "",
          "companyType" -> "1",
          "password" -> password,
          "repassword" -> password
        ))
        status(result) must equalTo(BAD_REQUEST)
      }
    }
  }
}
