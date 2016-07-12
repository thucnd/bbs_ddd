package domain.model.user

import org.specs2.mutable.Specification

class UserSpec extends Specification {
  val userId: UserId = UserId("user_id")
  val name: String = "User Name"
  val companyName: String = "Company Name"
  val company: Company = Company(companyName)
  val email: String = "hoai_pt@example.com"
  val password: String = "password"
  val title: String = "title"
  val content: String = "content"

  "User" should {
    "apply()" in {
      "check to create a new user entity" in {
        val user = User(userId, name, company, email, password)
        user.email must beEqualTo(email)
        user.password must beEqualTo(password)
        user.id.value must beEqualTo("user_id")
      }
    }

    "createArticle(title: String, content: String)" in {
      "check to create a article entity" in {
        val user = User(userId, name, company, email, password)
        val article = user.createArticle(title, content)
        article.user must beEqualTo(user)
        article.title must beEqualTo(title)
        article.content must beEqualTo(content)
      }
    }

    "createUser(name: String, companyNane: String, email: String, password: String)" in {
      "check to create a user entity" in {
        val user = User(userId, name, company, email, password)
        val nUser = user.createUser(name, companyName, email, password)
        nUser.name must beEqualTo(name)
        nUser.email must beEqualTo(email)
        nUser.company must beEqualTo(company)
      }
    }
  }
}