package com.example.app.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.util.FileCreate;
import com.example.app.util.FileReader;
import com.example.app.util.DirectoryCreate;

@RestController
public class FileController {

    private final FileReader fileReader;
		FileCreate fileCreate = new FileCreate();
		DirectoryCreate directoryCreate = new DirectoryCreate();		

    public FileController(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    // `resources/files/` 内のファイルを読み取るエンドポイント
    @GetMapping("/read-file")
    public String readFile(@RequestParam String filename) throws IOException{
        return fileReader.readFileFromResources(filename);
    }

		@GetMapping("/create-file")
    public String createFile()throws IOException{
        return fileCreate.fileCreate();
    }

		@GetMapping("/create-directory")
    public String createDirectory(@RequestParam String deleteFlg)throws IOException{
        return directoryCreate.directoryCreate(deleteFlg);
    }

		

}
