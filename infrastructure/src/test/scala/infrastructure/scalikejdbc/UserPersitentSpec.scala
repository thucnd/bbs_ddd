package infrastructure.scalikejdbc

import org.joda.time.DateTime
import org.specs2.mutable.Specification
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class UserPersitentSpec extends Specification with settings.DBSettings {
  val userPersistent = new UserPersistent
  val userId: String = "uuidvb"
  val name: String = "User Name"
  val companyName: String = "Company Name"
  val email: String = "bbs_ddd@example.com"
  val password: String = "Ha noi mua thu"

  trait AutoRollbackWithFixture extends AutoRollback

  "UserPersitent" should {
    "selectByEmail(email:String)" should {
      "Found User By Email In DB" in new AutoRollbackWithFixture {
        sql"INSERT INTO users(user_id, name, company_name, email, password) VALUES(${userId}, ${name}, ${companyName}, ${email}, ${password})".update.apply()
        val user = userPersistent.selectByEmail(email)
        user.get("user_id") must beEqualTo(userId)
        user.get("name") must beEqualTo(name)
        user.get("company_name") must beEqualTo(companyName)
        user.get("email") must beEqualTo(email)
        user.get("password") must beEqualTo(password)
      }

      "Not Found User List In DB" in new AutoRollbackWithFixture {
        val user = userPersistent.selectByEmail(email)
        user must beFailedTry
      }
    }

    "insert(user: Map[String, Any])" should {
      "insert to DB success" in new AutoRollbackWithFixture {
        val user: Map[String, Any] = Map(
          "user_id" -> "article_id",
          "name" -> "title",
          "company_name" -> "companyName",
          "email" -> "content",
          "password" -> "password"
        )

        val result = userPersistent.insert(user)
        result must beSuccessfulTry
        result.get must beEqualTo(1)
      }

      "insert to DB fails" in new AutoRollbackWithFixture {
        val user: Map[String, Any] = Map(
          "user_id" -> "article_id",
          "name" -> "title",
          "company_name" -> "companyName",
          "email" -> "content",
          "password" -> null
        )

        val result = userPersistent.insert(user)
        result must beFailedTry
      }
    }
  }
}