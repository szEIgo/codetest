CREATE TABLE IF NOT EXISTS  supplier(
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS data_source (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS parameter (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS data_entry (
  id VARCHAR(255) PRIMARY KEY,
  supplier_id VARCHAR(255),
  data_source_id VARCHAR(255),
  parameter_id VARCHAR(255),
  score DECIMAL(10, 2),
  FOREIGN KEY (supplier_id) REFERENCES supplier (id),
  FOREIGN KEY (data_source_id) REFERENCES data_source (id),
  FOREIGN KEY (parameter_id) REFERENCES parameter (id)
);
