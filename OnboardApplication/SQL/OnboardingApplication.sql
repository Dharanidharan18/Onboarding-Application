create database onboarding_application;
use onboarding_application;
CREATE TABLE users (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_name VARCHAR(100),
    email VARCHAR(100),
    username VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20),
    status VARCHAR(20) DEFAULT 'active',
    asset_id VARCHAR(10)  unique,
    date_asset_assigned DATE 
);
select*from users;
ALTER TABLE users ADD COLUMN health_insurence ENUM('active','inactive') ;

CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    task TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    FOREIGN KEY (employee_id) REFERENCES users(employee_id)
);

select*from tasks;

INSERT INTO users (employee_name, email, username, password, role)
VALUES ('Dumbledore', 'dumbledore@hr.com', 'Dumbledore', 'password0', 'hr');

INSERT INTO users (employee_name, email, username, password, role)
VALUES 
('Severus Snape', 'severusSnape@manager.com', 'Snape', 'password1', 'manager'),
('Minerva McGonagall', 'minervaMcGonagall@manager.com', 'McGonagall', 'password2', 'manager'),
('Remus Lupin', 'remuslupin@manager.com', 'Lupin', 'password3', 'manager');


CREATE TABLE employee_details (
    employee_id INT ,
    employee_name varchar(20)not null,
    phone_num varchar(10) not null,
    aadhar_num VARCHAR(12) NOT NULL,
    aadhar_img BLOB NOT NULL,
    pan_num VARCHAR(10) NOT NULL,
    pan_img BLOB NOT NULL,
    marksheet BLOB NOT NULL,
    resume BLOB NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES users(employee_id)
);


select*from employee_details;


CREATE TABLE project_assignments (
    employee_id INT,
    manager_id INT,
    project_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES users(employee_id),
    FOREIGN KEY (manager_id) REFERENCES users(employee_id)
);
select*from project_assignments;

CREATE TABLE performance (
    employee_id INT NOT NULL,
    technical_skill INT NOT NULL CHECK (technical_skill >= 0 AND technical_skill <= 10),
    communication_skill INT NOT NULL CHECK (communication_skill >= 0 AND communication_skill <= 10),
    total_score INT AS (technical_skill + communication_skill) STORED,
    FOREIGN KEY (employee_id) REFERENCES users(employee_id)
);
ALTER TABLE performance DROP COLUMN total_score;

ALTER TABLE performance ADD COLUMN total_score DECIMAL(5, 2) AS ((technical_skill + communication_skill) / 2.0) STORED;

select*from performance;

CREATE TABLE bench (
    bench_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    certification_type VARCHAR(255),
    completion_of_certification ENUM('yes', 'no') NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES users(employee_id)
);
select*from bench;

DELIMITER $$

CREATE TRIGGER trg_move_to_bench
AFTER INSERT ON performance
FOR EACH ROW
BEGIN
    DECLARE avg_score DECIMAL(5, 2);
    SET avg_score = NEW.total_score;

    IF avg_score <= 4 THEN
        INSERT INTO bench (employee_id, start_date, completion_of_certification)
        VALUES (NEW.employee_id, CURDATE(), 'no');
    END IF;
END $$
DELIMITER ;
SELECT u.employee_id, u.employee_name FROM users u JOIN project_assignments pa ON u.employee_id = pa.employee_id WHERE pa.manager_id