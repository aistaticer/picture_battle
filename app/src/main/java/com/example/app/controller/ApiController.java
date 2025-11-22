package com.example.app.controller;

import com.example.app.util.RSAKeyGenerator;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.service.BoardGameService;
import com.example.app.service.QuizService;
import com.example.app.dto.BoardState;
import com.example.app.dto.CheckAnswerRequest;
import com.example.app.dto.sendThemeResponse;
import com.example.app.entity.Game;
import com.example.app.entity.Team;
import com.example.app.dto.CheckAnswerRequest;
import com.example.app.dto.Reward;
import com.example.app.service.RedisService;
import com.example.app.service.UserService;
import com.example.app.mapper.BoardMapper;
import com.example.app.model.Board;
import com.example.app.repository.jpa.GameRepository;
import com.example.app.repository.jpa.TeamRepository;
import com.example.app.repository.jpa.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(
  origins = "http://localhost:3001",
  methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
  allowedHeaders = "*"
)
public class ApiController {

    RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator();
    QuizService quizService = new QuizService();
    BoardGameService gameService = new BoardGameService();
    
    private final RedisService redisService;
    private final WebSocketBroadcaster webSocketBroadcaster;
    private final UserService userService;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    public ApiController(RedisService redisService,WebSocketBroadcaster webSocketBroadcaster, UserService userService, GameRepository gameRepository, TeamRepository teamRepository) {
        this.redisService = redisService;
        this.webSocketBroadcaster = webSocketBroadcaster;
        this.userService = userService;
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
    }

    @PostMapping("/data")
    public ResponseEntity<Map<String, String>> receiveData(@RequestBody Map<String, String> payload) {
        System.out.println("受信データ: " + payload.get("message"));
        Map<String, String> response = new HashMap<>();
        Random random = new Random();
        int number = random.nextInt(8);
        int color = random.nextInt(3);

        response.put("number", String.valueOf(number));
        response.put("color", String.valueOf(color));
        return ResponseEntity.ok(response);  // JSON形式でレスポンスを返す
    }

    /**
     * お題を作成してクライアントに返す
     * @return　日本語のお題
     * @throws Exception
     */
    @GetMapping("/theme")
    public ResponseEntity<sendThemeResponse> submitTheme() throws Exception {

        sendThemeResponse response = new sendThemeResponse(
            quizService.generateQuestion()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkAnswer")
    public ResponseEntity<Reward> checkAnswer(@RequestBody CheckAnswerRequest request) throws Exception {

        String answer = request.getAnswer();

        int reward = answer.length();

        Reward response = new Reward(reward);

        System.out.println(answer+"fecthされた"+response.getReward());

        return ResponseEntity.ok(response);
    }

    /**
     * 更新されたデータを受け取り、盤面の状態を更新
     * @return　日本語のお題
     * @throws Exception
     */
    @PostMapping("/startGame")
    public ResponseEntity<Map<String, String>> startGame(@RequestBody Map<String, String> body) {

        String userId = body.get("userId");
        String gameId = body.get("gameId");

        List<Team> teams = teamRepository.findByGameId(UUID.fromString(gameId));

        Board board = gameService.getInitialBoard(7,teams);

        redisService.saveBoard("board1",board);

        System.out.println(redisService.getBoard("board1"));

        //webSocketBroadcaster.broadcastTogame("1","データ送ります");
        webSocketBroadcaster.printAllSessions();

        // DBに仮登録したgame_idを使用　いつか変える
        UUID game_Id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        //userService.registerUser(game_Id,"taro");

        return ResponseEntity.ok(Map.of("gameId", gameId));
    }

    /**
     * 更新されたデータを受け取り、盤面の状態を更新
     * @return　日本語のお題
     * @throws Exception
     */
    //@PostMapping("/api/board")
    //public ResponseEntity<BoardState> receiveBoardState(@RequestBody BoardState boardState) {
    //    System.out.println(boardState);
    //    return ResponseEntity.ok().build();
    //}

    /**
     * 作成された秘密鍵をクライアントに返す
     * ※ 開発・デバッグ目的。秘密鍵をクライアントなどに渡すのはセキュリティ上危険。
     *
     * @return Base64エンコードされた秘密鍵の文字列
     * @throws Exception 鍵生成時のエラー
     */
    /*@GetMapping("/board/init")
    public ResponseEntity<BoardDTO> getFirstBoard() throws Exception{
        BoardDTO board = gameService.getInitialBoard(4);
        return ResponseEntity.ok(board);
    }*/

        
    /**
     * 作成された秘密鍵をクライアントに返す
     * ※ 開発・デバッグ目的。秘密鍵をクライアントなどに渡すのはセキュリティ上危険。
     *
     * @return Base64エンコードされた秘密鍵の文字列
     * @throws Exception 鍵生成時のエラー
     */
    @GetMapping("/dataExample")
    public String ex() throws Exception{
        return rsaKeyGenerator.exString();
    }

    
}

