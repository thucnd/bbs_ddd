package application.services.login

import domain.lifecycle.user.UserRepository
import domain.model.user.{ UserDummy, Company, User, UserId }
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification
import scalikejdbc.DBSession
import services.login.LoginService

import scala.util.{ Failure, Success }

/**
 * Created by ThucNd on 10/21/15.
 */
class LoginServiceSpec extends PlaySpecification with Mockito {
  "LoginService" should {
    val email: String = "email@bbs.jp"
    val password = "7c222fb2927d828af22f592134e8932480637c0d"
    val userId = UserId("user_id")
    val name: String = "name"
    val company: Company = Company("company")

    val mockUserRepository = mock[UserRepository]
    val mockUser: User = UserDummy(userId, name, company, email, password)
    val loginService = new LoginService(mockUserRepository)

    "login()" should {
      "login success" in {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Success(mockUser)
        val result = loginService.login(email, "12345678")
        result must beSuccessfulTry
        result.get.email must beEqualTo(email)
        result.get.password must beEqualTo(password)
      }

      "login fails because password invalid" in {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Success(mockUser)
        val result = loginService.login(email, "123456789")
        result must beFailedTry
      }

      "login fails because email not found" in {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Failure(new Exception("fails"))
        val result = loginService.login(email, "12345678")
        result must beFailedTry
      }
    }
  }
}
