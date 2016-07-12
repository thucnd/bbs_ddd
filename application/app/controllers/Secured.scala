package controllers

import domain.lifecycle.user.UserRepository
import domain.model.user.User
import play.api.mvc._

/**
 * Provide security features
 */
trait Secured {

  protected val userRepository: UserRepository

  val authInformationKey: String = "email"

  private def getUserInfo(request: RequestHeader): Option[User] = {
    userRepository.resolveByEmail(request.session.get(authInformationKey).getOrElse("")).map(user => Some(user)).getOrElse(None)
  }

  private def onUnauthorized(request: RequestHeader): Result = Results.Unauthorized

  def isAuthenticated(f: => User => Request[AnyContent] => Result): EssentialAction =
    Security.Authenticated(getUserInfo, onUnauthorized) { userInfo =>
      Action(request => f(userInfo)(request))
    }
}
