package infrastructure.util

import infrastructure.util.security.HashCreator
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

/**
 * Test for Hash creator
 */
@RunWith(classOf[JUnitRunner])
class HashCreatorSpec extends Specification {

  "Hash creator function" should {
    "create(s: String): String" should {
      "check convert password to md5" in {
        HashCreator.create("12345678") must beEqualTo("7c222fb2927d828af22f592134e8932480637c0d")
      }
    }
  }
}
