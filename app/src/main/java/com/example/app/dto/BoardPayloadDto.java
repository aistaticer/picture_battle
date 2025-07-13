package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.app.model.Board;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPayloadDto {
	private String roomId;
	private BoardDTO board;	
}
