#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    
    String podConfig = libraryResource "podConfig.yaml"
    def label = "ci-${UUID.randomUUID().toString()}"

    podTemplate(label: label, yaml: podConfig)   
    {
        body()
        
        node(label)
        {            
            container("maven") 
            {             
                stage("checkout")
                {
                    dir("${WORKSPACE}/src/github.com/jenkins-shared-library"){
                        checkout scm
                    }
                }

                stage("build+test")
                {
                    new logger().log("building and testing applications")
                }

                stage("sonar")
                {
                    new logger().log("performing sonar analysis")
                }
            }

            stage("reports")
            {
                parallel cobertura:
                {
                    stage("cobertura")
                    {
                        new logger().log("generating cobertura reports")
                    }
                },junit:                 
                {
                    stage("junit")
                    {
                        new logger().log("generating jnuit reports")
                    }
                },html:                 
                {
                    stage("html")
                    {
                        new logger().log("generating html reports")
                    }
                }
            }

            container("maven") 
            {
                stage("docker build")
                {
                    new logger().log("building docker image")
                }

                stage("docker publish")
                {
                    new logger().log("publishing docker image")
                }           
            }
        }

        cd
        {
           env = "int"
        }

        cd
        {
            env = "qa"
        }

        cd
        {
            env = "stage"
        }

        cd
        {
            env = "prod"
        }
    }
}