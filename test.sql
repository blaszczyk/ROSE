--other comment
--@ package test
create table TEST (
	TEST_ID int auto_increment,
	TEST_VARCHAR varchar(50),
	TEST_DATE date,
	TEST_NUMERIC numeric(9,2),
	TEST_CHAR char(5),
	constraint pk_test PRIMARY KEY (TEST_ID)
)