package controllers.signup

import javax.inject.Inject

import controllers.Secured
import domain.lifecycle.user.UserRepository
import exceptions.EmailExistingException
import forms.UserForm
import play.api.mvc._
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import services.signup.SignUpService

class SignUpController @Inject() (val userRepository: UserRepository, signUpService: SignUpService) extends Controller with Secured {

  def index: EssentialAction = isAuthenticated { _ =>
    implicit request =>
      Ok(views.html.signup.index(UserForm.form))
  }

  def register: EssentialAction = isAuthenticated { user =>
    implicit request =>
      UserForm.form.bindFromRequest.fold(
        errors => BadRequest(views.html.signup.index(errors)),

        userForm => {
          signUpService.createUser(user, userForm.name, userForm.email, userForm.company, userForm.password).map {
            _ =>
              Redirect(controllers.article.routes.ArticleController.showListPage)
                .flashing("success" -> Messages("message.user.create.success"))
          }.recover {
            case em: EmailExistingException => Redirect(controllers.signup.routes.SignUpController.index)
              .flashing("error" -> Messages("error.email.existing"))
          }.get
        }
      )
  }
}

