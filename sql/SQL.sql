    CREATE TABLE member(
                           id int NOT NULL auto_increment primary key,
                           user_id varchar(20) not null,
                           user_password varchar(100) not null,
                           user_nickname varchar(20) not null,
                           user_tell varchar(20) not null,
                           user_name varchar(10) not null,
                           admin_right boolean not null default 0
    );

CREATE TABLE api(
    domain varchar(30) not null,
    api_key varchar(100) not null
);

CREATE TABLE member_roles(
     member_id varchar(20),
     roles varchar(10)
);

CREATE TABLE refresh_token(
    refresh_token_id int NOT NULL auto_increment primary key,
    refresh_token varchar(250) not null,
    user_id varchar(25) not null
);