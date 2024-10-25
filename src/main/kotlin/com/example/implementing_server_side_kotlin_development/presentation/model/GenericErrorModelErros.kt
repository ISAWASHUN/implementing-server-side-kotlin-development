/**
 * エラーモデル
 *
 * エラーレスポンスの詳細を List<String> 型で記述する
 *
 * @property body
 */
data class GenericErrorModelErrors(
    @Schema(required = true, description = "")
    @field:JsonProperty("body", required = true) val body: List<String>,
)
