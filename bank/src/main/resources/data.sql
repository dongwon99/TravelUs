-- country 데이터 삽입 (명시적으로 컬럼을 지정)
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (1, 'KOR', 'Korea');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (2, 'USA', 'United States');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (3, 'JPN', 'Japan');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (4, 'EUR', 'European Union');
INSERT IGNORE INTO country (country_id, country_code, country_name) VALUES (5, 'TWD', 'Taiwan');

-- currency 데이터 삽입
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('1','KRW', 1, 1.0, 1);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('2','USD', 2, 1200.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('3','JPY', 3, 100.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('4','EUR', 4, 1400.0, 100);
INSERT IGNORE INTO currency (currency_id, currency_code, country_id, exchange_rate, exchange_min) VALUES ('5','TWD', 5, 180.0, 4000);

-- merchant 데이터 삽입
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (1, '롯데월드', 'OTHER', '서울특별시 송파구 올림픽로 240', 37.511022, 127.098619);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (2, 'N서울타워', 'OTHER', '서울특별시 용산구 남산공원길 105', 37.551169, 126.988227);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (3, '스타벅스 강남대로점', 'FOOD', '서울특별시 강남구 강남대로 390', 37.497952, 127.027619);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (4, '신라호텔 서울', 'ACCOMODATION', '서울특별시 중구 동호로 249', 37.556302, 127.010904);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (5, '경복궁', 'OTHER', '서울특별시 종로구 사직로 161', 37.579617, 126.977041);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (6, '스타필드 하남', 'SHOPPING', '경기도 하남시 미사대로 750', 37.546251, 127.222430);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (7, '부산 해운대', 'OTHER', '부산광역시 해운대구', 35.158698, 129.160384);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (8, '한옥마을', 'OTHER', '전라북도 전주시 완산구', 35.815130, 127.152196);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (9, '카페 노티드', 'FOOD', '서울특별시 강남구 신사동 123', 37.518028, 127.023505);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (10, '여의도 한강공원', 'OTHER', '서울특별시 영등포구 여의도동', 37.528102, 126.932670);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (11, 'In-N-Out Burger', 'FOOD', '7009 Sunset Blvd, Los Angeles, CA', 34.098145, -118.340665);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (12, 'Universal City', 'OTHER', '100 Universal City Plaza, Universal City, CA', 34.138116, -118.353378);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (13, 'Hilton Los Angeles/Universal City', 'ACCOMODATION', '555 Universal Hollywood Dr, Universal City, CA', 34.136207, -118.352038);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (14, 'The Getty Center', 'OTHER', '1200 Getty Center Dr, Los Angeles, CA', 34.078036, -118.474095);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (15, 'Santa Monica Pier', 'OTHER', '200 Santa Monica Pier, Santa Monica, CA', 34.010264, -118.495101);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (16, 'Griffith Observatory', 'OTHER', '2800 E Observatory Rd, Los Angeles, CA', 34.118434, -118.300393);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (17, 'Venice Beach', 'OTHER', '1800 Ocean Front Walk, Venice, CA', 33.985046, -118.469483);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (18, 'Hollywood Walk of Fame', 'OTHER', 'Hollywood Blvd, Los Angeles, CA', 34.101558, -118.326927);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (19, 'The Broad', 'OTHER', '221 S Grand Ave, Los Angeles, CA', 34.054431, -118.250547);
INSERT IGNORE INTO merchant (merchant_id, merchant_name, category, address, lat, lng)
VALUES (20, 'Dodger Stadium', 'OTHER', '1000 Vin Scully Ave, Los Angeles, CA', 34.073851, -118.240646);
       
-- user 데이터 삽입
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (1, 'heo_dongwon@naver.com', 'ADMIN', null, null, null, null);
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (2, '2pearl522@gmail.com', 'ADMIN', null, null, null, null);
INSERT IGNORE INTO user (user_id, credential_id, role, is_exit, create_at, update_at, exit_at) VALUES (3, 'lsh@gmail.com', 'ADMIN', null, null, null, null);

-- api_key 데이터 삽입
INSERT IGNORE INTO tunabank.api_key (id, create_at, expire_at, status, type, value, user_id) VALUES (1, '2024-09-18 22:06:16.282217', '2025-09-18 22:06:16.282217', 'ACTIVE', 'API', 'HUo52t-HWD_BYTDPMtnloxnppmSoRrBfduY6XYjnyC2-a84p9xxJwB_RKinE6WvQ', 1);

-- bank 데이터 삽입
INSERT IGNORE INTO bank (bank_id, bank_name) VALUES (1, '튜나은행');

-- card_product 데이터 삽입
INSERT IGNORE INTO card_product(card_product_id,card_name, card_description, card_unique_no) VALUES (1, '튜나 카드', '튜나뱅크의 카드입니다.', 'DC5O2YKQ');
