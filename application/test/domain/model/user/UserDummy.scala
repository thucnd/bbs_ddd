package domain.model.user

/**
 * Created by thuc_nd on 10/28/15.
 */
object UserDummy {
  def apply(userId: UserId, name: String, company: Company, email: String, password: String): User = User(userId, name, company, email, password)
}
