-- Create the database
CREATE DATABASE todo_list;

-- Create 'todo_user' user
CREATE USER 'todo_user'@'%' IDENTIFIED BY 'user@123';
-- Grant all privileges on the database to the user 'todo_user'
GRANT ALL PRIVILEGES ON todo_list.* TO 'todo_user'@'%';

-- Apply the changes
FLUSH PRIVILEGES;

-- Select the database to use
USE todo_list;

-- Create the tasks table
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE
);
