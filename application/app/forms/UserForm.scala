package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages

/**
 * Created by thuc_nd on 9/28/15.
 */
case class UserForm(name: String, email: String, company: String, password: String, repassword: String)

object UserForm {
  val SEPTENI_TECHNOLOGY_TYPE = "1"
  val SEPTENI_ORIGINAL_TYPE = "2"
  val EMAIL_NAME_REGEX = "^[\\w]+$"

  def form()(implicit messages: Messages): Form[UserForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "emailName" -> nonEmptyText.verifying("error.emailIsNotRecognize", _.matches(EMAIL_NAME_REGEX)),
      "companyType" -> nonEmptyText,
      "password" -> text(
        minLength = LoginForm.PASSWORD_MIN_LENGTH,
        maxLength = LoginForm.PASSWORD_MAX_LENGTH
      ),
      "repassword" -> text(
        minLength = LoginForm.PASSWORD_MIN_LENGTH,
        maxLength = LoginForm.PASSWORD_MAX_LENGTH
      )
    )(toUserForm)(UserForm.unapply)
      .verifying(Messages("error.password.invalid"), f => f.password == f.repassword)
  )

  def toUserForm(name: String, emailName: String, companyType: String, password: String, repassword: String): UserForm =
    UserForm(
      name = name,
      email = getFullEmail(companyType, emailName),
      company = getCompanyName(companyType),
      password = password,
      repassword = repassword
    )

  private def getFullEmail(companyType: String, emailName: String): String =
    emailName + Map(SEPTENI_TECHNOLOGY_TYPE -> "@septeni-technology.jp", SEPTENI_ORIGINAL_TYPE -> "@septeni-original.co.jp")(companyType)

  private def getCompanyName(companyType: String): String =
    Map(SEPTENI_TECHNOLOGY_TYPE -> "Septeni Technology", SEPTENI_ORIGINAL_TYPE -> "Septeni Original")(companyType)

}
