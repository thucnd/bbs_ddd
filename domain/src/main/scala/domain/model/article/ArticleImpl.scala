package domain.model.article

import org.joda.time.DateTime
import domain.model.user.User

/**
 * Created by dell5460 on 8/28/2015.
 */
private[article] case class ArticleImpl(
  id:          ArticleId,
  user:        User,
  title:       String,
  content:     String,
  createdDate: DateTime
) extends Article
