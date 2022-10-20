CREATE table episode (
    id int primary key,
    name varchar(250),
    type varchar(50),
    medium_image text,
    original_image text,
    summary text,
    link text,
    season int,
    number int,
    runtime int,
    average_rating tinyint


);