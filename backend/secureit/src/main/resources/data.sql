-- PASSWORD = 'cascaded'
INSERT INTO ADMIN(ID, EMAIL, EMAIL_VERIFIED, FIRST_NAME, LAST_NAME, PASSWORD, ROLE)
VALUES ('e3661c31-d1a4-47ab-94b6-1c6500dccf24', 'admin@secureit.com', TRUE, 'Super', 'Admin',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_ADMIN');

-- PASSWORD = 'cascaded'
INSERT INTO PROPERTY_OWNER
VALUES (UUID '638520b8-cd02-43ee-a21f-778d7d5752a6', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:00.283326+00',
        'john@deste.com', TRUE, 'John', 'Doe', TIMESTAMP WITH TIME ZONE '2023-03-29 23:31:24.10518+00',
        '$2a$10$Qg.gpYTtZiVMJ6Fs9QbQA.BtCx4106oSj92X.A/Gv7iAEKQXAg.gy', 'ROLE_PROPERTY_OWNER',
        'b687c6b59f651f54893c2589dca06ad1fc1f09c7be267a5e23525ccef672bb87');

INSERT INTO CSR_DETAILS
    (ID, CSR_PEM, PRIVATE_KEY_PEM, PUBLIC_KEY_PEM, ALIAS, COMMON_NAME, ORGANIZATION, CITY, STATE,
                        COUNTRY, ALGORITHM, KEY_SIZE, STATUS, REJECTION_REASON, PROCESSED, CREATED, MODIFIED)
VALUES (UUID '24829239-f310-4bd9-9df1-e940760a9808', '-----BEGIN CERTIFICATE REQUEST-----
MIIBnjCCAQcCAQAwXjEXMBUGA1UEAwwOam9obkBkZXN0ZS5jb20xETAPBgNVBAoM
CG9yZyBuYW1lMREwDwYDVQQHDAhOb3ZpIFNhZDEQMA4GA1UECAwHcGVuZGluZzEL
MAkGA1UEBhMCUlMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMbVoWkmnNtO
90xtyl63H4hfIrqQWcok3NHsOk55ELQQJKZqGO+vDELYGKTAHenuRtxlLvixbqyr
MJeQBI++jcA13qemYQnEM1IJjnq/y/Y1jn2YuvCvLC7kaUWhriw6tUvw5nJK0T9X
US4HmQbI9JCXWKX4dRQJFpFwqvdnCFuxAgMBAAGgADANBgkqhkiG9w0BAQsFAAOB
gQA8KI2eccYTC/K3GCVZhhPByCzsZE0A88AxbuMFE+YoihZG45xSM03VOKh9bCzP
awAt0Sb2YregZVuYG2cZcLCHj8/NHkFUlPEoBM6+Bdkj6JMAUv+Ys1ikijvTe+y/
VdLxOrmvl+StwqEknk9xvTq+K3QMvXHXyhcLsa47jO1/0w==
-----END CERTIFICATE REQUEST-----
', '-----BEGIN RSA PRIVATE KEY-----
MIICXQIBAAKBgQDG1aFpJpzbTvdMbcpetx+IXyK6kFnKJNzR7DpOeRC0ECSmahjv
rwxC2BikwB3p7kbcZS74sW6sqzCXkASPvo3ANd6npmEJxDNSCY56v8v2NY59mLrw
rywu5GlFoa4sOrVL8OZyStE/V1EuB5kGyPSQl1il+HUUCRaRcKr3ZwhbsQIDAQAB
AoGAAR4o91TOLZGz5FYq7MXD563svZ6jgZZb1zKIOKIXuuJrGVQn/ht2Yr8RIuZ5
WJQESheHyEJDmnUHK7f1nZsDNBySoGm3UFVcZUXjuYLdNZwS8PDZNg82tzSlHHvc
RC60QZ34K900dxS1RYrgPU2/bpAYmEn5oaV4p60sliPQ5IECQQDTX/GKZHwOho4t
06G5XdA3qOdplelsmKfB1QHlwTRXm9B3IMWRwHuJOd1HNY5d4Xhfs0RgFt3AnTIN
4zVw8mkxAkEA8M/t2SAsI48OF+u6bz05jdIfCY/VwzBEZflnD0LhPSpehudb8s+v
1rSE+mJj4uYP/YS3qVwWKbE/MfWzssB6gQJBAM/X1bBZYkr+MCwLn0igYK96XCJM
kHyCFuLXysfalNdYlUGubdcFx/OFfHoaxWS24iBoK+G4WlCdwaw6MEBhfOECQQCs
+2AKmS67uFZSXpXEtIE0W9zJvJW8KV7otZgPgWZUzquqUmVdbU0NeQ0Q4z6HTeXs
obpgoPKWrmkL1UhpPhSBAkB1Bm5WOfk6+12vHi9hHI6XyEC4vIHbMyUU8XKxpbHK
UcMDz4yU+GHv3Qud5Jyx068mZ0DZEK93W/Y+2FnY+uZP
-----END RSA PRIVATE KEY-----
', '-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDG1aFpJpzbTvdMbcpetx+IXyK6
kFnKJNzR7DpOeRC0ECSmahjvrwxC2BikwB3p7kbcZS74sW6sqzCXkASPvo3ANd6n
pmEJxDNSCY56v8v2NY59mLrwrywu5GlFoa4sOrVL8OZyStE/V1EuB5kGyPSQl1il
+HUUCRaRcKr3ZwhbsQIDAQAB
-----END PUBLIC KEY-----
', 'john@deste.com', 'john@deste.com', 'org name', 'Novi Sad', 'Serbia', 'RS', 'RSA', 1024, 'PENDING', null, null, null, null),
('9702d8ac-2e8c-48a7-9c23-7260af6c9966', '-----BEGIN CERTIFICATE REQUEST-----
MIIBoTCCAQoCAQAwYTEXMBUGA1UEAwwOam9obkBkZXN0ZS5jb20xEzARBgNVBAoM
Cm9yZyBuYW1lIDIxETAPBgNVBAcMCE5vdmkgU2FkMREwDwYDVQQIDAhyZWplY3Rl
ZDELMAkGA1UEBhMCUlMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMQuN7LA
ZqXpcKbpnjJ1dr3eQpMEs8l9gXmna/nkmKRrLq0sxeJHknxhudFGE1TQwx58xNKw
LeOv72gZvYRNHuOkdybmZZKPPiApOX4guDos7ym5uFZ342Y6WQ5JEFpm9UPxEwyh
LAR3FkDe0IRqQJzHFEUgXH1cQpQzn9bz73spAgMBAAGgADANBgkqhkiG9w0BAQsF
AAOBgQCaAaoDj2qnlWcXWliSiZsIX1kKE7kv/LFLJY5XQp6fY8kjLWvIzAihr82s
Kq0IfOfJhk4codFl9DFbVa3WZKbvqpB3eC4Ud0mE1P/oxoTNfV890r7DqqtAczYf
wdKYz9OGwXm2NrNMBIrnXt5NTa5WG2NUIn55oyneMW7ccjQB+Q==
-----END CERTIFICATE REQUEST-----
', '-----BEGIN RSA PRIVATE KEY-----
MIICXAIBAAKBgQDELjeywGal6XCm6Z4ydXa93kKTBLPJfYF5p2v55Jikay6tLMXi
R5J8YbnRRhNU0MMefMTSsC3jr+9oGb2ETR7jpHcm5mWSjz4gKTl+ILg6LO8pubhW
d+NmOlkOSRBaZvVD8RMMoSwEdxZA3tCEakCcxxRFIFx9XEKUM5/W8+97KQIDAQAB
AoGAAxWw9YQV+RzjExTQUn3LWK/yfwpPY2cwTn/l8D96cPdoe/ucvvKeKIuG1vYR
nr9LmiKfG6r+hbqB+rKQ6tAloKqsgyjPYB4b0NbdOEkwBIJ8LRuHaAujba51d0UK
JZtE7ExMzxGRvKHsTrdKAQsjmh1FuPvOP9Tg9kdc9XP+MvsCQQDjCwEfUWC/eH3O
VqRsUy7oUdQoOsNct9XYsM41J+fIYbMCB20hPr6Ss4ZFdpceWxahhL886Ez642dO
YfP2u+bjAkEA3TOMa6aIo7GGXNvbd3SlSihtnUmC8o859U7iq1e0eO7YTMxulzPm
NaKKAiGTlIYw2DekUDSO/2uOsYK+2wFngwJAXjBOguOLs2MVLeibxaSsrQxla1sV
eP165a5TnZG0glilno3eJBCXltyLM34DK0C2nsMnYuksJlj4nnfccdt56QJATR3r
lpw4htkdPsJngP3pojD2Bh/axzWnvx4BaDrrcD3UqvGanJw7ZfeEpbmO+fjOjpLX
dh7cu6+Dz98FKtUSgQJBAInRSX4OA8dLjEortxzlxMuPvBvAK17s8FlTx3Q1DSmN
n7RRfj5q6PrsxUrK1bIgQVtPQsh+kSRpMZvPhWGGq0g=
-----END RSA PRIVATE KEY-----
', '-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDELjeywGal6XCm6Z4ydXa93kKT
BLPJfYF5p2v55Jikay6tLMXiR5J8YbnRRhNU0MMefMTSsC3jr+9oGb2ETR7jpHcm
5mWSjz4gKTl+ILg6LO8pubhWd+NmOlkOSRBaZvVD8RMMoSwEdxZA3tCEakCcxxRF
IFx9XEKUM5/W8+97KQIDAQAB
-----END PUBLIC KEY-----
', 'john@deste.com', 'john@deste.com', 'org name 2', 'Belgrade', 'Serbia', 'RS', 'RSA', 1024, 'REJECTED', null, null, null, null),
       ('c8bf64da-8ce3-4619-87cf-ee3e745cce87', '-----BEGIN CERTIFICATE REQUEST-----
MIIBoTCCAQoCAQAwYTEXMBUGA1UEAwwOam9obkBkZXN0ZS5jb20xEzARBgNVBAoM
Cm9yZyBuYW1lIDIxETAPBgNVBAcMCE5vdmkgU2FkMREwDwYDVQQIDAhyZWplY3Rl
ZDELMAkGA1UEBhMCUlMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAIEKNPsQ
q4TU6l9hkHvLWIC4EnxSmh5lhaDezn4gC7qiZNtVHM11ghe8ktkQyNFqicVkzT2z
1QFm5EFyE7lqCHAgon/fS1EEetVgRoJ9r8W9gheozzaL4y9h3KXIKAGb6DPJW8uK
0X3moLCQWEF3nwu8OH/eRMwNmE7gBmxODf/3AgMBAAGgADANBgkqhkiG9w0BAQsF
AAOBgQA58RW6QhxaLmkttVEhk7r/3U/URc/vMAGswHB4eZCnSdAmY4WJ/tzIcDzm
Y7hadTm72Cv5UkzHinObo2m1jK4BWnkRWHXy6kAUCCKq9rvCDVQZmdZE9xAipYtN
UkIUMNo9KvPk6TCn5GTVCB6cMm07bWFv2NWOGplil1s7X4NhzQ==
-----END CERTIFICATE REQUEST-----
', '-----BEGIN RSA PRIVATE KEY-----
MIICXAIBAAKBgQCBCjT7EKuE1OpfYZB7y1iAuBJ8UpoeZYWg3s5+IAu6omTbVRzN
dYIXvJLZEMjRaonFZM09s9UBZuRBchO5aghwIKJ/30tRBHrVYEaCfa/FvYIXqM82
i+MvYdylyCgBm+gzyVvLitF95qCwkFhBd58LvDh/3kTMDZhO4AZsTg3/9wIDAQAB
AoGAB4Ax4zQtXCvKMeAod2GzE8xfWrWser7Y/ShmZWN0exVMeolggrrlZH7UJGT6
311rg7tfyMnmxE2YUAuTBPCaa+EAVqXxhtfr2Wg7vdLCuOUTJh3+zhujf0VUCSzN
/wq68PpCR6om5nSyRkd8Mw5xz6VBOLmVobWntxPeruQzoG0CQQC3BFYEuFMNU9pt
bE4Xb/zKfMMkTeWoxz/jUSSSmAsoS7rSrS1UsfJyPqsqRl6khEFJFFrorCZpmCh3
9nueEgx1AkEAtH+CkrELgoNTPpPGW8vjK81fdim/M05GVDi/R4+AqusVQiGAlKLh
dvRinzEQcaxGD33s7V+1YT31cf2LFpB9OwJAEuze3EQXSoSGTNRrSt8ou+Ok5kcr
3YUlJSOJ7aLNXJBgNFL2IhD4qjmyx39ZYBfRDHaPZAoROtAiNy2E4Paq1QJASnrv
Q17BFBrip8jMqiZo2DNHt/ekCSS8ipZb/8hsXJriy8YNS8gmZzKS24OgeHeQ9WWN
9alvq++oL2BjG22F7wJBAKal7uixYL03S4/FkUbuHdvjfRr3eKetHoSZ/IJyb1g0
8qr4C56weo9MkJ4SI+UOwBVqj3IZnsp9JzOieuU3SD8=
-----END RSA PRIVATE KEY-----
', '-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBCjT7EKuE1OpfYZB7y1iAuBJ8
UpoeZYWg3s5+IAu6omTbVRzNdYIXvJLZEMjRaonFZM09s9UBZuRBchO5aghwIKJ/
30tRBHrVYEaCfa/FvYIXqM82i+MvYdylyCgBm+gzyVvLitF95qCwkFhBd58LvDh/
3kTMDZhO4AZsTg3/9wIDAQAB
-----END PUBLIC KEY-----
', 'john@deste.com', 'john@deste.com', 'org name 3', 'Belgrade', 'Serbia', 'RS', 'RSA', 1024, 'APPROVED', null, null, null, null);
