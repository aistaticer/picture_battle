package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * websocket接続(join)の際に返されるボードの状態
 */
public class BoardResponseDTO {
    private String type;
    private String action;
    private String myTeamName;
    private List<String> teamNames;
    private BoardDTO board;
}
