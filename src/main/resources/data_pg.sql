INSERT INTO "users" ("username", "password", "enabled") VALUES
    ('genkaipg', '$2a$10$ZqAW.Liah.eydEvRE09xOOVDFIDsjggwkIba7ysuOES9iAp5oEdVu', true),
    ('guest', '$2a$10$YQpd/GbOCS4IK5FGgTW64OdhzcNvQV8T8xIsAFItzNjSefOpr0PAu', true)
    ON CONFLICT ("username") DO NOTHING;

INSERT INTO "authorities" ("username", "authority") VALUES
    ('genkaipg', 'ROLE_GENKAIPG'),
    ('guest', 'ROLE_GUEST')
    ON CONFLICT ("username", "authority") DO NOTHING;

INSERT INTO "groups" ("id", "group_name") VALUES
    (1, 'ADMIN'),
    (2, 'USER')
    ON CONFLICT ("id") DO NOTHING;

INSERT INTO "group_authorities" ("group_id", "authority") VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER')
    ON CONFLICT ("group_id", "authority") DO NOTHING;

INSERT INTO "group_members" ("id", "username", "group_id") VALUES
    (1, 'genkaipg', 1),
    (2, 'genkaipg', 2),
    (3, 'guest', 2)
    ON CONFLICT ("id") DO NOTHING;