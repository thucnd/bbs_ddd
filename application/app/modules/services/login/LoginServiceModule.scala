package modules.services.login

import com.google.inject.AbstractModule
import services.login.LoginService

class LoginServiceModule extends AbstractModule {
  override def configure(): Unit = bind(classOf[LoginService])
    .toInstance(LoginService())
}
