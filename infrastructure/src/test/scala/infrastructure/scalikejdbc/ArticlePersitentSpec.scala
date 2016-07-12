package infrastructure.scalikejdbc

import scalikejdbc._
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import scalikejdbc.specs2.mutable.AutoRollback

class ArticlePersitentSpec extends Specification with settings.DBSettings {
  val articlePersitent = new ArticlePersistent
  val articleId: String = "articleId"
  val title: String = "Ha noi mua thu"
  val content: String = "Ha Noi dep vai"

  val userId: String = "userId"
  val name: String = "User Name"
  val companyName: String = "Company Name"
  val email: String = "bbs_ddd@example.com"
  val password: String = "Ha noi mua thu"

  trait AutoRollbackWithFixture extends AutoRollback

  "ArticlePersistent" should {
    "selectAll()" should {
      "Found Article List In DB" in new AutoRollbackWithFixture {
        sql"INSERT INTO users(user_id, name, company_name, email, password) VALUES(${userId}, ${name}, ${companyName}, ${email}, ${password})".update.apply()
        sql"INSERT INTO articles(article_id, user_id, title, content, created) VALUES(${articleId}, ${userId}, ${title}, ${content}, ${DateTime.now()})".update.apply()
        val article = articlePersitent.selectAll()
        article.get.head("name") must beEqualTo(name)
        article.get.head("company_name") must beEqualTo(companyName)
        article.get.head("email") must beEqualTo(email)
        article.get.head("title") must beEqualTo(title)
        article.get.head("content") must beEqualTo(content)
      }

      "Not Found Article List In DB" in new AutoRollbackWithFixture {
        val articles = articlePersitent.selectAll()
        articles.get must beEmpty
      }
    }

    "selectById()" should {
      "Found Article By Id In DB" in new AutoRollbackWithFixture {
        sql"INSERT INTO users(user_id, name, company_name, email, password) VALUES(${userId}, ${name}, ${companyName}, ${email}, ${password})".update.apply()
        sql"INSERT INTO articles(article_id, user_id, title, content, created) VALUES(${articleId}, ${userId}, ${title}, ${content}, ${DateTime.now()})".update.apply()
        val article = articlePersitent.selectById(articleId)
        article.get("name") must beEqualTo(name)
        article.get("company_name") must beEqualTo(companyName)
        article.get("email") must beEqualTo(email)
        article.get("title") must beEqualTo(title)
        article.get("content") must beEqualTo(content)
      }

      "Not Found Article List In DB" in new AutoRollbackWithFixture {
        val article = articlePersitent.selectById(articleId)
        article must beFailedTry
      }
    }

    "insert(article: Map[String, Any])" should {
      "insert to DB success" in new AutoRollbackWithFixture {
        val article: Map[String, Any] = Map(
          "article_id" -> "article_id",
          "title" -> "title",
          "user_id" -> "user_id",
          "content" -> "content",
          "created" -> DateTime.now()
        )

        val result = articlePersitent.insert(article)
        result must beSuccessfulTry
        result.get must beEqualTo(1)
      }

      "insert to DB fails" in new AutoRollbackWithFixture {
        val article: Map[String, Any] = Map(
          "article_id" -> "article_id",
          "title" -> "title",
          "user_id" -> "user_id",
          "content" -> "content",
          "created" -> "wrong date"
        )

        val result = articlePersitent.insert(article)
        result must beFailedTry
      }
    }
  }

}