# J.P Morgan Java Technical Test

This is my solution for JP Morgan Test. This program process some messages send by a customer in XML format.

# How to run the solution

It is necessary Java 1.8 version to run the application.

Execute the command: mvn clean install

After the clean and build process, it is necessary to run a jar file on path ProcessFile\target\ProcessFile-1.0-SNAPSHOT.jar

Execute the command: java -jar ProcessFile-1.0-SNAPSHOT.jar

The program will request some information: the path where the files will be placed and the path to where the files will be moved.

# XML Files Format

There are some examples of files on folder examples.

# Messagege Type 1 - Format

"<?xml version="1.0"?>
<Purchases>
	<Purchase>
		<productType>Apple</productType>
		<value>1</value>
	</Purchase>
	<Purchase>
		<productType>Samsung</productType>
		<value>1</value>
	</Purchase>
</Purchases>"

# Messagege Type 2 - Format

"<?xml version="1.0"?>
<Purchases>
	<Purchase>
		<productType>Apple</productType>
		<value>2</value>
		<occurrenses>5</occurrenses>
	</Purchase>
	<Purchase>
		<productType>Samsung</productType>
		<value>2</value>
		<occurrenses>5</occurrenses>
	</Purchase>
</Purchases>"

# Messagege Type 3 - Format

"<?xml version="1.0"?>
<Purchases>
	<Purchase>
		<productType>Apple</productType>
		<value>1</value>
		<action>add</action>
	</Purchase>
	<Purchase>
		<productType>Samsung</productType>
		<value>1</value>
		<action>subtract</action>
	</Purchase>
</Purchases>"
