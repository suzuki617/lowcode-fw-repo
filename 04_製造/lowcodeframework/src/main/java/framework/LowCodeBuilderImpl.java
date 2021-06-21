package framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import application.LowCodeInvocationImpl;
import common.LowCodeConsts;

/**
 * ローコード画面遷移フレームワークのビルダー実装クラス。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeBuilderImpl implements LowCodeBuilder {
	private String setting; // 設定ディレクトリパス
	private Map<String, String> queryParams; // クエリパラメータ
	private byte[] messageBody; // メッセージボディ

	/**
	 * 設定ファイルパスの設定
	 * 
	 * @param setting 設定ファイルパス
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setSetting(String setting) {
		this.setting = setting;
		return this;
	}

	/**
	 * クエリパラメータの設定
	 * 
	 * @param queryParams クエリパラメータ
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setQueryParams(MultivaluedMap<String, String> queryParams) {
		this.queryParams = new HashMap<String, String>();
		for (Entry<String, List<String>> entry : queryParams.entrySet()) {
			this.queryParams.put(entry.getKey(), entry.getValue().get(0));
		}

		return this;
	}

	/**
	 * メッセージボディの設定
	 * 
	 * @param messageBody メッセージボディ
	 * @return LowCodeBuilderクラス
	 */
	public LowCodeBuilder setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
		return this;
	}

	/**
	 * ビルド実行。 ローコード画面遷移フレームワークの実行インターフェース（LowCodeInvocation）を取得する。
	 * 
	 * @return LowCodeInvocationクラス
	 */
	public LowCodeInvocation build() {
		LowCodeInvocation invocation;
		if (messageBody != null) {
			Map<String, String> messageBodyMap = this.changeBytesToMap(this.messageBody);
			invocation = new LowCodeInvocationImpl(setting, messageBodyMap, LowCodeConsts.VIEW_TRANSITION_POST);
		} else {
			invocation = new LowCodeInvocationImpl(setting, queryParams, LowCodeConsts.VIEW_TRANSITION_GET);
		}
		return invocation;
	}

	/**
	 * バイト文字列から、Map型に変換する。
	 * 
	 * @param bytes バイト文字列
	 * @return 変換したMap
	 */
	private Map<String, String> changeBytesToMap(byte[] bytes) {
		String bytesStr = new String(bytes);
		String[] strArr = bytesStr.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String str : strArr) {
			String[] tmpArr = str.split("=");
			map.put(tmpArr[0], tmpArr[1]);
		}
		return map;
	}
}
