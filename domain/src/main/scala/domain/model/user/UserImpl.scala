package domain.model.user

import java.util.UUID

import domain.model.article.{ ArticleId, Article }
import infrastructure.util.security.HashCreator
import org.joda.time.DateTime

/**
 * Created by HoaiPT on 10/21/2015.
 */
private[user] case class UserImpl(
    id:       UserId,
    name:     String,
    company:  Company,
    email:    String,
    password: String
) extends User {
  override def createArticle(title: String, content: String): Article =
    Article(
      ArticleId(UUID.randomUUID.toString),
      this,
      title,
      content,
      DateTime.now()
    )

  override def createUser(name: String, companyName: String, email: String, password: String): User =
    User(
      id = UserId(UUID.randomUUID.toString),
      name = name,
      company = Company(companyName),
      email = email,
      password = HashCreator.create(password)
    )
}
