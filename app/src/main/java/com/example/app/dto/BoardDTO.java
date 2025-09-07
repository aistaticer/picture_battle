package com.example.app.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private String boardId;
	Map<String,TileDTO> tiles;
}
