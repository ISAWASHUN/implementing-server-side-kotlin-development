/**
 * MockMvc のカスタマイズ
 *
 * Response の Content-Type に"charset=UTF-8"を付与
 * 理由
 * - デフォルトだと日本語が文字化けするため
 */
@Component
class CustomizedMockMvc : MockMvcBuilderCustomizer {
    override fun customize(builder: ConfigurableMockMvcBuilder<*>?) {
        builder?.alwaysDo { result -> result.response.characterEncoding = "UTF-8" }
    }
}
