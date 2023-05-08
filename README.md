# README #

This is a dummy microservice.

It manages users and tokens. There is one pre-created admin user. The admin user can create other users. 
Users (admin or not) can create new tokens and refresh existing unexpired tokens.

Token expiration date is set to 60 seconds after creation or refresh date.

## Specification ##

### Token creation/refresh ###

POST /tokens

JSON payload :  `{    "username": "<user name>",    "password": "<password>"  }`

HTTP response status

- 201 (Created) : successful token creation

- 200 (OK) : successful token refresh

- 401 (Unauthorized) : incorrect username/password combination is used

Response : `{"token": "String", "creationDate": "yyyy-MM-dd HH:mm:ss", "expirationDate": "yyyy-MM-dd HH:mm:ss"}`

Notes:

- any user can create a token if correct username/password combination is used

- if token is not expired, the existing token is refreshed; no new token created. In case of refresh the expiration date is extended.

- if caller has an existing token that is expired, a new token is created.




### User creation ###

POST /users

Headers: `Authorization: bearer <valid token>`

JSON payload :  `{    "username": "<user name>", "password": "<password>"  }`

HTTP response status

- 201 (Created) : successful user creation

- 409 (Conflict) : if attempting to create a duplicate user

- 401 (Unauthorized) : call without a valid unexpired token

- 403 (Forbidden) : call with a valid unexpired non-admin token


Response : `{"username": "String", "id": Long}`


## How to build and run ##

1. Build the service using maven
2. Build a docker image
3. Run the docker image. Inside the container, the service listens on port 8080. Use `docker run` with a -p argument to specify the external listening port, e.g `-p 80:8080/tcp` to expose the service on port 80.

## Jenkinsfile ##
pipeline{
    agent any
    tools{
        maven "M2_HOME"
    }
    stages{
        stage("Build maven project"){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/njartiana/IZICAP']]])
                sh 'mvn clean install'
            }
        }
        stage("Build and run service"){
            steps{
                script{
                    sh 'mvn compile && mvn install'
                    sh 'cd target && java -jar izicap-api.jar'
                }
            }
        }

        /*stage("call Robot framework job"){
            steps{
                script{
                    build job: "TA_IZICAP_Robot_Framework", wait: false
                }
            }
        }*/
    }
}
