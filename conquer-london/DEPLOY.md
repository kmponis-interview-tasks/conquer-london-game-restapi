## Clean docker engine
* Remove docker images, containers, volumes etc
<br>`$ docker system prune -a`
<br>`$ docker volume prune`
<br>`$ docker-compose rm -v`

## Manually Deployment
* Install MySQL
<br>- Run and check the logs
<br>`$ docker run --name=mysql1 -p 3306:3306 -d mysql/mysql-server:latest`v-mysql
<br>`$ docker logs mysql1`
<br>- Copy the 'GENERATED ROOT PASSWORD: '
<br>`$ docker logs mysql1 2>&1 | grep GENERATED` // lisL4jUdEcdIxaLaJ^4R+ExEs)e
<br>- Login and set-up to DB 
<br>`$ docker exec -it mysql1 mysql -uroot -p`
<br>`mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'password';`
<br>`mysql> CREATE USER 'user'@'%' IDENTIFIED BY 'password';`
<br>`mysql> GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;`
<br>`mysql> exit`
<br>`$ docker exec -it mysql1 mysql -uuser -p`

* Run conquer-london app
<br>- Build and run application
<br>`$ docker build -t conquerlondon -f Dockerfile .`
<br>`$ docker run -p 8880:8880 conquerlondon` // Skip this
<br>- Upload to dockerhub 
<br>`$ docker tag conquerlondon kbonis/conquer-london:latest`
<br>`$ docker login`
<br>`$ docker push kbonis/conquer-london:latest`
<br>- Deploy to server
<br>`$ docker stack deploy -c docker-compose.yml conquerlondon` // Deploy on swarn orchestration
