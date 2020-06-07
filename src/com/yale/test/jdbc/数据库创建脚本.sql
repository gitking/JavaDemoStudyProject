DROP TABLE member PURUGE;
DROP SEQUENCE myseq;
CREATE SEQUENCE myseq;
CREATE TABLE member (
	mid NUMBER,
	name varchar2(50) not null,
	age NUMBER,
	birthday DATE,
	note CLOB,
	constraint pk_mid PRIMARY KEY(mid)
);