CREATE SEQUENCE IF NOT EXISTS public.group_members_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999999999
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.groups_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 999999999
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.users
(
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(500) COLLATE pg_catalog."default" NOT NULL,
    enabled boolean NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (username)
    );

CREATE INDEX IF NOT EXISTS users_enabled_idx
    ON public.users USING btree
    (enabled ASC NULLS LAST);

CREATE TABLE IF NOT EXISTS public.authorities
(
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "authorities_username-authority_uq" UNIQUE (username, authority)
    );

CREATE TABLE IF NOT EXISTS public.groups
(
    id bigint NOT NULL DEFAULT nextval('groups_id_seq'::regclass),
    group_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.group_authorities
(
    group_id bigint NOT NULL,
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT group_authorities_pkey PRIMARY KEY (group_id, authority),
    CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id)
    REFERENCES public.groups (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS public.group_authorities
(
    group_id bigint NOT NULL,
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT group_authorities_pkey PRIMARY KEY (group_id, authority),
    CONSTRAINT fk_group_authorities_group FOREIGN KEY (group_id)
    REFERENCES public.groups (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE INDEX IF NOT EXISTS group_authorities_authority_idx
    ON public.group_authorities USING btree
    (authority COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.group_members
(
    id bigint NOT NULL DEFAULT nextval('group_members_id_seq'::regclass),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    group_id bigint NOT NULL,
    CONSTRAINT group_members_pkey PRIMARY KEY (id),
    CONSTRAINT "group_members_username-group_id_uq" UNIQUE (username, group_id),
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id)
    REFERENCES public.groups (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT group_members_username_fkey FOREIGN KEY (username)
    REFERENCES public.users (username) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID
    );

CREATE INDEX IF NOT EXISTS group_members_group_id_idx
    ON public.group_members USING btree
    (group_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS group_members_username_idx
    ON public.group_members USING btree
    (username COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
