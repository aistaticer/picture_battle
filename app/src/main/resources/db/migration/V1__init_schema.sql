CREATE TABLE websockets (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT now()
);