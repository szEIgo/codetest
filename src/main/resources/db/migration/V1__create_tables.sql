CREATE TABLE IF NOT EXISTS  supplier(
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS data_source (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS parameter (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS data_entry (
  id UUID PRIMARY KEY,
  supplier_id UUID,
  data_source_id UUID,
  parameter_id UUID,
  score DECIMAL(18, 2),
  FOREIGN KEY (supplier_id) REFERENCES supplier (id),
  FOREIGN KEY (data_source_id) REFERENCES data_source (id),
  FOREIGN KEY (parameter_id) REFERENCES parameter (id)
);
