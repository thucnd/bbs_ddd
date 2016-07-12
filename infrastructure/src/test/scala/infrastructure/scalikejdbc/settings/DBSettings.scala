package infrastructure.scalikejdbc.settings

/**
 * Created by thuc_nd on 9/30/15.
 */

import scalikejdbc._

trait DBSettings {
  DBSettings.initialize()
}

object DBSettings {
  private var isInitialized = false

  def initialize(): Unit = this.synchronized {
    if (isInitialized) return
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost/test?characterEncoding=UTF-8", "root", "123456")
    GlobalSettings.loggingSQLErrors = false
    GlobalSettings.sqlFormatter = SQLFormatterSettings("utils.HibernateSQLFormatter")
    isInitialized = true
  }

}

