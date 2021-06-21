package framework;

import javax.ws.rs.core.MultivaluedMap;

/**
 * ローコード画面遷移フレームワークのビルダーインターフェース。
 * 本インターフェースを使用して、ローコード画面遷移フレームワークの実行インターフェース（LowCodeInvocation）を取得する。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public interface LowCodeBuilder {
	/**
	 * 設定ディレクトリの設定
	 * 
	 * @param setting 設定ディレクトリパス
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setSetting(String setting);

	/**
	 * クエリパラメータの設定
	 * 
	 * @param queryParams クエリパラメータ
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setQueryParams(MultivaluedMap<String, String> queryParams);

	/**
	 * メッセージボディの設定
	 * 
	 * @param messageBody メッセージボディ
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setMessageBody(byte[] messageBody);

	/**
	 * ビルド実行。 ローコード画面遷移フレームワークの実行インターフェース（LowCodeInvocation）を取得する。
	 * 
	 * @return LowCodeInvocationクラス
	 */
	public LowCodeInvocation build();
}
