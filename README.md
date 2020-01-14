# Conquer London
A game to conquer map location points in London using mobile phone. The detailed requirments are located in the folder [information](https://github.com/kmponis/conquer-london-game-restapi/tree/master/information). The application is running on AWS following 
the [instructions](https://github.com/kmponis/conquer-london-game-restapi/tree/master/AWS).
<br><br>[ENJOY](http://54.194.218.84:9999/swagger-ui.html)

### Posible Actions
* Get a location point in a certain area
<br>- Input: postcode(London)
<br>- Output: Closest location point and location coordinates(JSON format)
* Conquer a location point
<br>- Input: userId, current coordinates(JSON format) and the location point you found above
<br>- Output: Confirmation and Error message
* Show your score
<br>- Input: userId
<br>- Output: Number of conquered location points

### Build, Deploy and Test Locally 
* Prerequisites:
<br>- Install docker, docker-compose
* Open CLI and move to your workspace: 
<br>`$ git clone https://github.com/kmponis/conquer-london.git`
<br>`$ cd conquer-london`
* Build, Deploy and Test containerised application 
<br>`$ docker-compose build`
<br>`$ docker-compose up`
<br>`$ curl http://<ip or localhost>:9999`
* Initialise and Check DB
<br>`$ curl http://<ip or localhost>:9999/initialiseDB/admin`
<br>`$ curl http://<ip or localhost>:9999/allLocationPoint`
* Test application
<br>`http://<ip or localhost>:9999/swagger-ui.html`
<br>- Check test coverage (97%)
<br>`/conquer-london/target/jacoco-reports/index.html`
