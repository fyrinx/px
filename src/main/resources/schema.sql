DROP Table if exists series_genre;
DROP table if exists genre;
DROP table if exists next_week;
DROP table if exists episode;
DROP table if exists series;
DROP table if exists network;
CREATE table network (
    id int primary key,
    name varchar(50)
);
CREATE table series (
    id int primary key,
    series_name varchar(250),
    network_id int,
    summary TEXT,
    rating float,
    episode_count int,
    released_episode_count int,
    ended tinyint(1),
    days varchar(200)
);
CREATE table episode (
    id int primary key,
    series_id int,
    network_id int,
    season_number int,
    episode_number int,
    episode_name varchar(250),
    rating  float
);
CREATE table next_week (
    series_id int primary key,
    days varchar(100)
);
CREATE table genre (
    id int primary key,
    genre varchar(50)
);
create table series_genre (
    series_id int,
    genre_id int,
    primary key (series_id,genre_id)
);