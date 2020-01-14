### AWS Ubuntu 1.8
* Create a EC2 instance and connect
<br>-Use the chmod to make your private key file not publicly viewable
<br>`$ chmod 600 AWS/conquer-london-key-pair-name.pem`
<br>-Connect to the instance
<br>`$ ssh -i AWS/conquer-london-key-pair-name.pem ubuntu@public_dns_name`
* Install docker
<br>`$ sudo apt update`
<br>`$ sudo apt install apt-transport-https ca-certificates curl software-properties-common`
<br>`$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -`
<br>`$ sudo apt-key fingerprint 0EBFCD88`
<br>`$ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"`
* Install docker-ce
<br>`$ sudo apt-get update`
<br>`$ sudo apt-get install docker-ce`
<br>- Get version(18.06.0), Install and Test
<br>`$ apt-cache madison docker-ce`
<br>`$ sudo apt-get install docker-ce=<VERSION>` 
<br>`$ sudo docker run hello-world`
* Install docker-compose
<br>`$ sudo curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose`
<br>`$ sudo chmod +x /usr/local/bin/docker-compose`
<br>`$ docker-compose --version`
* Clone and deploy application
<br>`$ mkdir lab`
<br>`$ cd lab`
<br>`$ git clone https://github.com/kmponis/conquer-london-game-restapi.git`
<br>`$ sudo docker-compose build`
<br>`$ sudo docker-compose -f docker-compose-dev.yaml up`
* Initialise and Check DB
<br>`$ curl http://<ip or localhost>:9999/initialiseDB/admin`
<br>`$ curl http://<ip or localhost>:9999/allLocationPoint`
* Test on browser
<br>`http://<IPv4 Public IP>:9999/swagger-ui.html`
