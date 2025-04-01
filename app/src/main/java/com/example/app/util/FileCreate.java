package com.example.app.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class FileCreate {
	public String fileCreate()throws IOException{
		StringBuilder filePath = new StringBuilder("/app/src/main/resources/Files");
		filePath.append("/sample.csv");

		List<String[]> data = Arrays.asList(
			new String[]{"1", "田中", "25"},
			new String[]{"2", "佐藤", "30"},
			new String[]{"3", "鈴木", "28"}
		);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()))) {

		for(String[] data_array : data){
			writer.write(String.join(",", data_array));
			writer.write(System.lineSeparator()); 
		}

			return "CSVファイルが作成されました: " + filePath;

		} catch (IOException e) {
			e.printStackTrace();
			return "CSVファイルの作成に失敗しました: " + filePath;
		}
	}
}
