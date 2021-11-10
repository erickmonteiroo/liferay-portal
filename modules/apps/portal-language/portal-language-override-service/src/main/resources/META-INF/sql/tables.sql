create table PLOEntry (
	mvccVersion LONG default 0 not null,
	ploEntryId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	key_ VARCHAR(255) null,
	languageId VARCHAR(75) null,
	value TEXT null
);