-- 初期データ投入
INSERT INTO games (id) VALUES ('23d1a8b5-6c67-4b36-a2db-a076d23e9b88');
INSERT INTO teams (id, name, game_id) VALUES (DEFAULT, 'TeamA', (SELECT id FROM games LIMIT 1));
INSERT INTO teams (id, name, game_id) VALUES ('0d6584d5-be21-439d-840d-dc57dab80393', 'TeamB', (SELECT id FROM games LIMIT 1));
INSERT INTO users (id, name, game_id, team_id)
VALUES 
('88bfbd90-9065-49ef-af81-68db708a4043', '高橋', (SELECT id FROM games LIMIT 1), (SELECT id FROM teams LIMIT 1)),
(uuid_generate_v4(), '田中', (SELECT id FROM games LIMIT 1), (SELECT id FROM teams LIMIT 1));