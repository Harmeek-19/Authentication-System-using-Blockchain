-- This script will run on startup to initialize the database

-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  salt VARCHAR(255) NOT NULL,
  public_key VARCHAR(1000) NOT NULL
);

-- You can add initial data here if needed
-- INSERT INTO users (username, password_hash, salt, public_key) VALUES ('admin', 'hashedpassword', 'salt', 'publickey');