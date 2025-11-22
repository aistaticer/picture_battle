package com.example.app.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThemeList {
	private List<String> easy;

	// デフォルトコンストラクタを追加
	public ThemeList() {
		this.easy = new ArrayList<>();
	}
}
