package exception;

/**
 * エラー画面遷移例外クラス
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class ErrorViewTransitionException extends LowCodeException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param msg メッセージ
	 */
	public ErrorViewTransitionException(String msg) {
		super(msg);
	}
}
