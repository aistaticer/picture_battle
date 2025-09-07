-- uuid拡張を有効化（PostgreSQL専用）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS websocket_sessions CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;

-- ユーザー情報（ユーザーが1つのルームに参加）
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT NOT NULL,
  room_id UUID, -- 1人1ルームに参加
  game_group_id TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

