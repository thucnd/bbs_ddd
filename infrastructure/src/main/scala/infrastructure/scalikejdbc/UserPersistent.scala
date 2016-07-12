package infrastructure.scalikejdbc

import scalikejdbc._

import scala.util.Try

/**
 * Created by HoaiPT on 10/21/2015.
 */
class UserPersistent {

  val convertToMap = (rs: WrappedResultSet) => Map(
    "user_id" -> rs.string("user_id"),
    "name" -> rs.string("name"),
    "company_name" -> rs.string("company_name"),
    "email" -> rs.string("email"),
    "password" -> rs.string("password")
  )

  def selectByEmail(email: String)(implicit session: DBSession = AutoSession): Try[Map[String, Any]] = Try {
    SQL(
      """
        SELECT    users.user_id,
                  users.name,
                  users.company_name,
                  users.email,
                  users.password
        FROM      users
        WHERE     users.email = {email}
      """
    ).bindByName('email -> email).map(convertToMap).single().apply().get
  }

  def insert(user: Map[String, Any])(implicit session: DBSession = AutoSession): Try[Int] = Try {
    SQL(
      """
        INSERT INTO users  (user_id, name, company_name, email, password)
        VALUES             ({user_id}, {name}, {company_name}, {email}, {password})
      """
    ).bindByName(
        'user_id -> user("user_id"),
        'name -> user("name"),
        'company_name -> user("company_name"),
        'email -> user("email"),
        'password -> user("password")
      )
      .update.apply()
  }
}

