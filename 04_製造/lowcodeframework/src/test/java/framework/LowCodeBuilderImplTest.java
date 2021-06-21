package framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import exception.LowCodeRequestException;
import exception.LowCodeSystemException;

/**
 * LowCodeBuilderImplのテストクラス。
 * 
 * @author t_suzuki
 * @version 1.0.0
 */
public class LowCodeBuilderImplTest {
	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： LowCodeBuilderImplの全体を通じた正常ケース。「DBからデータを取得して画面遷移」を実行。
	 * 
	 * 結果： 遷移先画面がtest_get.jspであること。レスポンスモデルがDBから取得した値であること。
	 */
	@Test
	public void test_正常系_DBからデータを取得して画面遷移() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		ArrayList<String> paramValue = new ArrayList<String>();
		paramValue.add("value1");
		queryParams.put("key1", paramValue);
		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework")
				.setQueryParams(queryParams).build();
		try {
			Viewable view = invocation.invoke("test_get");
			// 遷移先画面がtest_get.jspであること。
			assertEquals(view.getTemplateName(), "/test_get.jsp");
			// レスポンスモデルがDBから取得した値であること。
			@SuppressWarnings("unchecked")
			Map<String, List<Map<String, String>>> model = (Map<String, List<Map<String, String>>>) view.getModel();
			List<Map<String, String>> list = model.get("model");
			assertEquals(list.get(0).get("id"), "1");
			assertEquals(list.get(0).get("name"), "suzuki");
			assertEquals(list.get(0).get("email"), "suzuki@test.co.jp");
		} catch (LowCodeRequestException e) {
			fail("LowCodeRequestException");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： LowCodeBuilderImplの全体を通じた正常ケース。「DBにデータ保存をして画面遷移」を実行（INSERT文実行）。
	 * 
	 * 結果： 遷移先画面がtest_post.jspであること。INSERT結果が正しいこと。
	 */
	@Test
	public void test_正常系_DBにデータ保存をして画面遷移_INSERT() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		String str = "id=2&name=yamada&email=yamada@test.co.jp";
		byte[] sbyte = str.getBytes();
		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework\\")
				.setMessageBody(sbyte).build();
		try {
			Viewable view = invocation.invoke("test_post_insert");
			// 遷移先画面がtest_post.jspであること。
			assertEquals(view.getTemplateName(), "/test_post.jsp");
			// INSERT結果が正しいこと。
			ResultSet rset = stmt.executeQuery("SELECT * FROM Employee WHERE id = 2;");
			while (rset.next()) {
				assertEquals(rset.getString("name"), "yamada");
				assertEquals(rset.getString("email"), "yamada@test.co.jp");
			}
		} catch (LowCodeRequestException e) {
			fail("LowCodeRequestException");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		} catch (SQLException e) {
			fail("SQLException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： LowCodeBuilderImplの全体を通じた正常ケース。「DBにデータ保存をして画面遷移」を実行（UPDATE文実行）。
	 * 
	 * 結果： 遷移先画面がtest_post.jspであること。UPDATE結果が正しいこと。
	 */
	@Test
	public void test_正常系_DBにデータ保存をして画面遷移_UPDATE() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		String str = "id=1&name=tanaka&email=tanaka@test.co.jp";
		byte[] sbyte = str.getBytes();
		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework")
				.setMessageBody(sbyte).build();
		try {
			Viewable view = invocation.invoke("test_post_update");
			// 遷移先画面がtest_post.jspであること。
			assertEquals(view.getTemplateName(), "/test_post.jsp");
			// INSERT結果が正しいこと。
			ResultSet rset = stmt.executeQuery("SELECT * FROM Employee WHERE id = 1;");
			while (rset.next()) {
				assertEquals(rset.getString("name"), "tanaka");
				assertEquals(rset.getString("email"), "tanaka@test.co.jp");
			}
		} catch (LowCodeRequestException e) {
			fail("LowCodeRequestException");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		} catch (SQLException e) {
			fail("SQLException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： 識別子が見つからなくて、default画面に遷移する純正常ケース。
	 * 
	 * 結果： 遷移先画面がindex.jspであること。
	 */
	@Test
	public void test_準正常系_default遷移() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework\\")
				.build();
		try {
			Viewable view = invocation.invoke("no_exist");
			// 遷移先画面がindex.jspであること。
			assertEquals(view.getTemplateName(), "/index.jsp");
		} catch (LowCodeRequestException e) {
			fail("LowCodeRequestException");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： Viewファイルパスが存在しない異常ケース。
	 * 
	 * 結果： LowCodeRequestExceptionがthrowされること。
	 */
	@Test
	public void test_異常系_Viewファイルパスが存在しない() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework")
				.build();
		try {
			invocation.invoke("error_case_view");
			fail("NORMAL END");
		} catch (LowCodeRequestException e) {
			assertEquals(e.getMessage(), "Viewファイルのパスが見つかりません。");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： SQLファイルパスが存在しない異常ケース。
	 * 
	 * 結果： LowCodeRequestExceptionがthrowされること。
	 */
	@Test
	public void test_異常系_SQLファイルパスが存在しない() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework\\")
				.build();
		try {
			invocation.invoke("error_case_sql");
			fail("NORMAL END");
		} catch (LowCodeRequestException e) {
			assertEquals(e.getMessage(), "SQLファイルのパスが見つかりません。");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： エラー画面ファイルパスが存在しない異常ケース。
	 * 
	 * 結果： LowCodeRequestExceptionがthrowされること。
	 */
	@Test
	public void test_異常系_エラー画面ファイルパスが存在しない() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework")
				.build();
		try {
			invocation.invoke("error_case_error_view");
			fail("NORMAL END");
		} catch (LowCodeRequestException e) {
			assertEquals(e.getMessage(), "エラー画面ファイルのパスが見つかりません。");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	/**
	 * 対象： LowCodeBuilderImpl
	 * 
	 * 概要： SQLの実行に失敗する異常ケース。
	 * 
	 * 結果： エラー画面に遷移すること。
	 */
	@Test
	public void test_異常系_SQL実行失敗() {
		// DB初期化
		Statement stmt = this.getConnectionStatement();
		this.dbInit(stmt);

		LowCodeBuilderImpl testClass = new LowCodeBuilderImpl();
		LowCodeInvocation invocation = testClass.setSetting("C:\\Users\\suzuk\\eclipse-workspace\\lowcodeframework\\")
				.build();
		try {
			Viewable view = invocation.invoke("error_execute_sql");
			// 遷移先画面がsystem_error.jspであること。
			assertEquals(view.getTemplateName(), "/system_error.jsp");
		} catch (LowCodeRequestException e) {
			fail("LowCodeRequestException");
		} catch (LowCodeSystemException e) {
			fail("LowCodeSystemException");
		}
	}

	private void dbInit(Statement stmt) {
		try {
			stmt.execute("TRUNCATE TABLE Employee;");
			stmt.execute("INSERT INTO employee VALUES (1, 'suzuki', 'suzuki@test.co.jp');");
		} catch (SQLException e) {
			// 何もしない
		}
	}

	private Statement getConnectionStatement() {
		Connection conn = null;

		String url = "jdbc:postgresql://localhost:5432/postgres";
		String user = "postgres";
		String password = "pass";
		try {
			// PostgreSQLへ接続
			conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();
			return stmt;
		} catch (Exception e) {
			// 何もしない
			return null;
		}
	}
}
