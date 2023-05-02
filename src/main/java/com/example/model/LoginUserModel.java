package com.example.model;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Data;

/**
 * LoginUserModelはログインユーザの情報を保持するクラスです。
 */
/**
 * @SessionScoped アノテーションを付けることで、CDI beanとして宣言しています。
 * このクラスのインスタンスのライフサイクルは、
 * セッションスコープ（セッションが有効な間）となります。
 */
/**
 * CDI beanに名前を付ける場合、@Namedアノテーションを用います。
 * 例えば、@Named("FOO")と付けると"FOO"という名前になりますが、
 * 引数を省略すると、クラス名の先頭を小文字にした名前、
 * 下記では "loginUserModel" という名前になります。
 * 例えば、JSP内ではこの名前でインスタンスを参照することができます。
 */
/**
 * @Data はLombokのアノテーションです。
 */
@SessionScoped
@Named
@Data
public class LoginUserModel implements Serializable {
	private String name = null;
}

