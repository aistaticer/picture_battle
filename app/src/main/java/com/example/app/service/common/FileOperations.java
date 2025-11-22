package com.example.app.service.common;

import com.example.app.dto.ThemeList;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileOperations {

/**
 * themes.json ファイルを読み込み、お題リストを返すメソッド。
 * 
 * Files/themes.jsonファイルをクラスパスから読み込み、
 * JSONデータをthemeListオブジェクトとしてデシリアライズ(JSON→オブジェクト)します。
 * 
 * @return 読み込んだテーマリストを含む `themeList` オブジェクト
 * @throws IOException ファイルの読み込みやデータのデシリアライズ中に発生した例外
 */
public ThemeList loadThemes() throws IOException {

	ObjectMapper mapper = new ObjectMapper();

	// themes.jsonファイルのクラスパスを取得
	ClassPathResource resource = new ClassPathResource("Files/themes.json");

	// ファイルを読み込む
	try (InputStream inputStream = resource.getInputStream()) {
		// JSONをthemeListオブジェクトにデシリアライズして返す
		return mapper.readValue(inputStream, ThemeList.class);
	} catch (IOException e) {
		System.out.println("テーマの読み込みに失敗しました: " + e.getMessage());
		throw e;
	}
}

}
