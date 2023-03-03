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
        stage("Build docker image"){
            steps{
                script{
                    sh 'docker build -t izicap.api .'
                    sh 'docker image ls'
                }
            }
        }

        stage("call Robot framework job"){
            steps{
                script{
                    build job: "TA_IZICAP_Robot_Framework", wait: false
                }
            }
        }
    }
}
