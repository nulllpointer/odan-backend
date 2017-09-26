-- Table: product
-- DROP TABLE product;

CREATE TABLE product
(
  id integer NOT NULL,
  name character varying(255),
  status smallint,
  created_at timestamp without time zone,
  updated_at timestamp without time zone,
  deleted_at timestamp without time zone,
  CONSTRAINT product_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product
  OWNER TO postgres;
  

-- Table: product_category
-- DROP TABLE product_category;

CREATE TABLE product_category
(
  id integer NOT NULL,
  name character varying(255),
  created_at timestamp without time zone,
  updated_at timestamp without time zone,
  deleted_at timestamp without time zone,
  CONSTRAINT product_category_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_category
  OWNER TO postgres;
  
  
-- Table: product_category_link
-- DROP TABLE product_category_link;

CREATE TABLE product_category_link
(
  id integer NOT NULL,
  product_id integer,
  product_category_id integer,
  CONSTRAINT product_category_link_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_category_link
  OWNER TO postgres;

  
-- Table: product_feature
-- DROP TABLE product_feature;

CREATE TABLE product_feature
(
  id integer NOT NULL,
  name character varying(255),
  code character varying(255),
  description character varying(255),
  status smallint,
  created_at timestamp without time zone,
  updated_at timestamp without time zone,
  deleted_at timestamp without time zone,
  CONSTRAINT product_feature_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_feature
  OWNER TO postgres;
  
-- Table: product_feature_link
-- DROP TABLE product_feature_link;

CREATE TABLE product_feature_link
(
  id integer NOT NULL,
  product_id integer,
  product_feature_id integer,
  CONSTRAINT product_feature_link_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_feature_link
  OWNER TO postgres;

  
-- Table: rate_plan
-- DROP TABLE rate_plan;

CREATE TABLE rate_plan
(
  id integer NOT NULL,
  name character varying(255),
  description character varying(255),
  uom character varying(255),
  amount double precision,
  type smallint,
  billing_period character varying(255),
  created_at timestamp without time zone,
  updated_at timestamp without time zone,
  deleted_at timestamp without time zone,
  CONSTRAINT rate_plan_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE rate_plan
  OWNER TO postgres;

  
-- Table: sale
-- DROP TABLE sale;

CREATE TABLE sale
(
  id integer NOT NULL,
  product_id integer,
  rate_plan_id integer,
  status smallint,
  created_at timestamp without time zone,
  updated_at timestamp without time zone,
  deleted_at timestamp without time zone,
  CONSTRAINT sale_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sale
  OWNER TO postgres;
