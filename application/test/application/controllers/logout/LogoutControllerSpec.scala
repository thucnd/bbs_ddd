package application.controllers.logout

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.runner.JUnitRunner
import play.api.test._

/**
 * Created by dell5460 on 7/8/2015.
 */
@RunWith(classOf[JUnitRunner])
class LogoutControllerSpec extends PlaySpecification with Mockito {

  "LogoutController" should {
    "logout()" should {

      "logout success" in new WithApplication {
        val home = route(FakeRequest(GET, "/logout")).get

        status(home) must equalTo(SEE_OTHER)
      }
    }
  }
}
