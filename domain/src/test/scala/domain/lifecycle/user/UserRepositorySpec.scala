package domain.lifecycle.user

import domain.model.user.UserId
import infrastructure.scalikejdbc.UserPersistent
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.WithApplication
import scalikejdbc.DBSession

import scala.util.{ Failure, Try }

class UserRepositorySpec extends Specification with Mockito {

  private val mockUserPersistent = mock[UserPersistent]

  private def userRepository(userPersistent: UserPersistent) =
    new UserRepositoryOnDbImpl(userPersistent)

  val userIdString: String = "123Abc"
  val userId = UserId(userIdString)
  val name = "User Name"
  val companyName = "Company Name"
  val email = "hoaipt@exmaple.com"
  val password = "123Abc"
  val dummyData: Map[String, Any] = Map(
    "user_id" -> userIdString,
    "name" -> name,
    "company_name" -> companyName,
    "email" -> email,
    "password" -> password
  )

  "UserRepository" should {
    "resolveByEmail()" should {
      "Found in DB" in new WithApplication {
        mockUserPersistent.selectByEmail(any[String])(any[DBSession]) returns Try(dummyData)

        val user = userRepository(mockUserPersistent).resolveByEmail(email)
        user must beSuccessfulTry
        user.get.email must beEqualTo(email)
        user.get.password must beEqualTo(password)
      }

      "Not found in DB" in new WithApplication {
        mockUserPersistent.selectByEmail(any[String])(any[DBSession]) returns Failure(new Exception("fails"))

        val user = userRepository(mockUserPersistent).resolveByEmail(email)
        user must beFailedTry
      }
    }
  }
}