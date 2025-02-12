
CREATE TABLE bank_accounts (
  id BIGINT NOT NULL,
   balance BIGINT DEFAULT 0 NULL,
   type VARCHAR(50) NOT NULL,
   date_of_creation date DEFAULT curdate() NULL,
   currency_id BIGINT NULL,
   account_status BIT(1) DEFAULT 1 NULL,
   last_operation_date timestamp NULL,
   CONSTRAINT PK_BANK_ACCOUNTS PRIMARY KEY (id)
);

CREATE TABLE bank_accounts_cards (
  id BIGINT NOT NULL,
   card_id BIGINT NULL,
   CONSTRAINT PK_BANK_ACCOUNTS_CARDS PRIMARY KEY (id)
);

CREATE TABLE bank_accounts_credentials (
  bank_account_id BIGINT NOT NULL,
   credentials_id BIGINT NOT NULL,
   CONSTRAINT PK_BANK_ACCOUNTS_CREDENTIALS PRIMARY KEY (bank_account_id, credentials_id)
);

CREATE TABLE cards (
  id BIGINT NOT NULL,
   balance BIGINT DEFAULT 0 NULL,
   package_id BIGINT NULL,
   year_of_exp date NOT NULL,
   month_of_exp date NOT NULL,
   card_number BIGINT NOT NULL,
   card_status VARCHAR(20) DEFAULT 'active' NULL,
   holder_name VARCHAR(150) NOT NULL,
   is_virtual BIT(1) DEFAULT 0 NULL,
   CONSTRAINT PK_CARDS PRIMARY KEY (id),
   UNIQUE (card_number)
);

CREATE TABLE credentials (
  id BIGINT NOT NULL,
   user_id BIGINT NULL,
   email VARCHAR(255) NOT NULL,
   `role` VARCHAR(50) NULL,
   CONSTRAINT PK_CREDENTIALS PRIMARY KEY (id)
);

CREATE TABLE currencies (
  id BIGINT NOT NULL,
   cur_abbreviation VARCHAR(10) NOT NULL,
   cur_scale BIGINT NOT NULL,
   cur_rate DECIMAL(10, 4) NOT NULL,
   CONSTRAINT PK_CURRENCIES PRIMARY KEY (id)
);

CREATE TABLE login_details (
  id BIGINT NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   last_login_date date NULL,
   login_attempts BIGINT DEFAULT 0 NULL,
   user_status BIGINT NULL,
   password_hash_salt BIGINT NULL,
   two_factor_auth_enabled BIT(1) DEFAULT 0 NULL,
   CONSTRAINT PK_LOGIN_DETAILS PRIMARY KEY (id),
   UNIQUE (email)
);

CREATE TABLE operations (
  id BIGINT NOT NULL,
   currency_id BIGINT NULL,
   card_id BIGINT NULL,
   recipient_details BIGINT NULL,
   value BIGINT NOT NULL,
   date_time timestamp DEFAULT NOW() NULL,
   operation_type VARCHAR(50) NOT NULL,
   status VARCHAR(20) DEFAULT 'pending' NULL,
   `description` VARCHAR(255) NULL,
   reference_number BIGINT NOT NULL,
   CONSTRAINT PK_OPERATIONS PRIMARY KEY (id),
   UNIQUE (reference_number)
);

CREATE TABLE packages (
  id BIGINT NOT NULL,
   name VARCHAR(100) NOT NULL,
   cashback DECIMAL(5, 2) DEFAULT 0 NULL,
   CONSTRAINT PK_PACKAGES PRIMARY KEY (id)
);

CREATE TABLE roles (
  id BIGINT NOT NULL,
   `role` VARCHAR(50) NOT NULL,
   CONSTRAINT PK_ROLES PRIMARY KEY (id)
);

CREATE TABLE users (
  id BIGINT NOT NULL,
   name VARCHAR(100) NOT NULL,
   surname VARCHAR(100) NOT NULL,
   date_of_birth date NOT NULL,
   contact_phone VARCHAR(20) NULL,
   address VARCHAR(255) NULL,
   created_at timestamp DEFAULT NOW() NULL,
   updated_at timestamp DEFAULT NOW() NULL,
   is_active BIT(1) DEFAULT 1 NULL,
   verification_status BIT(1) DEFAULT 0 NULL,
   CONSTRAINT PK_USERS PRIMARY KEY (id)
);

ALTER TABLE bank_accounts_cards ADD CONSTRAINT id UNIQUE (id, card_id);

ALTER TABLE credentials ADD CONSTRAINT user_id UNIQUE (user_id, email);

CREATE INDEX card_id ON bank_accounts_cards(card_id);

CREATE INDEX credentials_ibfk_2 ON roles(`role`);

CREATE INDEX credentials_id ON bank_accounts_credentials(credentials_id);

CREATE INDEX email ON credentials(email);

CREATE INDEX idx_bank_accounts_currency ON bank_accounts(currency_id);

CREATE INDEX idx_cards_package ON cards(package_id);

CREATE INDEX idx_credentials_user ON credentials(user_id);

CREATE INDEX idx_operations_card ON operations(card_id);

CREATE INDEX idx_operations_currency ON operations(currency_id);

CREATE INDEX idx_operations_date ON operations(date_time);

CREATE INDEX `role` ON credentials(`role`);

ALTER TABLE bank_accounts_cards ADD CONSTRAINT bank_accounts_cards_ibfk_1 FOREIGN KEY (card_id) REFERENCES cards (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE bank_accounts_cards ADD CONSTRAINT bank_accounts_cards_ibfk_2 FOREIGN KEY (id) REFERENCES bank_accounts (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE bank_accounts_credentials ADD CONSTRAINT bank_accounts_credentials_ibfk_1 FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE bank_accounts_credentials ADD CONSTRAINT bank_accounts_credentials_ibfk_2 FOREIGN KEY (credentials_id) REFERENCES credentials (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE bank_accounts ADD CONSTRAINT bank_accounts_ibfk_1 FOREIGN KEY (currency_id) REFERENCES currencies (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE cards ADD CONSTRAINT cards_ibfk_1 FOREIGN KEY (package_id) REFERENCES packages (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE credentials ADD CONSTRAINT credentials_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE credentials ADD CONSTRAINT credentials_ibfk_2 FOREIGN KEY (`role`) REFERENCES roles (`role`) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE credentials ADD CONSTRAINT credentials_ibfk_3 FOREIGN KEY (email) REFERENCES login_details (email) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE operations ADD CONSTRAINT operations_ibfk_1 FOREIGN KEY (currency_id) REFERENCES currencies (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE operations ADD CONSTRAINT operations_ibfk_2 FOREIGN KEY (card_id) REFERENCES cards (id) ON UPDATE RESTRICT ON DELETE RESTRICT;