interface ShowArticleUseCase {
  fun execute(slug: String): EitherNel<Error, CreatedArticle> = throw NotImplementedError()

  sealed interface Error {
    data class ValidationErrors(val errors: List<ValidationError>) : Error

    data class NotFoundArticle(val slug: String) : Error
  }
}

@Service
class ShowArticleUseCaseImpl(
  val articleRepository: ArticleRepository
): ShowArticleUseCase {
  override fun execute(slug: String) : Either<ShowArticleUseCase.Error, CreatedArticle> {
    /**
     * slug の検証
     *
     * 不正な slug だった場合、早期 return
     */
    val validatedSlug = Slug.new(slug).getOrElse {return ShowArticleUseCase.Error.ValidationErrors(it.all).left()}

    /**
     * 記事の取得
     *
     * 取得失敗した場合、早期 return
     */
    val createdArticle = articleRepository.findBySlug(validatedSlug).getOrElse { 
      return when (it) {
        is ArticleRepository.FindBySlugError.NotFound -> ShowArticleUseCase.Error.NotFoundArticle(it.slug.value).left()
      }
     }

    return createdArticle.right()
  }
}
