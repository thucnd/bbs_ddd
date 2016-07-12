package domain.model.article

import domain.model.user.{ Company, User, UserId }
import org.joda.time.DateTime
import org.specs2.mutable.Specification

class ArticleSpec extends Specification {

  val user: User = User(UserId("userId"), "User Name", Company("Company Name"), "email@example.com", "password")
  "Article" should {
    "apply()" in {
      "check to create a new article entity" in {
        val article = Article(ArticleId("article_id"), user, "title", "content", new DateTime)
        article.title must beEqualTo("title")
        article.content must beEqualTo("content")
        article.id.value must beEqualTo("article_id")
      }
    }
  }
}