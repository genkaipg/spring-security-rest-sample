INSERT IGNORE INTO `users` (`username`, `password`, `enabled`) VALUES
	('genkaipg', '$2a$10$ZqAW.Liah.eydEvRE09xOOVDFIDsjggwkIba7ysuOES9iAp5oEdVu', 1),
	('guest', '$2a$10$YQpd/GbOCS4IK5FGgTW64OdhzcNvQV8T8xIsAFItzNjSefOpr0PAu', 1);

INSERT IGNORE INTO `authorities` (`username`, `authority`) VALUES
	('genkaipg', 'ROLE_GENKAIPG'),
	('guest', 'ROLE_GUEST');

INSERT IGNORE INTO `groups` (`id`, `group_name`) VALUES
	(1, 'ADMIN'),
	(2, 'USER');

INSERT IGNORE INTO `group_authorities` (`group_id`, `authority`) VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_USER');

INSERT IGNORE INTO `group_members` (`id`, `username`, `group_id`) VALUES
	(1, 'genkaipg', 1),
	(2, 'genkaipg', 2),
	(3, 'guest', 2);


