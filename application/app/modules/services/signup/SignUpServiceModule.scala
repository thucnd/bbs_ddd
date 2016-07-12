package modules.services.signup

import com.google.inject.AbstractModule
import services.signup.SignUpService

/**
 * Created by thuc_nd on 10/27/15.
 */
class SignUpServiceModule extends AbstractModule {
  override def configure(): Unit = bind(classOf[SignUpService])
    .toInstance(SignUpService())
}
