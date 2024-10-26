/**
 * 作成済記事リポジトリの具象クラス
 *
 */
@Repository
class ArticleRepositoryImpl(
  val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
  ): ArticleRepository {
  /**
   * slug から作成済記事を取得
   *
   * @param slug
   * @return
   */
  override fun findBySlug(slug: Slug): Either<FindBySlugError, CreatedArticle> {
    override fun findBySlug(slug: Slug): Either<FindBySlugError, CreatedArticle> {
      val sql = """
        SELECT
          articles.slug,
          articles.title,
          articles.description,
          articles.body
        FROM 
          articles
        WHERE 
          articles.slug = :slug
      """.trimIndent()
      val articleMapList = namedParameterJdbcTemplate.queryForList(sql, MapSqlParameterSource().addValue("slug", slug.value))

        /**
         * DB から作成済記事が見つからなかった場合、早期 return
         */
        if (articleMapList.isEmpty()) {
            return ArticleRepository.FindBySlugError.NotFound(slug = slug).left()
        }

        val articleMap = articleMapList.first()

        return CreatedArticle.newWithoutValidation(
            slug = Slug.newWithoutValidation(articleMap["slug"].toString()),
            title = Title.newWithoutValidation(articleMap["title"].toString()),
            description = Description.newWithoutValidation(articleMap["description"].toString()),
            body = Body.newWithoutValidation(articleMap["body"].toString()),
        ).right()
    }
  }
}