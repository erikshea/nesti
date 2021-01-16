Nesti Account Manager
=============
A manager for user account creation, connection, and editing.  Users can log in and stay connected between sessions. Supports MySQL, and SQLite for local testing. Click [here](#running-the-project) for instructions on how to run the project, and [here](#dependencies) for a list of dependencies.

## Connection
![](https://github.com/erikshea/nesti/blob/master/src/main/resources/readme/connection.png?raw=true)

User stays connected between app launches.

## Account creation
![](https://github.com/erikshea/nesti/blob/master/src/main/resources/readme/creation.png?raw=true)

Validation conditions are updated continuously when fields are filled. Input areas and validation conditions are styled in real-time as valid (green) or invalid (orange), and submit button goes from greyed-out and disabled to green when all conditions are met.

## Account modification
![](https://github.com/erikshea/nesti/blob/master/src/main/resources/readme/modification.png?raw=true)

Likewise, users can only confirm validated changes. 

## Database settings
![](https://github.com/erikshea/nesti/blob/master/src/main/resources/readme/database-settings.png?raw=true)

SQLite (for local testing) or MySQL can be used to store registered users.

## Populating with test data
![](https://github.com/erikshea/nesti/blob/master/src/main/resources/readme/populate_database.png?raw=true)

You can populate your database with example users by going into the Database menu.

Running GUI and validation tests
=============
* *"GUIUserAccountControlTest.java"* tests GUI elements (fields, buttons, menus).
* *"GUISettingsUserAccountControlTest.java"* tests the database settings dialog (made seperate from the above to allow testing of GUI elements with your own database).
* *"UserAccountControlTest.java"* tests field validation, SQL injection, field formatting, submit button activation, and user creation and connection.

Dependencies
=============
All dependencies are included in the project's pom.xml file.

They include:
* JUnit 5
* javafx15
* testfx
* bcrypt
* jdbc_mysql
* jdbc_sqlite
