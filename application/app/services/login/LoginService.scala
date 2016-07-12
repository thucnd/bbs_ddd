package services.login

import domain.lifecycle.user.UserRepository
import domain.model.user.User
import exceptions.PasswordInvalidException
import infrastructure.util.security.HashCreator
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.{ Try, Success, Failure }

/**
 * Created by thuc_nd on 10/21/15.
 */
class LoginService(userRepository: UserRepository) {

  def login(email: String, password: String)(implicit session: DBSession = AutoSession): Try[User] = {
    (
      for {
        user <- userRepository.resolveByEmail(email)
        isCorrectPassword = user.password == HashCreator.create(password)
      } yield (user, isCorrectPassword)
    ).flatMap {
        result: (User, Boolean) =>
          result match {
            case (_, false)   => Failure(new PasswordInvalidException)
            case (user, true) => Success(user)
          }
      }
  }
}

object LoginService {
  def apply(): LoginService = new LoginService(UserRepository())
}
