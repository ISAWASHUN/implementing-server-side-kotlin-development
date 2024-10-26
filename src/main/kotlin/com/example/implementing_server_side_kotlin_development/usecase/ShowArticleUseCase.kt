interface ShowArticleUseCase {
  fun execute(slug: String): EitherNel<Error, CreatedArticle> = throw NotImplementedError()

  sealed interface Error {
    data class ValidationErrors(val errors: List<ValidationError>) : Error

    data class NotFoundArticle(val slug: String) : Error
  }
}

@Service
class ShowArticleUseCaseImpl: ShowArticleUseCase