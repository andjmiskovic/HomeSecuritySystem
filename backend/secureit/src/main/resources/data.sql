-- PASSWORD = 'cascaded'
INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE, PHONE_NUMBER, CITY, PASSWORD_SET, LOGIN_ATTEMPTS, LAST_LOGIN_ATTEMPT, LOCKED_UNTIL, LOCK_REASON, TWO_FACTOR_KEY, DELETED)
VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@secureit.com', TRUE, 'Super', 'Admin',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_ADMIN', '+48123456789', 'Warszawa', true, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', null, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7', false);

-- PASSWORD = 'cascaded'
INSERT INTO PROPERTY_OWNER(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE, PHONE_NUMBER, CITY, PASSWORD_SET, LOGIN_ATTEMPTS, LAST_LOGIN_ATTEMPT, LOCKED_UNTIL, LOCK_REASON, TWO_FACTOR_KEY, DELETED)
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', 'john@deste.com', TRUE, 'John', 'Doe',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        '+38123456789', 'Novi Sad', TRUE, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', NULL, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7', false),
       (UUID '138520b8-cd02-43ee-a21f-778d7d5752a6', 'bsep-test@outlook.com', TRUE, 'Peter', 'Hill',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        '+38123456789', 'Novi Sad', TRUE, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', NULL, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7', false),
       (UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', 'john2@deste.com', TRUE, 'John', 'Doeeee',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        '+38127456789', 'Belgrade', TRUE, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', NULL, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7', false);

INSERT INTO PROPERTY(ID, ADDRESS, IMAGE, NAME, TYPE, OWNER_ID, DELETED)
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5952a6', 'Njegoseva 12', '', 'Vila Lovcen', 0, UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', false),
       (UUID '638520b8-cd02-43ee-a21f-778d7d5952a7', 'Bulevar oslobodjenja 12', '', 'Objekat 2', 1, UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', false);

INSERT INTO PROPERTY_TENANTS (PROPERTY_ID, TENANTS_ID)
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5952a7', UUID '638520b8-cd02-43ee-a21f-778d7d5752a6');

-- INSERT INTO PROPERTY_OWNER_OWNED_PROPERTIES(PROPERTY_OWNER_ID, OWNED_PROPERTIES_ID)
-- VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', UUID '638520b8-cd02-43ee-a21f-778d7d5952a6'),
--        (UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', UUID '638520b8-cd02-43ee-a21f-778d7d5952a7');

-- INSERT INTO PROPERTY_OWNER_TENANT_PROPERTIES  (PROPERTY_OWNER_ID, TENANT_PROPERTIES_ID)
-- VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', UUID '638520b8-cd02-43ee-a21f-778d7d5952a7');
