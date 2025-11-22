package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TileResponseDTO {
	private String type;
	private String action;
	private TileDTO tileDTO;
}
