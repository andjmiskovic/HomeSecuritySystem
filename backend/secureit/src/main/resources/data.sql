-- PASSWORD = 'cascaded'
INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE, PHONE_NUMBER, CITY, PASSWORD_SET, LOGIN_ATTEMPTS, LAST_LOGIN_ATTEMPT, LOCKED_UNTIL, LOCK_REASON, TWO_FACTOR_KEY)
VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@secureit.com', TRUE, 'Super', 'Admin',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_ADMIN', '+48123456789', 'Warszawa', true, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', null, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7');

-- PASSWORD = 'cascaded'
INSERT INTO PROPERTY_OWNER(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE, PHONE_NUMBER, CITY, PASSWORD_SET, LOGIN_ATTEMPTS, LAST_LOGIN_ATTEMPT, LOCKED_UNTIL, LOCK_REASON, TWO_FACTOR_KEY)
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', 'john@deste.com', TRUE, 'John', 'Doe',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        '+38123456789', 'Novi Sad', TRUE, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', NULL, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7'),
       (UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', 'john2@deste.com', TRUE, 'John', 'Doeeee',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        '+38127456789', 'Belgrade', TRUE, 0, '-1000000000-01-01T00:00:00Z', TIMESTAMP '-1000000000-01-01T00:00:00Z', NULL, 'F3OPURVECFTYHZXAM62YME7PVESQZXP7')
