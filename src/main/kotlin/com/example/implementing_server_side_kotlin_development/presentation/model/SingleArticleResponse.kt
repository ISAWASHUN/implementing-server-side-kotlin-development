/**
 * 単一記事取得のレスポンス
 *
 * @property article 作成済み記事
 */
data class SingleArticleResponse(
    @field:Valid
    @Schema(required = true, description = "")
    @field:JsonProperty("article", required = true) val article: Article
)
