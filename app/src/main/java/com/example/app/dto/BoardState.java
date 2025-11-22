package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardState {
	private String type;
	private List<Integer> position;

}
