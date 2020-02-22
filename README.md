# medium-java-security-exams
medium-java-security-exams is an example project to study secure software development. 
It contains a simple application to perform multiple-choice exams with multiple participants. 
It therefore provides the following features:
- A teacher can create an exam by uploading a XML-File with the exam definition. 
- The teacher gets an exam key he can share with the students.
- Students can then perform the exams and get instant feedback if they responded the questions correctly.
- The teacher can check who has already successfully taken the exam.
- The teacher can stop the exam and get a final overview over the participants
For authentication any OAuth2-Infrastructure providing access-tokens as JWT can be used. 
The desired authentication server has to be configured in the angular application

## Installing / Getting started
To start this locally, a [docker-compose](./src/test/docker/docker-compose.yml) file is provided. 
It starts the frontend using the node development server and a keycloak authentication server with self registration.

The application can be started with the command
```
./mvnw package && docker-compose -f src/test/docker/docker-compose.yml up
```
You can then reach the application under http://localhost:8080/

You can also start the UI, the backend and keycloak individually. Keycloak can be started using those commands
```
docker run --rm -p 9080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=secret -e KEYCLOAK_IMPORT=/tmp/example-realm.json -v `pwd`/src/test/docker/keycloak.json:/tmp/example-realm.json jboss/keycloak -b 0.0.0.0 -Djboss.http.port=8080

mvn spring-boot:run

ng serve 

npm start --prefix=src/main/angular/
```
then the UI and the backend can be started normally using `ng serve` and `mvn spring-boot:run`.
