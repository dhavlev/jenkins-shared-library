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
        agent none {
            kubernetes {
                label podLabel
                defaultContainer 'jnlp'
                yaml podConfig
            }
        }
        stages {
            stage('Stage-Sq-1') {
                steps {
                    agent {
                        kubernetes {
                        label podLabel
                        defaultContainer 'jnlp'
                        yaml podConfig
                    }

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
                
                stages{
                    stage("Stage-B1"){
                        step{
                            echo "Stage B1"
                        }
                    }
                    stage("Stage-B2"){
                        step{
                            echo "Stage B2"
                        }
                    }
                    stage("Parallel Stage"){
                        parallel{
                            stage("Stage-C1"){
                                step{
                                    echo "Paraller Stage C1"
                                }
                            }
                            stage("Stage-C2"){
                                step{
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