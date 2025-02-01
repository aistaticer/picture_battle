package com.example.app;  // パッケージ宣言を追加

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")  // 明示的に "/" にマッピング
    public String home() {
        return "Welcome to the Home Page!";
    }
}
