package domain.lifecycle.user

import domain.model.user.User
import infrastructure.scalikejdbc.UserPersistent
import scalikejdbc.{ AutoSession, DBSession }
import scala.util.Try

trait UserRepository {
  def resolveByEmail(email: String)(implicit session: DBSession = AutoSession): Try[User]
  def store(user: User)(implicit session: DBSession = AutoSession): Try[User]
}

object UserRepository {
  def apply(): UserRepository = new UserRepositoryOnDbImpl(new UserPersistent)
}
