package exception;

/**
 * 入力チェック例外クラス
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeRequestException extends LowCodeException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param msg メッセージ
	 */
	public LowCodeRequestException(String msg) {
		super(msg);
	}
}
