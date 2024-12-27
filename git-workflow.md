### Gitの運用ルール
- 1コミットにつき1つの機能またはバグ修正に集中
- コミットメッセージは「[タイプ]: 説明」の形式を使用 (例: `fix: パスワードが異なってもログインできる問題を修正`)

### mainブランチへの反映の手順

- mainブランチにいないことを確認
```
git branch

# 結果の例
* feature/login
  main
  master
```

- リモートのmainブランチの全データを取得
```
git fetch origin
```

- mainブランチに移動してローカルのmainブランチの情報を最新のものにする
```
git switch main
git pull origin main
```
- 作業中のブランチに切り替えてリベース
```
git switch feature-branch
git rebase main
```

- リベース中に競合が発生した場合、修正してリベースを続けます
```
git status  # 競合しているファイルを確認
# vscodeを用いて競合を解消
git add <解消したファイル>
git rebase --continue
```

- 作業中だったブランチをmainブランチにマージ
```
git checkout main
git merge feature-branch
``` 

- リモートにpush
```
git push origin main
```

