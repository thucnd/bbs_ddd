package controllers.login

import javax.inject.Inject

import domain.exceptions.UserNotFoundException
import exceptions.PasswordInvalidException
import forms.LoginForm
import play.api.mvc._
import services.login.LoginService
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.Play.current

/**
 * Created by ThucNd on 10/13/15.
 */
class LoginController @Inject() (loginService: LoginService) extends Controller {

  def authenticate: EssentialAction = Action { implicit request =>
    LoginForm.form.bindFromRequest.fold(
      errors => BadRequest(views.html.login.index(errors)),

      loginForm => {
        loginService.login(loginForm.email, loginForm.password).map {
          user =>
            Redirect(controllers.article.routes.ArticleController.showListPage)
              .withSession("email" -> user.email, "name" -> user.name)
        }.recover {
          case e: PasswordInvalidException => Redirect(controllers.login.routes.LoginController.index)
            .flashing("error" -> Messages("error.login.password"))
          case e: UserNotFoundException => Redirect(controllers.login.routes.LoginController.index)
            .flashing("error" -> Messages("error.emailOrPassword.wrong"))

        }.get
      }
    )
  }

  def index: EssentialAction = Action { implicit request =>
    Ok(views.html.login.index(LoginForm.form))
  }

}
