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
        agent {
            kubernetes {
                label podLabel
                defaultContainer 'jnlp'
                yaml podConfig
            }
        }
        stages {
            stage('Run maven') {
                steps {
                    container('maven') {
                        sh 'mvn -version'
                    }
                    container('busybox') {
                        sh '/bin/busybox'
                    }
                }
            }
        }
    }
}