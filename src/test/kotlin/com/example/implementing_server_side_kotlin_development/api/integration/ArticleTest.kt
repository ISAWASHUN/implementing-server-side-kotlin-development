class ArticleTest {
  @SpringBootTest
  @AutoConfigureMockMvc
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  @DBRider
  class GetArticle(
      @Autowired val mockMvc: MockMvc,
  ) {
      // 略

      @Test
      fun `異常系-slug のフォーマットが不正な場合、バリデーションエラー`() {
          /**
           * given:
           * - 不正なフォーマットの slug
           */
          val slug = "dummy-slug"

          /**
           * when:
           */
          val response = mockMvc.get("/api/articles/$slug") {
              contentType = MediaType.APPLICATION_JSON
          }.andReturn().response
          val actualStatus = response.status
          val actualResponseBody = response.contentAsString

          /**
           * then:
           * - ステータスコードが一致する
           * - レスポンスボディが一致する
           */
          val expectedStatus = HttpStatus.FORBIDDEN.value()
          val expectedResponseBody = """
              {
                "errors": {
                  "body": [
                    "slug は 32 文字の英小文字数字です。"
                  ]
                }
              }
          """.trimIndent()
          assertThat(actualStatus).isEqualTo(expectedStatus)
          JSONAssert.assertEquals(
              expectedResponseBody,
              actualResponseBody,
              JSONCompareMode.NON_EXTENSIBLE
          )
      }
  }
}
