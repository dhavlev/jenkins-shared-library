#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    
    String podConfig = libraryResource "podConfig-kaniko.yaml"
    def label = "ci-${UUID.randomUUID().toString()}"

    println config

    podTemplate(label: label, yaml: podConfig)   
    {
        body()
        
        node(label)
        {
            container(name: "kaniko", shell: "/busybox/sh")
            {
                stage("Build and Push Docker")
                {
                    checkout scm
                    withEnv(["PATH+EXTRA=/busybox:/kaniko"]) {
                        sh """#!/busybox/sh
                            /kaniko/executor --dockerfile=Dockerfile --context=`pwd` --destination="${config.ci.dockerRegistry}/${config.ci.repoName}:${config.ci.version}.${env.BUILD_ID}"
                        """
                    }
                }
            }
        }
    }
}