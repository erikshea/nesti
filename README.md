Nesti Account Manager
=============
A manager for user account creation, connection, and editing.  Users can log in and stay connected between sessions. Supports MySQL, and SQLite for local testing. Click [here](#running-the-project) for instruction on how to run the project, and [here](#dependencies) for a list of dependencies.

## Account creation
![](https://github.com/erikshea/nesti/blob/master/assets/readme/creation.png?raw=true)

Validation conditions are updated as users type. Fields are styled in rea-time as valid (orange) or invalid (green), and submit button goes from greyed-out and disabled to blue when all conditions are met.

## Account modification
![](https://github.com/erikshea/nesti/blob/master/assets/readme/modification.png?raw=true)

Likewise, users can only validate changes when all conditions are met.

## Connection
![](https://github.com/erikshea/nesti/blob/master/assets/readme/connection.png?raw=true)

User stays connected between app launches.

## Database settings
![](https://github.com/erikshea/nesti/blob/master/assets/readme/database-settings.png?raw=true)

SQLite (for local testing) or MySQL can be used to store registered users.

## Populating with test data
![](https://github.com/erikshea/nesti/blob/master/assets/readme/populate_database.png?raw=true)

You can populate your database with example users by going into the Database menu.

## Running GUI and validation tests
* *"GUIUserAccountControlTest.java"* tests GUI elements (fields, buttons, menus).
* *"GUISettingsUserAccountControlTest.java"* tests the database settings dialog (made seperate from the above to allow testing of GUI elements with your own database).
* *"UserAccountControlTest.java"* tests field validation, SQL injection, field formatting, submit button activation, and user creation and connection.

Running the project
=============
*Right-click on project->Run As->Run Configurations, make "application.NestiUserAccountMain" main class,
	then click on Arguments->VM arguments,
	then enter the following line, substituting "F:\dev\javafx-sdk-15\lib" with your own javafx lib dir:
--module-path "F:\dev\javafx-sdk-15\lib" --add-modules javafx.controls,javafx.fxml ${build_files}

Dependencies
=============
The "assets" directory should be in the build path. The Eclipse project in this repository also includes the following user libraries in the build path. To add a user library in Eclipse: *Window*>*Preferences*>*Java*>*Build Path*>*User Libraries*>*New*, then add external jars.

### javafx15
* [All jars in the "lib" folder.](https://gluonhq.com/products/javafx/)

### testFX
* [testfx-core-4.0.16-alpha.jar](https://repo1.maven.org/maven2/org/testfx/testfx-core/4.0.16-alpha/testfx-core-4.0.16-alpha.jar)
* [testfx-junit-4.0.15-alpha.jar](https://repo1.maven.org/maven2/org/testfx/testfx-junit/4.0.15-alpha/testfx-junit-4.0.15-alpha.jar)
* [testfx-junit5-4.0.16-alpha.jar](https://repo1.maven.org/maven2/org/testfx/testfx-junit5/4.0.16-alpha/testfx-junit5-4.0.16-alpha.jar)

### BCrypt
* [bcrypt-0.9.0-optimized.jar](https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.9.0/bcrypt-0.9.0-optimized.jar)
* [bytes-1.4.0.jar](https://repo1.maven.org/maven2/at/favre/lib/bytes/1.4.0/bytes-1.4.0.jar)

### jdbc_mysql
* [mysql-connector-java-8.0.22.jar](https://dev.mysql.com/downloads/connector/j/)

### jdbc_sqlite
* [sqlite-jdbc-3.34.0.jar](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.34.0/sqlite-jdbc-3.34.0.jar)

