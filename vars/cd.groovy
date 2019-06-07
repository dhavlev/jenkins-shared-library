#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    
    String podConfig = libraryResource "podConfig-helm.yaml"
    def label = "cd-${UUID.randomUUID().toString()}"
    podTemplate(label: label, yaml: podConfig)   
    {
        body() 
        node(label)
        {
            stage("helm-deploy")
            {
                echo("Printing CD Config: ${config}")
                new helm().install(config.helm)
            }
        }
    }
}