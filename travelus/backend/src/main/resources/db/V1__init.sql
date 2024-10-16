create table currency
(
    id            tinyint not null auto_increment,
    currency_code varchar(10),
    currency_name varchar(50),
    primary key (id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;


create table exchange_rate
(
    exchange_min  float(53),
    exchange_rate float(53),
    created       datetime(2),
    currency_code varchar(8) not null,
    primary key (currency_code)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table foreign_account
(
    balance            float(53),
    bank_code          tinyint not null,
    created_at         datetime(2),
    currency_id        tinyint,
    foreign_account_id bigint  not null auto_increment,
    general_account_id bigint,
    updated_at         datetime(2),
    account_name       varchar(255),
    account_no         varchar(255),
    account_password   varchar(255),
    group_name         varchar(255),
    icon_name          varchar(255),
    primary key (foreign_account_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table general_account
(
    balance            float(53),
    bank_code          tinyint  not null,
    country_id         smallint not null,
    created_at         datetime(2),
    general_account_id bigint   not null auto_increment,
    updated_at         datetime(2),
    user_id            bigint,
    account_name       varchar(255),
    account_no         varchar(255),
    account_password   varchar(255),
    group_name         varchar(255),
    travel_end_date    date,
    travel_start_date  date,
    account_type       enum ('GROUP','INDIVIDUAL'),
    icon_name          varchar(255),
    primary key (general_account_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table latest_rate
(
    cash_buying    float(53),
    cash_selling   float(53),
    deal_bas_r     float(53),
    post_at        date,
    tc_buying      float(53),
    ttb            float(53),
    tts            float(53),
    currency_id    tinyint,
    latest_rate_id bigint not null auto_increment,
    primary key (latest_rate_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table participant
(
    is_master           tinyint(1) not null,
    created_at          datetime(2),
    general_account_id  bigint,
    participant_id      bigint     not null auto_increment,
    personal_account_id bigint,
    updated_at          datetime(2),
    user_id             bigint,
    primary key (participant_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;

create table user
(
    birth       date,
    is_exit     tinyint(1),
    register_at datetime(2),
    user_id     bigint not null auto_increment,
    address     varchar(128),
    email       varchar(50),
    name        varchar(50),
    password    varchar(64),
    phone       varchar(50),
    profile     varchar(255),
    user_key    varchar(255),
    role        enum ('ADMIN','MANAGER','USER'),
    primary key (user_id)
) engine = InnoDB
  default charset = utf8mb4
  collate = utf8mb4_general_ci;


alter table foreign_account
    add constraint u_general_account_id unique (general_account_id);

alter table participant
    add constraint u_personal_account_id unique (personal_account_id);

alter table foreign_account
    add constraint fk_foreign_account_currency_id
        foreign key (currency_id)
            references currency (id)
            on delete cascade
            on update cascade;

alter table foreign_account
    add constraint fk_foreign_account_general_account_id
        foreign key (general_account_id)
            references general_account (general_account_id)
            on delete cascade
            on update cascade;

alter table general_account
    add constraint fk_general_account_user_id
        foreign key (user_id)
            references user (user_id)
            on delete cascade
            on update cascade;

alter table latest_rate
    add constraint fk_latest_rate_currency_id
        foreign key (currency_id)
            references currency (id)
            on delete cascade
            on update cascade;

alter table participant
    add constraint fk_participant_general_account_id
        foreign key (general_account_id)
            references general_account (general_account_id)
            on delete cascade
            on update cascade;

alter table participant
    add constraint fk_participant_personal_account_id
        foreign key (personal_account_id)
            references general_account (general_account_id)
            on delete cascade
            on update cascade;

alter table participant
    add constraint fk_participant_user_id
        foreign key (user_id)
            references user (user_id)
            on delete cascade
            on update cascade;
