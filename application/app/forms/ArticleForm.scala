package forms

import play.api.data.Form
import play.api.data.Forms._

case class ArticleForm(title: String, content: String)
object ArticleForm {

  val TEXT_REGEX = "^[\\x20-\\x7E\\s]+$"

  val MIN_ARTICLE_TITLE_LENGHT = 1
  val MAX_ARTICLE_TITLE_LENGHT = 40

  val MIN_ARTICLE_CONTENT_LENGHT = 1
  val MAX_ARTICLE_CONTENT_LENGHT = 200

  val form = Form(
    mapping(
      "title" -> nonEmptyText(MIN_ARTICLE_TITLE_LENGHT, MAX_ARTICLE_TITLE_LENGHT)
        .verifying("error.noSpecialCharacter", _.matches(TEXT_REGEX)),
      "content" -> nonEmptyText(MIN_ARTICLE_CONTENT_LENGHT, MAX_ARTICLE_CONTENT_LENGHT)
        .verifying("error.noSpecialCharacter", _.matches(TEXT_REGEX))
    )(ArticleForm.apply)(ArticleForm.unapply)
  )
}
