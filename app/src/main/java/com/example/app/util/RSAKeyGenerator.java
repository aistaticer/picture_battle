package com.example.app.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyGenerator {
    public static void main(String[] args) throws Exception {
        // RSA 鍵ペアを生成
        /*KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // 2048ビットの鍵を生成
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 公開鍵と秘密鍵をBase64エンコードして表示
        System.out.println("公開鍵:");
        System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

        System.out.println("秘密鍵:");
        System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    */}

		/**
		 * RSA鍵ペアを生成し、秘密鍵をBase64形式の文字列として返す。
		 * ※ 開発・デバッグ目的。秘密鍵をクライアントなどに渡すのはセキュリティ上危険。
		 *
		 * @return Base64エンコードされた秘密鍵の文字列
		 * @throws Exception 鍵生成時のエラー
		 */
		public String exString() throws Exception {
			// RSA 鍵ペアを生成
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);  // 2048ビットの鍵を生成
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			// 公開鍵と秘密鍵をBase64エンコードして表示
			System.out.println("公開鍵:");
			System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

			System.out.println("秘密鍵:");
			System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
	
			return Base64.getEncoder().encodeToString(privateKey.getEncoded());
		}  
}
