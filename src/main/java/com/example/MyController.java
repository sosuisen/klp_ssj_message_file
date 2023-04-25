package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

import com.example.model.ErrorBean;
import com.example.model.LoginUser;
import com.example.model.MessageDTO;
import com.example.model.MessageFileDTO;
import com.example.model.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
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

	private final Messages messages;

	private final LoginUser loginUser;

	private final ErrorBean errorBean;

	// @Injectはコンストラクタインジェクションを用いるのが定石です。
	@Inject
	public MyController(Messages messages, LoginUser loginUser, ErrorBean errorBean) {
		this.messages = messages;
		this.loginUser = loginUser;
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
		this.loginUser.setName("鴨川三条");

		models.put("uploaderDirName", uploaderDirName);

		return "list.jsp";
	}

	@POST
	@Path("list")
	public String postMessage(@BeanParam MessageDTO mes) {
		messages.add(mes);
		// リダイレクトは "redirect:リダイレクト先のパス"
		return "redirect:list";
	}

	@POST
	@Path("fileupload")
	@Consumes("multipart/form-data")
	public String postFileUpload(List<EntityPart> parts) {
		var mes = new MessageFileDTO();
		for (EntityPart part : parts) {
			String name = part.getName();
			Optional<String> fileName = part.getFileName();
			InputStream is = part.getContent();
			MultivaluedMap<String, String> partHeaders = part.getHeaders();
			MediaType mediaType = part.getMediaType();

			String data = "";
			try {
				switch (name) {
				case "name" -> {
					data = IOUtils.toString(is, StandardCharsets.UTF_8);
					mes.setName(data);
				}
				case "message" -> {
					data = IOUtils.toString(is, StandardCharsets.UTF_8);
					mes.setMessage(data);
				}
				case "uploadfile" -> {
					var uploadedFileName = new String(fileName.get().getBytes("iso-8859-1"), "utf-8");
					var out = new FileOutputStream(
							uploaderRoot + File.separator + uploaderDirName + File.separator + uploadedFileName);
					IOUtils.copy(is, out);
					mes.setFileName(uploadedFileName);
				}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 参考のため出力
			System.out.println("name: %s, data: %s, fileName: %s, mediaType: %s".formatted(name, data, fileName,
					mediaType.toString()));
		}

		messages.add(mes);

		return "redirect:list";
	}

	@GET
	@Path("clear")
	public String clearMessage() {
		messages.clear();
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
