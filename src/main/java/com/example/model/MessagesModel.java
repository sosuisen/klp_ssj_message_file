package com.example.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;


/**
 * MessagesModelはArrayListを継承するリストクラスです。
 */
/**
 * @ApplicationScoped アノテーションを付けることで、CDI beanとして宣言しています。
 * このクラスのインスタンスのライフサイクルは、
 * Application Scope（アプリが起動してから終了するまで）となります。
 */
/**
 * CDI beanに名前を付ける場合、@Namedアノテーションを用います。
 * 例えば、@Named("FOO")と付けると"FOO"という名前になりますが、
 * 引数を省略すると、クラス名の先頭を小文字にした名前、
 * 下記では "messagesModel" という名前になります。
 * 例えば、JSP内ではこの名前でインスタンスを参照することができます。
 */
@ApplicationScoped
@Named
public class MessagesModel extends ArrayList<MessageDTO> implements Serializable {
	// データの保存先を適切なパスへ変更してください。
	private String saveFilePath = "c:\\pleiades-ssj2023\\data.json";

	/**
	 * コンストラクタが呼ばれた直後に実行する処理。
	 * 
	 * @PostConstruct アノテーションを付けて定義します。
	 * MessagesModelクラスはアプリケーションスコープであるため、
	 * 生成されるのはアプリが起動したときの1度きりですから、
	 * コンストラクタの中に処理を書いても結果は同じです。
	 * 他のスコープだと動作は変わります。
	 * 
	 * saveFilePathからJSON文字列を読み込んだ後、
	 * JsonbでMessagesModelオブジェクトへ変換し、リストへ追加しています。
	 * addAllはArrayListから継承したメソッドなのでここで呼ぶことができます。
	 */
	@PostConstruct
	public void prepare() {
		Jsonb jsonb = JsonbBuilder.create();
		try {
			String json = Files.readString(java.nio.file.Path.of(saveFilePath));
			addAll(jsonb.fromJson(json, MessagesModel.class));
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	/**
	 * このインスタンスが破棄される直前に実行する処理。
	 * 
	 * @PreDestroy アノテーションを付けて定義します。
	 * MessagesModelクラスはアプリケーションスコープであるため、
	 * 破棄されるのはアプリが終了するときです。
	 * 
	 * リスト（this、つまり自分自身）の内容をJsonbでJSON文字列へ変換して、
	 * saveFilePathへ保存しています。
	 */
	@PreDestroy
	public void after() {
		Jsonb jsonb = JsonbBuilder.create();
		String json = jsonb.toJson(this);
		try {
			Files.writeString(java.nio.file.Path.of(saveFilePath), json);
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
}
