-- Clothes Table Creation
create table clothes
(
    created_at       timestamp(6),
    id               bigint generated by default as identity primary key,
    updated_at       timestamp(6),
    category         varchar(255) constraint clothes_category_check check ((category)::text = ANY
        ((ARRAY ['SLEEVELESS'::character varying, 'SHORT_SLEEVE'::character varying, 'LONG_SLEEVE'::character varying, 'SHORT_PANTS'::character varying, 'LONG_PANTS'::character varying, 'SHOES'::character varying, 'OUTWEAR'::character varying, 'ACCESSORY'::character varying])::text[])),
    cloth_image_url  varchar(255),
    product_page_url varchar(255),
    site             varchar(255) constraint clothes_site_check check ((site)::text = ANY
                   ((ARRAY ['MUSINSA'::character varying, 'ABLY'::character varying, 'ZIGZAG'::character varying, 'ETC'::character varying])::text[]))
);

-- Users Table Creation
create table users
(
    created_at timestamp(6),
    id         bigint generated by default as identity primary key,
    updated_at timestamp(6),
    device_id  varchar(255) not null unique
);

-- Fitting Models Table Creation
create table fitting_models
(
    created_at      timestamp(6),
    id              bigint generated by default as identity primary key,
    updated_at      timestamp(6),
    user_id         bigint not null constraint fkc7v1ht4vuv36iqkghffcefijp references users,
    model_image_url varchar(255) not null
);

-- Fittings Table Creation
create table fittings
(
    cloth_id         bigint not null unique constraint fk70cmqik6u8nii5f0t2o7a4csh references clothes,
    created_at       timestamp(6),
    id               bigint generated by default as identity primary key,
    updated_at       timestamp(6),
    user_id          bigint not null constraint fk4h741hu4mf9vwctw3hk3k7nwy references users,
    llm_advice       text,
    result_image_url varchar(255) not null
);

-- Insert default data into tables

-- Insert Users with a specific deviceId
insert into users (created_at, updated_at, device_id) values
    (current_timestamp, current_timestamp, 'validDeviceId123');

-- Insert sample Clothes data with specific cloth_image_url values
insert into clothes (created_at, updated_at, category, cloth_image_url, product_page_url, site) values
(current_timestamp, current_timestamp, 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
(current_timestamp, current_timestamp, 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
(current_timestamp, current_timestamp, 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
(current_timestamp, current_timestamp, 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
(current_timestamp, current_timestamp, 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
(current_timestamp, current_timestamp, 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
(current_timestamp, current_timestamp, 'SHORT_SLEEVE', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/eac0b77f-b096-402f-9ed3-74db14d2545d_mockCloth.jpg', null, 'MUSINSA'),
(current_timestamp, current_timestamp, 'LONG_PANTS', 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/dfc301ed-10ad-4e48-bc11-e1d4c2ac7566_mockCloth2.jpg', null, 'ZIGZAG'),
;


-- Insert sample Fittings data with specific result_image_url values
insert into fittings (created_at, updated_at, user_id, cloth_id, result_image_url) values
(current_timestamp, current_timestamp, 1, 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
(current_timestamp, current_timestamp, 1, 2, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
(current_timestamp, current_timestamp, 1, 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
(current_timestamp, current_timestamp, 1, 2, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
(current_timestamp, current_timestamp, 1, 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
(current_timestamp, current_timestamp, 1, 2, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
(current_timestamp, current_timestamp, 1, 1, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/b75bc54e-4cc6-476b-82dc-3aac0f615d32_mockFittingResult.png'),
(current_timestamp, current_timestamp, 1, 2, 'https://8fit-bucket.s3.ap-northeast-2.amazonaws.com/d1527dab-34aa-4389-bb53-848c18ef3223_mockFittingResult2.png'),
;
