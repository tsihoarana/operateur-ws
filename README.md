# Tools
* Compile
```
mvn spring-boot:run
```

* Run local with Heroku Procfile
```
heroku local:start
```

* Logs
```
heroku logs --tail -a operateur-ws
```

Shell cloud
```bash
heroku run bash -a operateur-ws
```

# Notes / Problems
## Port
* I encountered some problems when deploying on heroku, the embeded tomcat server doesn't run for some dumb reason (I set the port to 80). Just like a heroku node environement, I guess the problem was because I didn't use the available port environement variable on the heroku server side. Something like this `use env.port OR default 5000` should be put but will be written as `server.port=${PORT:5000}` inside the `application.properties` file instead.
* All controllers should be in the same folder as the main application (in the same package as the artefactId, name labelled in `pom.xml`)
## Test query
```
GET http://localhost/test
GET http://localhost/test/list
POST/PUT/DELETE http://localhost/test with {"name" : "Test", "age" : 8} as input
```