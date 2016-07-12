package controllers.logout

import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.mvc._

class LogoutController extends Controller {

  def logout: EssentialAction = Action { implicit request =>
    Redirect(controllers.login.routes.LoginController.index).withNewSession.flashing(
      "success" -> Messages("message.logout.success")
    )
  }
}

