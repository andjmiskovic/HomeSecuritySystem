-- PASSWORD = 'cascaded'
INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE)
VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@secureit.com', TRUE, 'Super', 'Admin',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_ADMIN');

-- PASSWORD = 'cascaded'
INSERT INTO PROPERTY_OWNER
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:00.283326+00',
        'john@deste.com', TRUE, 'John', 'Doe', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:24.10518+00',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        'b687c6b59f651f54893c2589dca06ad1fc1f09c7be267a5e23525ccef672bb87'),
       (UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', TIMESTAMP WITH TIME ZONE '2023-03-27 23:31:00.283326+00',
        'john2@deste.com', TRUE, 'John', 'Doeeee', TIMESTAMP WITH TIME ZONE '2023-03-27 23:31:24.10518+00',
        '$2a$10$Qg.gpYTtZiWMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        'b687c6b59f651f54803c2589dca06ad1fc1f09c7be267a5e23525ccef672bb87');
INSERT INTO TENANT
VALUES (UUID '738520b8-cd02-43ee-a21f-778d7d5752a6', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:00.283326+00',
        'tenant@deste.com', TRUE, 'Tenant', 'Tenantic', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:24.10518+00',
        '$2a$11$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        'b688c6b59f651f54893c2589dca06ad1fc1f09c7be267a5e23525ccef672bb87'),
       (UUID 'eb17cdd4-f0e8-4db4-ae4d-07a58340663c', TIMESTAMP WITH TIME ZONE '2023-03-27 23:31:00.283326+00',
        'john2@deste.com', TRUE, 'John', 'Doeeee', TIMESTAMP WITH TIME ZONE '2023-03-27 23:31:24.10518+00',
        '$2a$10$Qg.gpYTtZiWMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        'b687c6b59f651f54803c2589dca06ad1fc1f09c7be267a5e23525ccef672bb87');
