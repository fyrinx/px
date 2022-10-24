DROP Table if exists series_genre;
DROP table if exists genre;
DROP table if exists nextweek;
DROP table if exists episode;
DROP table if exists series;
DROP table if exists network;
CREATE table network (
    id int primary key,
    network varchar(50)
);
CREATE table series (
    id int primary key,
    series_name varchar(250),
    network_id int,
    rating float,
    episode_count int,
    released_episode_count int,
    foreign key (network_id) references network(id)
);
CREATE table episode (
    id int primary key,
    series_id int,
    network_id int,
    season_number int,
    episode_number int,
    episode_name varchar(250),
    rating  float,
    foreign key (series_id) references series (id),
    foreign key (network_id) references network (id)
);
CREATE table nextweek (
    series_id int primary key,
    monday varchar(50),
    tuesday varchar(50),
    wednesday varchar(50),
    thursday varchar(50),
    friday varchar(50),
    saturday varchar(50),
    sunday varchar(50),
    foreign key (series_id) references series(id)
);
CREATE table genre (
    id int primary key,
    genre varchar(50)
);
create table series_genre (
    series_id int,
    genre_id int,
    primary key (series_id,genre_id),
    foreign key (series_id) references series(id),
    foreign key (genre_id) references genre(id)
);