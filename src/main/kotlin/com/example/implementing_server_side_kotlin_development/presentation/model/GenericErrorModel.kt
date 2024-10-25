/**
 * エラーモデル
 *
 * エラーの内容レスポンスモデル
 *
 * @property errors
 */
data class GenericErrorModel(
  @field:Valid
  @Schema(required = true, description = "")
  @field:JsonProperty("errors", required = true) val errors: GenericErrorModelErrors
)