package domain.lifecycle.user

import domain.model.user.{ Company, User, UserId }
import domain.exceptions.{ DbAccessException, UserNotFoundException }
import infrastructure.scalikejdbc.UserPersistent
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
 * Created by HoaiPT on 10/21/2015.
 */
private[user] class UserRepositoryOnDbImpl(userPersistent: UserPersistent) extends UserRepository {
  override def resolveByEmail(email: String)(implicit session: DBSession = AutoSession): Try[User] = Try {
    convertToEntity(userPersistent.selectByEmail(email)
      .getOrElse(throw new UserNotFoundException))
  }

  override def store(entity: User)(implicit session: DBSession = AutoSession): Try[User] = Try {
    userPersistent.insert(convertToMap(entity)).map(_ => entity)
      .getOrElse(throw new DbAccessException)
  }

  private def convertToEntity(m: Map[String, Any]): User =
    User(
      UserId(m("user_id").toString),
      m("name").toString,
      Company(m("company_name").toString),
      m("email").toString,
      m("password").toString
    )

  private def convertToMap(entity: User): Map[String, Any] =
    Map(
      "user_id" -> entity.id.value,
      "name" -> entity.name,
      "company_name" -> entity.company.name,
      "email" -> entity.email,
      "password" -> entity.password
    )
}
