# codetest


Start up and empty database:

podman run --rm  --net=host --name responsibly-db -e POSTGRES_PASSWORD=responsibly -d docker.io/postgres:14.8-bullseye
    or
docker run --rm  --net=host --name responsibly-db -e POSTGRES_PASSWORD=responsibly -d postgres:14.8-bullseye


Run sbt test to populate the database with data, this is instead of using seeding to flyway.

After tests has successfully executed, do 
sbt run - which should bind an grpc server to a specified port.

This project requires some environment variables to load and run, this env varialbes have been added the verySecret.env file, as this is not a sensitive project, and no harm will be done by publishing details.


When the project is listening on a port, it should be possible for you to use any grpc client with the embedded protobuf files, to communicate and start using this server.


 


