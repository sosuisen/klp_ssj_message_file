package com.example.model;

import lombok.Getter;
import lombok.Setter;

/**
 * メッセージ情報の受け渡しに用いるDTO（Data Transfer Object）です。
 * 次の3か所で利用されます。
 * 1) list.jspで表示したフォームからPOSTされたデータを、
 *   MyControllerのpostMessageメソッドの@BeanParamへ注入。
 * 2) posetMessageメソッド内でmessagesに追加。
 * 3) list.jspでmessagesから取り出されて表示。
 * 
 * @BeanParam でデータを渡すためには
 * list.jspのフォーム内でのinput要素のnameと
 * クラスのフィールドとの対応付けを
 * @FormParam で指定しておく必要があります。
 */
@Setter @Getter
public class MessageFileDTO extends MessageDTO {
	private String message;
	private String name;
	private String fileName;
}