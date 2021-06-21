package domain;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sun.jersey.api.view.Viewable;

import common.LowCodeConsts;
import exception.ErrorViewTransitionException;
import exception.LowCodeRequestException;
import infrastructure.LowCodeRepository;

/**
 * ローコードフレームワークの業務クラス
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeInvocationDomainService {
	private Logger log = Logger.getLogger(LowCodeInvocationDomainService.class);
	private LowCodeRepository repo; // リポジトリクラス

	/**
	 * コンストラクタ
	 */
	public LowCodeInvocationDomainService() {
		this.repo = new LowCodeRepository();
	}

	/**
	 * 設定ファイル読込
	 * 
	 * @param settingFile 設定ファイル
	 * @param identifier  識別子
	 * @return 設定情報
	 * @throws LowCodeRequestException 入力チェック例外
	 */
	public Map<String, String> readSetting(File settingFile, String identifier) throws LowCodeRequestException {
		try {
			return this.repo.readSetting(settingFile, identifier);
		} catch (Exception e) {
			throw new LowCodeRequestException(e.getMessage());
		}
	}

	/**
	 * 設定ファイル内容チェック
	 * 
	 * @param context 設定ファイルの内容
	 * @throws LowCodeRequestException 入力チェック例外
	 */
	public void checkSettingContext(Map<String, String> context) throws LowCodeRequestException {
		// Viewファイルのチェック
		this.repo.checkFileExistence(context.get(LowCodeConsts.SETTING_VIEW), "Viewファイルのパスが見つかりません。");
		// SQLファイルのチェック
		if (!context.get(LowCodeConsts.SETTING_SQL).contentEquals("")) {
			this.repo.checkFileExistence(context.get(LowCodeConsts.SETTING_SQL), "SQLファイルのパスが見つかりません。");
		}
		// エラー画面ファイルのチェック
		this.repo.checkFileExistence(context.get(LowCodeConsts.SETTING_ERROR_VIEW), "エラー画面ファイルのパスが見つかりません。");
	}

	/**
	 * SQL実行
	 * 
	 * @param dbPropertiesFilePath DB設定ファイルパス
	 * @param sqlFilePath          SQLファイルパス
	 * @param bindVariable         バインド変数
	 * @param viewTransitionPtn    画面遷移パターン
	 * @return SQL実行結果
	 * @throws ErrorViewTransitionException エラー画面遷移例外
	 */
	public List<Map<String, String>> executeSql(String dbPropertiesFilePath, String sqlFilePath,
			Map<String, String> bindVariable, String viewTransitionPtn) throws ErrorViewTransitionException {
		try {
			// SQLファイル読込
			String sqlContext = this.repo.readSqlFile(sqlFilePath);

			// バインド変数の置換
			String sql;
			if (bindVariable != null && bindVariable.size() > 0) {
				sql = this.repo.changeBindVariable(sqlContext, bindVariable);
			} else {
				sql = sqlContext;
			}
			// DB情報が記載されているプロパティファイル読込み
			Properties dbProperties = this.repo.readPropertiesFile(dbPropertiesFilePath);
			// SQLの実行
			log.info(String.format("SQLを実行します: %s", sql));
			if (viewTransitionPtn.equals(LowCodeConsts.VIEW_TRANSITION_GET)) {
				// SELECT文実行
				return this.repo.executeSelectSql(dbProperties, sql);
			} else {
				// UPDATE（INSERT）文実行
				this.repo.executeUpdateSql(dbProperties, sql);
				return null;
			}
		} catch (SQLException e) {
			throw new ErrorViewTransitionException(e.getMessage());
		} catch (IOException e) {
			throw new ErrorViewTransitionException(e.getMessage());
		}
	}

	/**
	 * 画面情報の作成
	 * 
	 * @param viewPath      遷移先画面パス
	 * @param responseModel レスポンスモデル
	 * @return Viewable 遷移先Viewable
	 */
	public Viewable createViewable(String viewPath, Map<String, List<Map<String, String>>> responseModel) {
		File file = new File(viewPath);
		viewPath = "/" + file.getName();
		log.info(String.format("遷移先画面:  %s", viewPath));
		return this.repo.createViewable(viewPath, responseModel);
	}
}
