CREATE TABLE page_in_database (
    id SERIAL PRIMARY KEY,
    url_ VARCHAR(255),
    name_ VARCHAR(255),
    interval_ INT,
    max_number_of_revisions INT,
    dom_element VARCHAR(255)
);
