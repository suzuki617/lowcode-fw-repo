
package com.tsuzuki.lowcode.testproject;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.view.Viewable;

import exception.LowCodeException;
import exception.LowCodeRequestException;
import exception.LowCodeSystemException;
import framework.LowCodeBuilder;
import framework.LowCodeBuilderImpl;

@Path("/myresource/{resource}")
public class MyResource {
    
	/**
	 * 「DBからデータを取得して画面遷移」の実行
	 * 
	 * @param uriInfo urlInfo
	 * @return 遷移先画面
	 * @throws LowCodeRequestException 入力チェック例外クラス
	 * @throws LowCodeSystemException  想定外例外クラス
	 */
    @GET 
    public Viewable executeGetTransition(@Context UriInfo uriInfo) {
    	String resourceName = uriInfo.getPath().split("/")[1];
    	LowCodeBuilder builder = new LowCodeBuilderImpl();
    	
    	try {
    		MultivaluedMap<String,String> queryParams = uriInfo.getQueryParameters(); 
    		return builder.setSetting("C:\\Users\\suzuk")
    		.setQueryParams(queryParams).build().invoke("test_get1");
    	} catch(LowCodeException e) {
    		// フレームワーク内でエラーがあった場合。
    		return new Viewable("/system_error.jsp");
    	}
    }
    	
	/**
	 * 「DBにデータ保存をして画面遷移」の実行
	 * 
	 * @Context UriInfo uriInfo
	 * @param input POST送信されたbyte文字列
	 * @return 遷移先画面
	 * @throws LowCodeRequestException 入力チェック例外クラス
	 * @throws LowCodeSystemException  想定外例外クラス
	 */
    @POST 
    public Viewable executePostTransition(@Context UriInfo uriInfo, byte[] input) {
    	String resourceName = uriInfo.getPath().split("/")[1];
    	LowCodeBuilder builder = new LowCodeBuilderImpl();
    	
    	try {
    		return builder.setSetting("C:\\Users\\suzuk\\")
    		.setMessageBody(input).build().invoke(resourceName);
    	} catch(LowCodeException e) {
    		// フレームワーク内でエラーがあった場合。
    		return new Viewable("/system_error.jsp");
    	}
    }
}
