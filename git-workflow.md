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

# 3. developブランチへのマージ手順及びリモートへの反映手順

- developブランチにいないことを確認
```
git branch

# 結果の例
* feature/login
　develop
  main
```

- 未コミットのファイルが無いかを確認
```
git status
```

- リモートの最新状態を取得
```
git fetch origin
```

- developブランチに移動してdevelopブランチに特定のブランチをマージ
```
git switch develop　
git merge feature/login
```

- マージ中に競合が発生した場合、コンフリクトを解消する
```
git status  # 競合しているファイルを確認
# vscodeを用いて競合を解消
git add <解消したファイル>
# 全てのコンフリクトを解消したら
git commit
```

- リモートにpush
```
git push origin develop
```

- 疎通確認
  - developブランチの動作が問題ないかを確認

- 問題がなければmainブランチを最新に更新
```
git switch main
git pull origin main
```

- developブランチをmainブランチにマージ
```
git merge develop
```

- mainブランチをリモートにプッシュ
```
git push origin main
```

