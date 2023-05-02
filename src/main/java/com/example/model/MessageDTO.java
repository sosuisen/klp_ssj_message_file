package com.example.model;

import jakarta.ws.rs.FormParam;
import lombok.Data;

/**
 * メッセージ情報の受け渡しに用いるDTO（Data Transfer Object）です。
 * 次の3か所で利用されます。
 * 1) list.jspで表示したフォームからPOSTされたデータを、
 *   MyControllerのpostMessageメソッドの@BeanParamへ注入。
 * 2) postMessageメソッド内でmessagesModelに追加。
 * 3) list.jspでmessagesModelから取り出されて表示。
 * 
 * @BeanParam でデータを渡すためには
 * list.jspのフォーム内でのinput要素のnameと
 * クラスのフィールドとの対応付けを
 * @FormParam で指定しておく必要があります。
 */
@Data
public class MessageDTO {
	@FormParam("message")
	private String message;
	@FormParam("name")
	private String name;
}