package com.example.app.util;

import java.io.IOException;
import java.nio.file.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DirectoryCreate {
	public String directoryCreate(String deleteFlg){
	
		StringBuilder directoryPathString = new StringBuilder("/app/src/main/resources/Directories");

		// 現在の日付を取得
		LocalDate currentDate = LocalDate.now();

		// フォーマットを指定 (yyyyMMdd)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// LocalDate を文字列に変換
		String formattedDate = currentDate.format(formatter);

		directoryPathString.append("/"+formattedDate);

		Path dirPath = Paths.get(directoryPathString.toString());

		if(deleteFlg == "delete"){
			try {
				Files.createDirectories(dirPath);  // 必要な親ディレクトリもすべて作成
				return "ディレクトリが作成されました: " + dirPath;
			} catch (Exception e) {
					e.printStackTrace();

					return "ディレクトリの作成に失敗しました";
			}
		}else{
			Path startPath = Paths.get(directoryPathString.toString());

			StringBuilder dirStructure = new StringBuilder();

			try {
				Files.walk(startPath)
				.sorted((child, parent) -> parent.compareTo(child))
				.findFirst()  // 最初の要素だけを取得
				.ifPresent(path -> {
						dirStructure.append(path);
						dirStructure.append("<br>");
				});
        } catch (IOException e) {
            e.printStackTrace();
        }
			return dirStructure.toString();
		}
	}	
}
