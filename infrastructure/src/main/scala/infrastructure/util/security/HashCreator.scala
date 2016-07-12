package infrastructure.util.security

import java.security.MessageDigest

/**
 * Created by dell5460 on 7/13/2015.
 */
object HashCreator {

  private def hashByteToString(hash: Array[Byte]): String = {
    val builder: StringBuilder = new StringBuilder
    hash.foreach(x => {
      if ((0xff & x) < 0x10) {
        builder.append("0" + Integer.toHexString(0xff & x))
      } else {
        builder.append(Integer.toHexString(0xff & x))
      }
    })
    builder.toString()
  }

  def create(password: String): String = {
    hashByteToString(MessageDigest.getInstance("SHA").digest(password.getBytes))
  }
}
