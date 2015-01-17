#NurseSchedule
**NurseSchedule** is an application to generate and organize shift schedules for nurses.

##Description
More information is available on the [wiki](https://github.com/goniex/NurseSchedule/wiki)

##Environments
###Remote
Worked application instance is deployed on **heroku.com** at [nurse-schedule.herokuapp.com](https://nurse-schedule.herokuapp.com)

###Local
How to deploy the application on local machine.
####Requirements
- Java 1.8.0_25
- Apache Tomcat 8.0.14 [download](http://archive.apache.org/dist/tomcat/tomcat-8/)
- Apache Maven 3 [download](http://maven.apache.org/download.cgi)

####Run steps
- clone repository
- go to project directory using the terminal
- build the application <code>mvn clean install</code>, application will be built on *target* folder
- copy application *NurseSchedule.war* to *TOMCAT_HOME/webapps* directory
- go to *TOMCAT_HOME/bin* directory using the terminal
- start Apache Tomcat server <code>startup.sh</code> (for Linux) <code>startup.bat</code> (for Windows)
- application will be available at [localhost:8080/NurseSchedule](http://localhost:8080/NurseSchedule)
- to stop Apache Tomcat server <code>shutdown.sh</code> (for Linux) <code>shutdown.bat</code> (for Windows)