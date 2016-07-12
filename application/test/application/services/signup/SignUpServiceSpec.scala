package application.services.signup

import domain.exceptions.UserNotFoundException
import domain.lifecycle.user.UserRepository
import domain.model.user.{ UserDummy, User, Company, UserId }
import org.specs2.mock.Mockito
import play.api.test.{ WithApplication, PlaySpecification }
import scalikejdbc.DBSession
import services.signup.SignUpService

import scala.util.{ Failure, Success }

/**
 * Created by thuc_nd on 10/27/15.
 */
class SignUpServiceSpec extends PlaySpecification with Mockito {
  "SignUpService" should {
    val email: String = "email@bbs.jp"
    val password = "7c222fb2927d828af22f592134e8932480637c0d"
    val userId = UserId("user_id")
    val name: String = "name"
    val companyName: String = "company"
    val company: Company = Company(companyName)

    val mockUserRepository = mock[UserRepository]
    val mockUser: User = UserDummy(userId, name, company, email, password)
    val signUpService = new SignUpService(mockUserRepository)

    "create(name: String, email: String, company: String, password: String)" should {
      "create a new User success" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Failure(new UserNotFoundException)
        mockUserRepository.store(any[User])(any[DBSession]) returns Success(mockUser)
        val result = signUpService.createUser(mockUser, name, email, companyName, password)
        result must beSuccessfulTry
        result.get.email must beEqualTo(email)
        result.get.name must beEqualTo(name)
        result.get.company must beEqualTo(company)
        result.get.password must beEqualTo(password)
      }

      "user existing" in new WithApplication {
        mockUserRepository.resolveByEmail(any[String])(any[DBSession]) returns Success(mockUser)
        val result = signUpService.createUser(mockUser, name, email, companyName, password)
        result must beFailedTry
      }
    }
  }
}
