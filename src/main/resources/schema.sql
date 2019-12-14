
-- I'm sorry I had to do this barbaric thing. R2DBC does not support query derivation as of Dec 2019. 😿

CREATE TABLE FETCHLIN_PAGE (
    id SERIAL PRIMARY KEY,
    url_ VARCHAR(255),
    name_ VARCHAR(255),
    interval_ INT,
    max_number_of_revisions INT,
    dom_element VARCHAR(255),
    last_fetch_time VARCHAR(50)
);

CREATE TABLE FETCHLIN_REVISION (
    id SERIAL PRIMARY KEY,
    data_ VARCHAR(2147483647),
    fetch_time VARCHAR(50),
    page_id INT
);
