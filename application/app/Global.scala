import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
/**
 * Created by hoai_pt on 10/13/15.
 */
object Global extends GlobalSettings {

  override def onError(request: RequestHeader, ex: Throwable): Future[Result] = {
    Future.successful(InternalServerError(
      views.html.errorPage(ex)
    ))
  }

}
