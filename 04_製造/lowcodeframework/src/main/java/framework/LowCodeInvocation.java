package framework;

import com.sun.jersey.api.view.Viewable;

import exception.LowCodeRequestException;
import exception.LowCodeSystemException;

/**
 * ローコード画面遷移フレームワークの実行インターフェース。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public interface LowCodeInvocation {
	/**
	 * 画面遷移の実行
	 * 
	 * @param identifer 識別子
	 * @return 遷移画面Viewable
	 * @throws LowCodeRequestException 入力チェック例外クラス
	 * @throws LowCodeSystemException  想定外例外クラス
	 */
	public Viewable invoke(String identifer) throws LowCodeRequestException, LowCodeSystemException;

}
