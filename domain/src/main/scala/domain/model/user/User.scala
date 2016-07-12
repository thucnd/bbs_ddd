package domain.model.user

import domain.model.article.Article

/**
 * Created by HoaiPT on 10/21/2015.
 */
trait User {
  val id: UserId

  val name: String

  val company: Company

  val email: String

  val password: String

  override def equals(other: Any): Boolean = other match {
    case that: User => that.id == this.id
    case _          => false
  }

  override def hashCode: Int = 31 * id.##

  def createArticle(title: String, content: String): Article

  def createUser(name: String, companyName: String, email: String, password: String): User
}

object User {

  private[domain] def apply(
    id:       UserId,
    name:     String,
    company:  Company,
    email:    String,
    password: String
  ): User = UserImpl(
    id = id,
    name = name,
    company = company,
    email = email,
    password = password
  )
}
