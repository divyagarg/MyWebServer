# WebServer
Multithreaded Web server implemented in Java

# Design
- HttpServer is the server class. I am passing the port and maximum no of thread to Server from service.properties file.
- Server is using LinkedBlockingQueue(Thread safe) workQueue, which is getting used for all client connections.
- To manage the worker threads I am using Executor service.
- Server will keep running until its thread is inturrupted.
- For each request, server creates a RequestHandler and create a thread for this request and pass this request to threadpool to execute sometime in future.
- Request Handler parses the reqeust into custom HttpRequest.
- Request Handler delegate it to app(FileServingApp) to process the request as per business logic.
- App(FileServingApp)  process the HttpRequest and return the HttpResonse.(For now, For PUT/POST request I am just parsing the request body but not doing anything with it. If request body exist I am considering it as a success case.)
- On Receiving the HttpResponse, RequestHandler write the response to OutputStream.
- Request Handler closes the stream.

# Build Step
- ./gradlew build (It will run all test cases as well)
- ./gradlew assemble (To exclude testcases)

# Run Step

Build step creates jar file in build/libs/mywebserver-1.0-SNAPSHOT.jar

execute java -jar "path to jar file"

