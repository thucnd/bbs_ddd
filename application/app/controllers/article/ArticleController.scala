package controllers.article

import javax.inject.Inject

import controllers.Secured
import domain.lifecycle.user.UserRepository
import exceptions.EmailNotMatchException
import forms.ArticleForm
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.mvc.Action
import services.article.ArticleService
import play.api.mvc._

/**
 * Created by hoai_pt on 10/13/15.
 */
class ArticleController @Inject() (val userRepository: UserRepository, articleService: ArticleService) extends Controller with Secured {

  def showListPage: EssentialAction = Action { implicit request =>
    Ok(views.html.article.index(articleService.getArticleList.get))
  }

  def view(articleId: String): EssentialAction = Action { implicit request =>
    Ok(views.html.article.detail(articleService.getArticleDetail(articleId).get))
  }

  def showCreatePage: EssentialAction = isAuthenticated { _ =>
    implicit request =>
      Ok(views.html.article.createArticlePage(ArticleForm.form))
  }

  def create: EssentialAction = isAuthenticated { user =>
    implicit request =>
      ArticleForm.form.bindFromRequest().fold(
        formWithErrors => {
          BadRequest(views.html.article.createArticlePage(formWithErrors))
        },
        newArticleInfo => {
          articleService.createArticle(
            user,
            newArticleInfo.title,
            newArticleInfo.content
          ).map { _ =>
            Redirect(routes.ArticleController.showListPage)
              .flashing("success" -> Messages("success.createArticleSuccess"))
          }.recover {
            case e: EmailNotMatchException => Redirect(controllers.article.routes.ArticleController.showCreatePage)
              .flashing("error" -> Messages("error.email.match"))
          }.get
        }
      )
  }
}
