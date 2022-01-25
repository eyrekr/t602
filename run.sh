mvn clean package -DskipTests | grep -e 'BUILD SUCCESS' -e 'BUILD FAILURE' -e 'Building' | cut -d' ' -f2-
java -jar target/t602-1.0-SNAPSHOT.jar

