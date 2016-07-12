package services.signup

import domain.exceptions.UserNotFoundException
import domain.lifecycle.user.UserRepository
import domain.model.user.User
import exceptions.EmailExistingException
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.{ Failure, Try }

/**
 * Created by thuc_nd on 10/27/15.
 */
class SignUpService(userRepository: UserRepository) {
  def createUser(user: User, name: String, email: String, companyName: String, password: String)(implicit session: DBSession = AutoSession): Try[User] = {
    userRepository.resolveByEmail(email).map {
      _ =>
        Failure(new EmailExistingException)
    }.recover {
      case ex: UserNotFoundException =>
        userRepository.store(user.createUser(name, companyName, email, password))
    }.get
  }
}

object SignUpService {
  def apply(): SignUpService = new SignUpService(UserRepository())
}
