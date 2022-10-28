# Payex kandidatoppgave


## Run the software

Denne løsningen krever at du har docker installert med mariaDb som database.  Kjør derfor følgende:

This solution requires that you have docker-installed with MariaDb. Therefore you should have docker installed before running next command.


``docker run -p 127.0.0.1:3306:3306  --name tvapi -e MARIADB_ROOT_PASSWORD=passord -d mariadb:latest``

Then you can run this. You will likely have to start with "sudo" if you are running on linux
``docker exec -it tvapi mariadb --user root -ppassord``

Now you are inside the database. Make a database with the name "tvapi" by running:

``create database tvapi;``

Go to the root-folder and run. This command installs what you need:

``./gradlew bootRun`` 



Gå to "src/frontend" and run:

``npm run dev``

Here you start the frontend software. 