# Time Tracker Client Assignemnt
Time tracker client
The spring boot application exposes REST endpoints and a User Interface as part of Time tracker client requirements. It should be able to :
- As a user, I would like to see time track records of any given employee that I provide by typing in his/her email address into a text-input-field.
- As a user I would like to record time track of any given employee by providing following data:
  - Work start time (e.g. 28.12.2016 09:00)
  - Work end time (e.g. 28.12.2016 17:00)
  - Email address of the employee
This application should integrate to a legacy application via docker.

# Solution

**Prerequisites** 

Client/Legacy Application were tested locally so some changes are required in properties as follows:

In application-dev.properties at (`client\src\main\resources`), replace the ip address with the local machine's ip for the key value 

`tt.app.uri=http://192.168.29.182:8080/records`

Build the jar file

`mvn clean install`

Build the docker image

`docker build -t amarwadhwani/time-tracker-client .`

Start the legacy application and the client application

`docker run -it -p 8080:8080 alirizasaral/timetracker:1`

`docker run -it -e "SPRING_PROFILES_ACTIVE=dev" -p 8081:8081 amarwadhwani/time-tracker-client`

# Swagger

Swagger can be accessed here <http://localhost:8081/swagger-ui.html>

# UI

UI can be accessed here <http://localhost:8081/index>

**Next Version Improvements**
- Better UI
- Logs should be written in log files as well
- Code can be distribute in different layers like validator, mapper etc.
- Additional configuration for environment specific, ports etc 
