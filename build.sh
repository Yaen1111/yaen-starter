#git pull and checkout
git pull

#maven clean and install, skip test
mvn clean package -Denv=dev -DsvnVersion=0 -DskipTests

