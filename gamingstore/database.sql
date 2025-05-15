USE sql7779083;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    registration_date DATETIME,
    is_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(100),
    profile_image MEDIUMBLOB,
    last_login DATETIME
);


CREATE TABLE IF NOT EXISTS games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    category VARCHAR(50),
    rating DOUBLE DEFAULT 0.0,
    system_requirements TEXT
);

CREATE TABLE IF NOT EXISTS purchases (
    purchase_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    game_id INT NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    price DOUBLE NOT NULL,
    payment_method VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (game_id) REFERENCES games(game_id)
);

