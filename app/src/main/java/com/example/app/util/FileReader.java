package com.example.app.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.stream.Collectors;
import java.io.File;
import java.util.List;


@Component
public class FileReader {

	// プロジェクト外のファイルを読み取るメソッド
	public String readFileFromResources(String filePath) throws IOException{
		ClassLoader classLoader = FileReader.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("files/"+filePath);
		StringBuilder content  = new StringBuilder();

		// 該当ファイルが存在しなかったら
		if(inputStream == null){
			String rootPath = System.getProperty("user.dir") + "/src";
			File dir = new File(rootPath);
      List<String> fileNames = new ArrayList<>();
			StringBuilder result = new StringBuilder();

			File[] files = dir.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						fileNames.add(file.getName());  // ファイル名をリストに追加
						
						result.append(file.getName())
						.append(System.lineSeparator());
					}
				}
			}else{

				return "ファイルない " + rootPath;
			}

			return result.toString();

		}else{

			if(getFileExtension(filePath).equals("txt")){
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
					String line;
					while ((line = reader.readLine()) != null) {
						content.append(line).append("<br>");
					}
				}
			//バイナリファイルだったらを想定
			}else{
				byte[] fileBytes = Files.readAllBytes(Paths.get( System.getProperty("user.dir")+"/src/main/resources/Files/"+filePath)); // バイナリデータを取得
        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
				String imageTag = "<img src='data:image/png;base64," + base64Encoded + "' />";
				return imageTag;
			}
		}

		return content.toString() + getFileExtension(filePath);
	}

	public String getFileExtension(String fileName) {

    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}
