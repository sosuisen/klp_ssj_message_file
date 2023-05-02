package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.example.model.ErrorBean;
import com.example.model.LoginUserModel;
import com.example.model.MessageDTO;
import com.example.model.MessageFileDTO;
import com.example.model.MessagesModel;

import org.apache.commons.io.IOUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotSupportedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import lombok.NoArgsConstructor;

/**
 * Jakarta MVCのコンロトーラクラスです。@Controllerアノテーションを付けましょう。
 * 
 * コントローラクラスはCDI beanであることが必須で、必ず@RequestScopedを付けます。
 * 
 * CDI beanには引数のないコンストラクタが必須なので、
 * Lombokの@NoArgsConstructorで空っぽのコンストラクタを作成します。
 * ただし、このクラスは宣言時に初期化してないfinalフィールドを持つため、
 * このままだとフィールドが初期化されない可能性があってコンパイルエラーとなります。
 * よって、force=true指定で該当フィールドを0かfalseかnullで初期化する処理を追加します。
 */
@Controller
@RequestScoped
@NoArgsConstructor(force = true)
@Path("/")
public class MyController {
	// uploaderRootで指定したディレクトリ直下に uploaded という名前のフォルダを作ること
	private final String uploaderRoot = "C:\\pleiades-ssj2023";
	private final String uploaderDirName = "uploaded";

	private final MessagesModel messagesModel;

	private final LoginUserModel loginUserModel;

	private final ErrorBean errorBean;

	// @Injectはコンストラクタインジェクションを用いるのが定石です。
	@Inject
	public MyController(MessagesModel messagesModel, LoginUserModel loginUserModel, ErrorBean errorBean) {
		this.messagesModel = messagesModel;
		this.loginUserModel = loginUserModel;
		this.errorBean = errorBean;
	}

	@Inject
	private Models models;

	/**
	 * @Path がないため、このメソッドはクラス全体が扱うURLのパスを扱います。
	 */
	@GET
	public String home() {
		models.put("appName", "メッセージアプリ");
		return "index.jsp";
	}

	@GET
	@Path("list")
	public String getMessage() {
		// 今回はここで強制的にユーザ名をセットしておきます。
		// 今後は、ログイン処理を追加し、その時にセットする必要があります。
		this.loginUserModel.setName("鴨川三条");

		models.put("uploaderDirName", uploaderDirName);

		return "list.jsp";
	}

	@POST
	@Path("list")
	public String postMessage(@BeanParam MessageDTO mes) {
		messagesModel.add(mes);
		// リダイレクトは "redirect:リダイレクト先のパス"
		return "redirect:list";
	}

	@POST
	@Path("fileupload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String postFileUpload(@FormParam("name") String name,
			@FormParam("message") String message,
			@FormParam("uploadfile") EntityPart uploadFile) {
		String fileName = uploadFile.getFileName().orElseThrow(NotSupportedException::new);
		String utfFileName = new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		try (
				InputStream content = uploadFile.getContent();
				var out = new FileOutputStream(
						uploaderRoot + File.separator + uploaderDirName + File.separator + utfFileName);
			) {
			IOUtils.copy(content, out);
			messagesModel.add(new MessageFileDTO(name, message, utfFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:list";
	}

	@GET
	@Path("clear")
	public String clearMessage() {
		messagesModel.clear();
		return "redirect:list";
	}

	@GET
	@Path("login")
	public String getLogin() {
		return "login.jsp";
	}

	@POST
	@Path("login")
	public String postLogin() {
		return "redirect:list";
	}
}
