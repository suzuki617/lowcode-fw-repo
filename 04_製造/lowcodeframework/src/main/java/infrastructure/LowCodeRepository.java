package infrastructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.jersey.api.view.Viewable;

import common.LowCodeConsts;
import exception.LowCodeRequestException;

/**
 * ローコード画面遷移フレームワークのリポジトリクラス。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeRepository {
	/**
	 * 設定ファイル読込
	 * 
	 * @param settingFile 設定ファイル
	 * @param identifier  識別子
	 * @return 読み込んだ設定情報
	 * @throws ParserConfigurationException ParserConfigurationException
	 * @throws IOException                  IOException
	 * @throws SAXException                 SAXException
	 */
	public Map<String, String> readSetting(File settingFile, String identifier)
			throws ParserConfigurationException, SAXException, IOException {
		Map<String, String> context = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(settingFile);
		Element root = doc.getDocumentElement();
		NodeList children = root.getChildNodes();
		Boolean targetFlag = false;

		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);

			if (child instanceof Element) {
				Element childElement = (Element) child;
				NodeList grandChildren = childElement.getChildNodes();
				for (int j = 0; j < grandChildren.getLength(); j++) {
					Node grandChild = grandChildren.item(j);
					if (grandChild instanceof Element) {
						Element grandChildElement = (Element) grandChild;
						if (grandChildElement.getTagName().equals(LowCodeConsts.SETTING_IDENTIFIER)) {
							if (grandChildElement.getTextContent().equals(identifier)) {
								context.put(grandChildElement.getTagName(), grandChildElement.getTextContent());
								targetFlag = !targetFlag;
							}
						} else {
							if (targetFlag) {
								context.put(grandChildElement.getTagName(), grandChildElement.getTextContent());
							}
						}
					}
				}
			}

			if (targetFlag) {
				return context;
			}
		}

		// 識別子が見つからない場合
		return null;
	}

	/**
	 * 設定ファイル内のファイルパス存在チェック
	 * 
	 * @param filePath     ファイルパス
	 * @param errorMessage エラーメッセージ
	 * @throws LowCodeRequestException 入力チェック例外
	 */
	public void checkFileExistence(String filePath, String errorMessage) throws LowCodeRequestException {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new LowCodeRequestException(errorMessage);
		}
	}

	/**
	 * SQLファイルの読込み
	 * 
	 * @param sqlFilePath SQLファイルパス
	 * @return SQLファイルの内容
	 * @throws IOException IO例外
	 */
	public String readSqlFile(String sqlFilePath) throws IOException {
		File file = new File(sqlFilePath);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String lines = "";
		String line;
		while ((line = br.readLine()) != null) {
			lines += line;
		}
		br.close();
		return lines;
	}

	/**
	 * プロパティファイル読込み
	 * 
	 * @param filePath ファイルパス
	 * @return プロパティクラス
	 * @throws IOException           IO例外
	 * @throws FileNotFoundException ファイル存在しない例外
	 */
	public Properties readPropertiesFile(String filePath) throws FileNotFoundException, IOException {
		Properties conf = new Properties();
		conf.load(new FileInputStream(filePath));
		return conf;
	}

	/**
	 * バインド変数の変換
	 * 
	 * @param sqlContext   SQL
	 * @param bindVariable バインド変数
	 * @return 変換したSQLの内容
	 */
	public String changeBindVariable(String sqlContext, Map<String, String> bindVariable) {
		String sql = new String(sqlContext);
		for (Map.Entry<String, String> entry : bindVariable.entrySet()) {
			String target = "{{" + entry.getKey() + "}}";
			sql = sql.replace(target, entry.getValue());
		}
		return sql;
	}

	/**
	 * SELECT文実行
	 * 
	 * @param dbProperties DBプロパティファイル
	 * @param sql          SQL
	 * @return SQL実行結果（※Listオブジェクトに、実行結果のMapが含まれている）
	 * @throws SQLException SQLException
	 */
	public List<Map<String, String>> executeSelectSql(Properties dbProperties, String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			// PostgreSQLへ接続
			conn = this.getConnection(dbProperties);

			// SQL実行
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);

			ResultSetMetaData rsmd = rset.getMetaData();

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while (rset.next()) {
				Map<String, String> result = new HashMap<String, String>();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					result.put(rsmd.getColumnName(i), rset.getString(rsmd.getColumnName(i)));
				}
				list.add(result);
			}
			conn.commit();
			return list;
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		} finally {
			try {
				if (rset != null)
					rset.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * UPDATE文（INSERT文）実行
	 * 
	 * @param dbProperties DBプロパティファイル
	 * @param sql          SQL
	 * @return SQL実行件数
	 * @throws SQLException SQLException
	 */
	public int executeUpdateSql(Properties dbProperties, String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			// PostgreSQLへ接続
			conn = this.getConnection(dbProperties);

			// SQL実行
			stmt = conn.createStatement();
			int rowcount = stmt.executeUpdate(sql);
			conn.commit();
			return rowcount;
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		} finally {
			try {
				if (rset != null)
					rset.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * DBコネクション取得
	 * 
	 * @param dbProperties DBプロパティファイル
	 * @return DBコネクション
	 * @throws SQLException
	 */
	private Connection getConnection(Properties dbProperties) throws SQLException {
		String url = dbProperties.getProperty("url");
		String user = dbProperties.getProperty("user");
		String password = dbProperties.getProperty("password");
		// PostgreSQLへ接続
		Connection conn = DriverManager.getConnection(url, user, password);
		// 自動コミットOFF
		conn.setAutoCommit(false);
		return conn;
	}

	/**
	 * 画面情報の作成
	 * 
	 * @param viewPath      遷移先画面パス
	 * @param responseModel レスポンスモデル
	 * @return Viewableクラス
	 */
	public Viewable createViewable(String viewPath, Map<String, List<Map<String, String>>> responseModel) {
		return new Viewable(viewPath, responseModel);
	}
}
