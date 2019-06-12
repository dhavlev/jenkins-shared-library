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
        stage('Non-Sequential Stage') {
            agent {
                kubernetes {
                    label podLabel
                    defaultContainer 'jnlp'
                    yaml podConfig
                }
            }
            steps {
                script{
                    new logger().log("On Non-Sequential Stage")
                }
            }
        }
        stage('Sequential') {
            agent {
                kubernetes {
                    label podLabel
                    defaultContainer 'jnlp'
                    yaml podConfig
                }
            }
            environment {
                FOR_SEQUENTIAL = "some-value"
            }
            stages {
                    stage('In Sequential 1') {
                        steps {
                            container('maven') {
                                script{
                                    new logger().log("In Sequential 1")
                                }
                                
                            }
                        }
                    }
                    stage('In Sequential 2') {
                        when {
                            branch 'master'
                        }
                        steps {
                            container('maven') {
                                script{
                                    new logger().log("In Sequential 2")
                                }
                                
                            }
                        }
                    }
                    stage('Parallel In Sequential') {                       
                        parallel {
                            stage('In Parallel 1') {
                                steps {
                                    container('maven') {
                                        script{
                                            new logger().log("In Parallel 1")
                                        }
                                        
                                    }
                                }
                            }
                            stage('In Parallel 2') {
                                steps {
                                    container('maven') {
                                        script{
                                            new logger().log("In Parallel 2")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
