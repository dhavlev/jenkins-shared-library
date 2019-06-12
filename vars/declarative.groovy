#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    
    String podConfig = libraryResource "podConfig.yaml"
    def podLabel = "ci-${UUID.randomUUID().toString()}"

    pipeline {
        agent none
        stages {
            stage('Stage-Sq-1') {
                agent {
                    kubernetes {
                        label podLabel
                        defaultContainer 'jnlp'
                        yaml podConfig
                    }
                }
                steps {
                    container('maven') {
                        sh 'mvn -version'
                    }
                }
            }

            stage('Stage-A') {
                agent {
                    kubernetes {
                        label podLabel
                        defaultContainer 'jnlp'
                        yaml podConfig
                    }
                }
                
                stages {
                    stage("Stage-B1"){
                        steps{
                            echo "Stage B1"
                        }
                    }
                    stage("Stage-B2"){
                        steps{
                            echo "Stage B2"
                        }
                    }
                    stage("Parallel Stage"){
                        parallel{
                            stage("Stage-C1"){
                                steps{
                                    echo "Paraller Stage C1"
                                }
                            }
                            stage("Stage-C2"){
                                steps{
                                    echo "Paraller Stage C2"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}