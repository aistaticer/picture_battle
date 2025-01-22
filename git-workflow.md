# Gitの運用ルール
- 1コミットにつき1つの機能またはバグ修正に集中
- コミットメッセージは「[タイプ]: 説明」の形式を使用 (例: `fix: パスワードが異なってもログインできる問題を修正`)

# ブランチ運用方法

## 目次
1. ブランチ戦略
2. ブランチの命名規則
3. developブランチへのマージ手順及びリモートへの反映手順

---

# 1. ブランチ戦略

本プロジェクトでは以下のブランチ構造を採用します。

- **main**: 本番環境用の安定したブランチ。常にデプロイ可能な状態を維持する。
- **develop**: 開発用の統合ブランチ。各機能がここに統合され、動作確認後にmainにマージする。
- **feature/**: 機能開発用の個別ブランチ。新しい機能や修正をこのブランチで作業する。
- **hotfix/**: 緊急修正用のブランチ。mainのバグ修正を行う。

---

# 2. ブランチの命名規則

以下の命名規則を使用します。

- **feature/機能名**: 新しい機能開発
  - 例: `feature/user-login`
- **bugfix/バグ内容**: バグ修正
  - 例: `bugfix/fix-login-error`
- **hotfix/修正内容**: 本番環境の緊急修正
  - 例: `hotfix/critical-bug`

---

# 3.コンフリクト解消手順

- developブランチにいないことを確認
```
git branch

# 結果の例
* feature/login
　develop
  main
```

- リモートの最新状態を取得
```
git fetch origin feature/login
```

- feature/loginブランチに特定のブランチをマージ
```
git merge origin/feature/login
```

- マージ中に競合が発生した場合、コンフリクトを解消する
```
git status  # 競合しているファイルを確認
git add <解消したファイル>
# 全てのコンフリクトを解消したら
git commit -m "適切なコメント"
```

- マージが解消されているかを確認する
```
git status
```

# 4 プルリク手順

- ファイルをステージングしてからコミットする
```
git add .
git commit -m "適切なコメント"
```

- リモートにpush
```
git push origin feature/login
```

- GitHubでプルリクエスト作成
feature/loginブランチからdevelopブランチに対して

- プルリクがクローズ
GithubのUIにある「Merge pull request」ボタンをクリック
feature/loginブランチをdevelopブランチにマージ

- 疎通確認
リモートのdevelopブランチをローカルに取り込み動作を確認する
```
git fetch origin
git switch develop
git merge origin/develop
＃ その後動作確認
```　

- 問題がなければリモートのdevelopブランチからリモートのmainブランチに対してプルリクを発行

- プルリクが閉じられたらリモートのdevelopブランチをリモートのmainブランチにマージ

- mainブランチを最新に更新
```
git fetch origin
git switch main
git merge origin/main
```
