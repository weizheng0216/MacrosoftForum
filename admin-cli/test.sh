mvn clean; 
mvn package; 
cat ./integrationTestCommand | mvn exec:java  