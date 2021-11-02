CREATE TYPE public.event AS ENUM
    ('VACATION', 'PUBLIC_HOLIDAY', 'DAY_OFF', 'BIRTHDAY', 'OTHER');

CREATE TYPE public.role AS ENUM
    ('ADMIN', 'USER');

CREATE TABLE IF NOT EXISTS public.events
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    type event NOT NULL,
    date_start date NOT NULL,
    date_end date NOT NULL,
    is_public boolean NOT NULL,
    CONSTRAINT events_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.users
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    role role NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.users_events
(
    user_id integer NOT NULL,
    event_id integer NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event_id)
        REFERENCES public.events (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
