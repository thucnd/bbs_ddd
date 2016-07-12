package modules.services.article

import com.google.inject.AbstractModule
import services.article.ArticleService

class ArticleServiceModule extends AbstractModule {
  override def configure(): Unit = bind(classOf[ArticleService])
    .toInstance(ArticleService())
}
