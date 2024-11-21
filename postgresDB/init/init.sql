create table clothes
(
    created_at       timestamp(6),
    id               bigint generated by default as identity
        primary key,
    updated_at       timestamp(6),
    category         varchar(255)
        constraint clothes_category_check
            check ((category)::text = ANY
        ((ARRAY ['SLEEVELESS'::character varying, 'SHORT_SLEEVE'::character varying, 'LONG_SLEEVE'::character varying, 'SHORT_PANTS'::character varying, 'LONG_PANTS'::character varying, 'SHOES'::character varying, 'OUTWEAR'::character varying, 'ACCESSORY'::character varying])::text[])),
    cloth_image_url  varchar(255),
    product_page_url varchar(255),
    site             varchar(255)
        constraint clothes_site_check
            check ((site)::text = ANY
                   ((ARRAY ['MUSINSA'::character varying, 'ABLY'::character varying, 'ZIGZAG'::character varying, 'ETC'::character varying])::text[]))
);

alter table clothes
    owner to "8fit-dev";

create table users
(
    created_at timestamp(6),
    id         bigint generated by default as identity
        primary key,
    updated_at timestamp(6),
    device_id  varchar(255) not null
        unique
);

alter table users
    owner to "8fit-dev";

create table fitting_models
(
    created_at      timestamp(6),
    id              bigint generated by default as identity
        primary key,
    updated_at      timestamp(6),
    user_id         bigint       not null
        constraint fkc7v1ht4vuv36iqkghffcefijp
            references users,
    model_image_url varchar(255) not null
);

alter table fitting_models
    owner to "8fit-dev";

create table fittings
(
    cloth_id         bigint       not null
        unique
        constraint fk70cmqik6u8nii5f0t2o7a4csh
            references clothes,
    created_at       timestamp(6),
    id               bigint generated by default as identity
        primary key,
    updated_at       timestamp(6),
    user_id          bigint       not null
        constraint fk4h741hu4mf9vwctw3hk3k7nwy
            references users,
    llm_advice       text,
    result_image_url varchar(255) not null
);

alter table fittings
    owner to "8fit-dev";


-- Insert Users with a specific deviceId
insert into users (created_at, updated_at, device_id) values
('2024-11-01 10:00:00', '2024-11-01 10:00:00', 'validDeviceId123');

-- Insert sample Clothes data with specific cloth_image_url values and precise timestamps
insert into clothes (created_at, updated_at, category, cloth_image_url, product_page_url, site) values
('2024-11-06 01:33:53.853651', '2024-11-06 01:33:53.853651', 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
('2024-11-06 01:34:00.123456', '2024-11-06 01:34:00.123456', 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
('2024-11-06 01:34:05.654321', '2024-11-06 01:34:05.654321', 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
('2024-11-06 01:34:10.987654', '2024-11-06 01:34:10.987654', 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
('2024-11-06 01:34:15.333333', '2024-11-06 01:34:15.333333', 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
('2024-11-06 01:34:20.888888', '2024-11-06 01:34:20.888888', 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
('2024-11-06 01:34:25.444444', '2024-11-06 01:34:25.444444', 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
('2024-11-06 01:34:30.999999', '2024-11-06 01:34:30.999999', 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG');

-- Insert sample Fittings data with specific result_image_url values and precise timestamps
insert into fittings (created_at, updated_at, user_id, cloth_id, result_image_url) values
('2024-11-06 01:35:00.111111', '2024-11-06 01:35:00.111111', 1, 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:35:05.222222', '2024-11-06 01:35:05.222222', 1, 2, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
('2024-11-06 01:35:10.333333', '2024-11-06 01:35:10.333333', 1, 3, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:35:15.444444', '2024-11-06 01:35:15.444444', 1, 4, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
('2024-11-06 01:35:20.555555', '2024-11-06 01:35:20.555555', 1, 5, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:35:25.666666', '2024-11-06 01:35:25.666666', 1, 6, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
('2024-11-06 01:35:30.777777', '2024-11-06 01:35:30.777777', 1, 7, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:35:35.888888', '2024-11-06 01:35:35.888888', 1, 8, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png');

-- Insert mock data into fitting_models table
insert into fitting_models (created_at, updated_at, user_id, model_image_url) values
('2024-11-06 01:30:00.000000', '2024-11-06 01:30:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:31:00.000000', '2024-11-06 01:31:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:32:00.000000', '2024-11-06 01:32:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:33:00.000000', '2024-11-06 01:33:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:34:00.000000', '2024-11-06 01:34:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:35:00.000000', '2024-11-06 01:35:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:36:00.000000', '2024-11-06 01:36:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
('2024-11-06 01:37:00.000000', '2024-11-06 01:37:00.000000', 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png');
