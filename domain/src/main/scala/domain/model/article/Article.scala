package domain.model.article

import org.joda.time.DateTime
import domain.model.user.User

/**
 * Created by ThucNd on 10/14/2015.
 */
trait Article {
  val id: ArticleId

  val user: User

  val title: String

  val content: String

  val createdDate: DateTime

  override def equals(other: Any): Boolean = other match {
    case that: Article => that.id == this.id
    case _             => false
  }

  override def hashCode: Int = 31 * id.##
}

object Article {

  private[domain] def apply(
    id:          ArticleId,
    user:        User,
    title:       String,
    content:     String,
    createdDate: DateTime
  ): Article = ArticleImpl(
    id = id,
    user = user,
    title = title,
    content = content,
    createdDate = createdDate
  )
}

