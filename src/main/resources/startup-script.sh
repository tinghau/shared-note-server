apt-get install -yq openjdk-11-jdk git maven gradle

mkdir -p ~/.gradle && echo "org.gradle.daemon=false" >> ~/.gradle/gradle.properties
export GRADLE_OPTS=-Dorg.gradle.daemon=false

cd ~/app/

git clone https://github.com/tinghau/shared-note-server.git

cd shared-note-server
./gradlew build