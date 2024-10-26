/**
 * 作成済記事記事のコントローラー
 *
 * @property showArticleUseCase 単一記事取得ユースケース
 */
@RestController
@Validated
class ArticleController(val showArticleUseCase: ShowArticleUseCase) {
    /**
     * 単一の作成済記事取得
     *
     * @return
     */
    @Operation(
        summary = "単一記事取得",
        operationId = "getArticle",
        description = "slug に一致する記事を取得します。",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        schema = Schema(implementation = SingleArticleResponse::class),
                        examples = [
                            ExampleObject(
                                name = "OK",
                                value = """
                                    {
                                        "article": {
                                            "slug": "283e60096c26aa3a39cf04712cdd1ff7",
                                            "title": "title",
                                            "description": "description",
                                            "body": "body"
                                        }
                                    }
                                """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = [
                    Content(
                        schema = Schema(implementation = GenericErrorModel::class),
                        examples = [
                            ExampleObject(
                                name = "Not Found",
                                value = """
                                    {
                                        "errors": {
                                            "body": [
                                                "slug に該当する記事は見つかりませんでした"
                                            ]
                                        }
                                    }
                                """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    @GetMapping("/articles/{slug}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getArticle(
        @Parameter(description = "記事の slug", required = true) @Valid @PathVariable("slug") @Length(min = 32, max = 32) slug: String,
    ): ResponseEntity<SingleArticleResponse> {
        /**
         * 作成済記事の取得
         */
        val createdArticle = showArticleUseCase.execute(slug).getOrElse { throw ShowArticleUseCaseErrorException(it) } // TODO から ShowArticleUseCaseErrorException に変更する

        return ResponseEntity(
            SingleArticleResponse(
                article = Article(
                    slug = createdArticle.slug.value,
                    title = createdArticle.title.value,
                    description = createdArticle.description.value,
                    body = createdArticle.body.value,
                ),
            ),
            HttpStatus.OK
        )
    }

    /**
     * 単一記事取得ユースケースがエラーを戻したときの Exception
     *
     * このクラスの例外が発生したときに、@ExceptionHandler で例外処理をおこなう
     *
     * @property error
     */
    data class ShowArticleUseCaseErrorException(val error: ShowArticleUseCase.Error) : Exception()

    /**
     * ShowArticleUseCaseErrorException をハンドリングする関数
     *
     * ShowArticleUseCase.Error の型に合わせてレスポンスを分岐させる
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = [ShowArticleUseCaseErrorException::class])
    fun onShowArticleUseCaseErrorException(e: ShowArticleUseCaseErrorException): ResponseEntity<GenericErrorModel> =
        when (val error = e.error) {
            /**
             * 原因: slug に該当する記事が見つからなかった
             */
            is ShowArticleUseCase.Error.NotFoundArticleBySlug -> ResponseEntity<GenericErrorModel>(
                GenericErrorModel(
                    errors = GenericErrorModelErrors(
                        body = listOf("${error.slug} に該当する記事は見つかりませんでした")
                    )
                ),
                HttpStatus.NOT_FOUND
            )

            /**
             * 原因: バリデーションエラー
             */
            is ShowArticleUseCase.Error.ValidationErrors -> ResponseEntity<GenericErrorModel>(
                GenericErrorModel(
                    errors = GenericErrorModelErrors(
                        body = error.errors.map { it.message }
                    )
                ),
                HttpStatus.BAD_REQUEST
            )
        }
}
