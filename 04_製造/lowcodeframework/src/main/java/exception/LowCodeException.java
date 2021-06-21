package exception;

/**
 * ローコード画面フレームワークの例外クラス 例外に関しては、本クラスを継承して作成する。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * 
	 * @param msg メッセージ
	 */
	public LowCodeException(String msg) {
		super(msg);
	}
}
