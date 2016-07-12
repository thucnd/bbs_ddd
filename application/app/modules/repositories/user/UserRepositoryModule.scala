package modules.repositories.user

import com.google.inject.AbstractModule
import domain.lifecycle.user.UserRepository

/**
 * Created by thuc_nd on 10/27/15.
 */
class UserRepositoryModule extends AbstractModule {
  override def configure(): Unit = bind(classOf[UserRepository])
    .toInstance(UserRepository())
}
