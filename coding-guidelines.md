# 1. コードスタイル
- インデント: 2スペースを使用
- 行末に不要なスペースを置かない
- 関数を閉じる中かっこは関数名すぐ後ろ　中かっこの前にスペースを配置(例: 下記コードに記載)
```
void example() {
  logger.debug("This is a debug message");
}
```

# 2. コメントの書き方
- 必要な場合のみ簡潔に記述する。
- 1行コメントには // を、複数行コメントには /* */ を使用。

# 3. 変数のスコープと使用
## スコープ
- 変数は可能な限りローカルスコープで宣言。
- クラス変数や静的変数（`static`）を使用する場合は、その使用目的を明確化。
- グローバルスコープの使用は最小限に抑える。

# 4. エラー処理
## 例外処理
- 必要な箇所には必ず try-catch を使用。
## エラーメッセージ
- 明確かつ理解しやすいメッセージを提供。
## ログ
- ログレベルに応じて適切なメソッドを使用 (例: debug, info, warn, error)。

# 5. 変数の宣言方法
- 再代入が不要な場合は `final` をデフォルトで使用。
- 再代入が必要な場合は、コメントなどでその理由を補足。
- `var` の使用は、簡潔性やコードの可読性が向上する場面に限定。
- 型を明示する場合は、適切な型を使用してコードの意図を明確にする。


# 6. 命名ルール
- クラス名: パスカルケース (例: `MyClass`)
- メソッド名: キャメルケース (例: `myMethod`)
- 定数: 全て大文字とアンダースコア (例: `MY_CONSTANT`)
- ファイル名: スネークケース (例: `my_file.js`)

# 7. テストコード
- 単体テストを書くことを必須とする
- テストは常に自動化し、CI パイプラインに組み込む
- テストのカバレッジ: 少なくとも 80% 以上のカバレッジを目指す
- テストメソッド名はそのテストが何をテストしているかがわかるように記述

# 8. セキュリティ
- ハードコーディングの禁止
## パスワードやAPIキーなどをコードに直接記述しない。
- .env ファイルやセキュアなキーストアを使用。
## 入力値の検証
- ユーザー入力には必ずバリデーションを実施。