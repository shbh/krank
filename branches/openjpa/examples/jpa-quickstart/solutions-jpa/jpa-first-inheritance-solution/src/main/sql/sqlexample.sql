create table ImprovedContactInfo (
	id bigint generated by default as identity (start with 1), 
	line1 varchar(255), line2 varchar(255), zip varchar(255), state varchar(255), 
	work_Address_Line1 varchar(255), work_Address_Line2 varchar(255), 
	work_Address_Zip varchar(255), work_Address_State varchar(255), 
	phone varchar(255), firstName varchar(255), lastName varchar(255), primary key (id)
)

-- Maps to Group
create table ImprovedGroup (
    ID bigint generated by default as identity (start with 1), 
    NAME varchar(255), 
    primary key (id)
);

-- Maps to User
create table ImprovedUser (
    ID bigint generated by default as identity (start with 1), 
    NAME varchar(255), 
    FK_GROUP_ID bigint, 
    contactInfo_id bigint, 
    primary key (id)
)


--maps to Group
create table UserGroup (
     id bigint generated by default as identity (start with 1), 
	 name varchar(255), 
	 primary key (id)
);

--maps to Role
create table Role (
    id bigint generated by default as identity (start with 1), 
    name varchar(255), 
    primary key (id)
);

--Join table Group->Roles
create table UserGroup_Role (
	UserGroup_id bigint not null, 
	roles_id bigint not null
);

--maps to ContactInfo and Address
create table ContactInfo (
       id bigint generated by default as identity (start with 1), 
       phone varchar(255), 
       firstName varchar(255),  lastName varchar(255), 
       line1 varchar(255),  	line2 varchar(255), 
       zip varchar(255), 		state varchar(255), 
       primary key (id)
);


--maps to User
create table SimpleModelUser (
     id bigint generated by default as identity (start with 1), 
	 name varchar(255), contactInfo_id bigint, 
	 primary key (id)
);


--make reference from User to ContactInfo 
alter table SimpleModelUser add constraint FK_USER_CONTACT 
        foreign key (contactInfo_id) references ContactInfo



--join table
create table UserGroup_SimpleModelUser (
       UserGroup_id bigint not null, 
       users_id bigint not null, 
       unique (users_id)
)