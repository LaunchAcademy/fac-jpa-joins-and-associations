CREATE TABLE categories (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX categories_name_index ON categories(name);
