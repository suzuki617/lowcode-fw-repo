package exception;

/**
 * 想定外例外クラス
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeSystemException extends LowCodeException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param msg メッセージ
	 */
	public LowCodeSystemException(String msg) {
		super(msg);
	}
}
