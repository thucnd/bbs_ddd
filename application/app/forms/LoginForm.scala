package forms

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created by thuc_nd on 10/21/15.
 */
case class LoginForm(email: String, password: String)

object LoginForm {
  val EMAIL_REGEX = "(.*).@septeni-(original\\.co\\.jp|technology\\.jp)$"
  val PASSWORD_MAX_LENGTH = 20
  val PASSWORD_MIN_LENGTH = 6

  val form: Form[LoginForm] = Form(
    mapping(
      "email" -> nonEmptyText.verifying("error.emailIsNotRecognize", _.matches(EMAIL_REGEX)),
      "password" -> text(
        minLength = PASSWORD_MIN_LENGTH,
        maxLength = PASSWORD_MAX_LENGTH
      )
    )(LoginForm.apply)(LoginForm.unapply)
  )
}
