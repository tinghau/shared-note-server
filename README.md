# shared-note-server

### Description

A simple Java web-socket server that allows concurrent real-times edits of text notes, 
i.e. multiple users can edit a note at the same time. 
Consider it a very simple implementation of Google Docs.

There are Android, iOS and web clients, developed using [flutter](http://flutter.dev). 
The client source can be found [here](https://github.com/tinghau/shared-note-app).

### Installation
1. Download the source.
2. `cd shared-note-server`
3. `./gradlew build`

A Gradle Wrapper is included in the source, so no additional steps are needed to install Gradle. 
Gradle will take care of the build and app execution.

Compiled and run with Java 11.

### Usage
To start the server: `./gradlew run`

The Android, iOS and web clients can be built from [here](https://github.com/tinghau/shared-note-app).

### Details
Clients connect to a desired session, as defined by a text string.
The service then serves up the text for that session, which can be edited.

Another client can then connect to the same session, by entering the same text string.
Now both clients can view and edit the same piece of text.

The text persists in-memory, so it is available till the service restarts.

Behind the scenes, the service versions each line of text. 
For an edit to be accepted, the client needs to provide the expected next version number for that line of text. 
The provided version number ensures that the client has updated the latest version of that line of text. 
If the version is unexpected, the server discards the update. 
If the server discards the update, the latest text (without changes) is published so that the client can correct itself, 
i.e. so that it knows the server rejected the update.

### To Do
* Review behaviour in high-latency environments when there are concurrent edits across clients.