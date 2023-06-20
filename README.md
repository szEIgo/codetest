# Coding Test for a Responsible Company

Prerequisites:
- postgres or Docker
- scala


Stack:
- Web,Concurrency = Akka
- Rpc = Akka-Grpc
- Dao = Doobie
- Migration = Flyway
- Test = Scalatest
- Logger = airframe-log



Protobuf files:
/src/main/proto/SustainabilityService.proto


How to run:
- sbt test (also adds data to the database)
- sbt run (Server or Client)
- verySecret.env file has been bundled with the project




if you need a potsgres in a jiffy:
```
docker run --rm --net=host --name responsibly-db -e POSTGRES_PASSWORD=responsibly -d postgres:14.8-bullseye

```


