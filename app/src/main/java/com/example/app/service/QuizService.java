package com.example.app.service;

import com.example.app.service.common.FileOperations;
import com.example.app.dto.ThemeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizService {

	FileOperations fileOperations = new FileOperations();
	
	/**
	 * themes.json ファイルのお題をコントローラに渡す。
	 * 
	 * loadThemesメソッドによって渡されたthemes.jsonのお題のリストからランダムに一つ取り出す。
	 * 
	 * @return リストから取り出したランダムな任意のお題
	 */
	public String generateQuestion() {
		List<String> themes = new ArrayList<>();;

		try{
			ThemeList list = fileOperations.loadThemes();
			themes = list.getEasy();
			
		}catch(Exception e){

		}
		return themes.get(new Random().nextInt(themes.size()));
	}

	// クライアントが送ってきた回答の正誤判定
	public boolean isCorrectAnswer(String question, String userAnswer) {
		return question.equalsIgnoreCase(userAnswer);
	}
	
}
