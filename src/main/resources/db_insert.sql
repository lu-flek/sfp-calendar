INSERT INTO public.users(name, email, role, pass) VALUES
('Mike','mike@calendar.com','ADMIN','adminpass'),
('John','john@calendar.com','USER','pass'),
('Alex','alex@calendar.com','USER','pass'),
('Leo','leo@calendar.com','USER','pass'),
('Rachel','rachel@calendar.com','USER','pass'),
('Anna','anna@calendar.com','USER','pass');

INSERT INTO public.events(name, type, date_start, date_end, is_public) VALUES
('Johns birthday','BIRTHDAY','2021-09-03','2021-09-03',false),
('Rachels vacation','VACATION','2021-09-13','2021-09-19',false),
('Leos vacation','VACATION','2021-09-27','2021-10-03',false),
('New Year Holiday','PUBLIC_HOLIDAY','2022-01-01','2022-01-10',true),
('Annas day off','DAY_OFF','2021-09-22','2021-09-22',false),
('Some public day off','DAY_OFF','2021-11-17','2021-11-17',true);

INSERT INTO public.users_events(user_id, event_id) VALUES
(2,1),
(5,2),
(4,3),
(1,4),
(2,4),
(3,4),
(4,4),
(5,4),
(6,4),
(6,5),
(1,6),
(2,6),
(3,6),
(4,6),
(5,6),
(6,6);
