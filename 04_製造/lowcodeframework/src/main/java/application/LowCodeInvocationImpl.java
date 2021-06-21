package application;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.sun.jersey.api.view.Viewable;

import common.LowCodeConsts;
import domain.LowCodeInvocationDomainService;
import exception.ErrorViewTransitionException;
import exception.LowCodeRequestException;
import exception.LowCodeSystemException;
import framework.LowCodeInvocation;

/**
 * ローコード画面遷移フレームワークの実行クラス。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeInvocationImpl implements LowCodeInvocation {
	private String setting; // 設定ディレクトリ
	private File settingFile; // 設定ファイル
	private Map<String, String> bindVariable; // バインド変数
	private String viewTransitionPtn; // 画面遷移パターン
	private Logger log = Logger.getLogger(LowCodeInvocationImpl.class); // ログ
	private LowCodeInvocationDomainService service; // ドメインサービスクラス

	/**
	 * コンストラクタ
	 * 
	 * @param setting           設定ディレクトリパス
	 * @param bindVariable      バインド変数
	 * @param viewTransitionPtn 画面遷移パターン
	 */
	public LowCodeInvocationImpl(String setting, Map<String, String> bindVariable, String viewTransitionPtn) {
		if (setting.endsWith("/") || setting.endsWith("\\")) {
			this.setting = setting;
		} else {
			this.setting = setting + "\\";
		}
		this.settingFile = new File(this.setting + LowCodeConsts.SETTING_XML_FILE);
		this.bindVariable = bindVariable;
		this.viewTransitionPtn = viewTransitionPtn;
		// ログファイルの設定
		DOMConfigurator.configure(this.setting + LowCodeConsts.LOG4J_XML_FILE);
		// ドメインサービスクラスの生成
		this.service = new LowCodeInvocationDomainService();
	}

	/**
	 * 画面遷移の実行
	 * 
	 * @param identifer 識別子
	 * @return 遷移画面Viewable
	 * @throws LowCodeRequestException 入力チェック例外クラス
	 * @throws LowCodeSystemException  想定外例外クラス
	 */
	public Viewable invoke(String identifer) throws LowCodeRequestException, LowCodeSystemException {
		Map<String, String> settingContext = null;
		try {
			// 開始ログの出力
			log.info(String.format("「%s」を開始します。", viewTransitionPtn));

			// 設定ファイルの読込み
			settingContext = this.service.readSetting(this.settingFile, identifer);
			if (settingContext == null) {
				// デフォルトページに遷移
				log.warn(String.format("指定された識別子が見つからなかった為、デフォルト画面に遷移します。識別子: %s", identifer));
				settingContext = this.service.readSetting(this.settingFile, LowCodeConsts.DEFAULT_IDENTIFIER);
			}

			// 設定ファイルの入力チェック
			this.service.checkSettingContext(settingContext);

			// SQLの実行
			Map<String, List<Map<String, String>>> responseModel = new HashMap<String, List<Map<String, String>>>();
			if (!settingContext.get(LowCodeConsts.SETTING_SQL).contentEquals("")) {
				List<Map<String, String>> list = this.service.executeSql(
						this.setting + LowCodeConsts.DB_PROPERTIES_FILE, settingContext.get(LowCodeConsts.SETTING_SQL),
						bindVariable, this.viewTransitionPtn);
				if (list != null) {
					// レスポンスモデルの作成
					responseModel.put("model", list);
				}
			}

			// 画面情報の作成
			Viewable view = this.service.createViewable(settingContext.get(LowCodeConsts.SETTING_VIEW), responseModel);

			// 終了ログの出力
			log.info(String.format("「%s」を終了します。", viewTransitionPtn));

			return view;

		} catch (LowCodeRequestException e) {
			log.error(e.getMessage());
			throw e;

		} catch (ErrorViewTransitionException e) {
			log.error(e.getMessage());
			// エラー画面遷移
			return this.service.createViewable(settingContext.get(LowCodeConsts.SETTING_ERROR_VIEW), null);

		} catch (Exception e) {
			log.error(String.format("想定外のエラーが発生しました。%s", e.getMessage()));
			throw new LowCodeSystemException(e.getMessage());
		}
	}
}
