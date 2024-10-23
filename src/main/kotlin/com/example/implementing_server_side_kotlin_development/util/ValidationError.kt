/**
 * ドメインオブジェクトのバリデーションにおけるエラー型
 *
 * 必ずエラーメッセージを記述する
 *
 */
interface ValidationError {
    /**
     * エラーメッセージ
     */
    val message: String
}
