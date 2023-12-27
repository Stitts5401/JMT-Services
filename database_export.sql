CREATE DATABASE  IF NOT EXISTS `jmt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `jmt`;

DROP TABLE IF EXISTS `job`;
CREATE TABLE job (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     user_id INT,
                     name VARCHAR(255) NOT NULL,
                     description TEXT,
                     location VARCHAR(255),
                     created_date TIME,
                     completion_status BOOLEAN NOT NULL,
                     image LONGBLOB,
                     FOREIGN KEY (user_id) REFERENCES user(id)
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      profile_picture LONGBLOB,
                      first_name VARCHAR(255),
                      last_name VARCHAR(255),
                      phone_number VARCHAR(50),
                      address TEXT,
                      nationality VARCHAR(100),
                      dob DATE,
                      enabled BOOLEAN NOT NULL,
                      joined_date DATE,
                      last_active_date TIMESTAMP
);

CREATE TABLE policies (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          category VARCHAR(255) NOT NULL
);
CREATE TABLE policy_items (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              policy_id INT,
                              section VARCHAR(255),  -- 'contract_agreement', 'cancellation', 'refund', etc.
                              content TEXT,
                              FOREIGN KEY (policy_id) REFERENCES policies(id)
);
INSERT INTO policies (category) VALUES ('Welder');

INSERT INTO policy_items (policy_id, section, content) VALUES
                                                           (1, 'contract_agreement', 'The client will receive a contract agreement via email within 48 hours of successful job request submission.'),
                                                           (1, 'contract_agreement', 'In case the requested start date is unavailable, an alternate schedule according to the client’s preference will be arranged and a new contract agreement will be sent via email.'),
                                                           (1, 'contract_agreement', 'Alternatively, the client may choose to cancel the job request before the contract agreement is signed and no fee will be charged.');

INSERT INTO policy_items (policy_id, section, content) VALUES
                                                           (1, 'cancellation', 'More than 30 days before the start date: No charge'),
                                                           (1, 'cancellation', '15 to 30 days before the start date: 25% of the estimated job cost'),
                                                           (1, 'cancellation', '7 to 14 days before the start date: 50% of the estimated job cost'),
                                                           (1, 'cancellation', 'Less than 7 days before the start date: 75% of the estimated job cost'),
                                                           (1, 'cancellation', 'After job commencement: 100% of the estimated job cost');

INSERT INTO policy_items (policy_id, section, content) VALUES
                                                           (1, 'refund', 'Any applicable refund amount will be processed within 15 business days after the cancellation request is confirmed.'),
                                                           (1, 'refund', 'Refunds will be made via the original payment method or a company check.');


INSERT INTO job (user_id, name, description, location, created_date, completion_status, image) VALUES
                                                                                                   (1, 'Software Developer', 'Develop and maintain web applications.', 'Remote', '09:00:00', FALSE, 'am9iaW1hZ2U='),
                                                                                                   (2, 'Graphic Designer', 'Design graphics for brand campaigns.', 'New York', '10:00:00', TRUE, 'am9iaW1hZ2U='),
                                                                                                   (3, 'Data Analyst', 'Analyze data sets and produce reports.', 'San Francisco', '11:30:00', FALSE, 'am9iaW1hZ2U=');

INSERT INTO user (email, profile_picture, first_name, last_name, phone_number, address, nationality, dob, enabled, joined_date, last_active_date) VALUES
                                                                                                                                                      ('john.doe@example.com', 'cHJvZmlsZXBpY3R1cmU=', 'John', 'Doe', '123-456-7890', '123 Maple Street', 'American', '1980-01-01', TRUE, '2021-01-01', '2023-03-01 12:00:00'),
                                                                                                                                                      ('jane.smith@example.com', 'cHJvZmlsZXBpY3R1cmU=', 'Jane', 'Smith', '234-567-8901', '234 Oak Avenue', 'Canadian', '1985-05-05', TRUE, '2021-05-15', '2023-03-02 13:30:00'),
                                                                                                                                                      ('alice.jones@example.com', 'cHJvZmlsZXBpY3R1cmU=', 'Alice', 'Jones', '345-678-9012', '345 Pine Road', 'British', '1990-10-10', FALSE, '2022-02-20', '2023-03-03 14:45:00');
