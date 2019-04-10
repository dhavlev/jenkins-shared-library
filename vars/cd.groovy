#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    
    String podConfig = libraryResource "podConfig.yaml"
    def label = "cd-${UUID.randomUUID().toString()}"
    podTemplate(label: label, yaml: podConfig)   
    {
        body() 
        node(label)
        {
            def stageName = "deploy-${config.env}"
            stage(stageName)
            {
                if(!config.approval)
                {
                    input "Do you approve ${config.env} deployment?"
                }
                switch(config.env) 
                {
                    case "int":
                        new logger().log("deploying it on int")
                    break
                    case "qa":
                        new logger().log("deploying it on qa")
                    break
                    case "stage":
                        new logger().log("deploying it on stage")
                    break
                    case "prod":
                        new logger().log("deploying it on prod")
                    break
                    default:
                        new logger().log("unable to find ${config.env}", "ERROR")
                    break
                }
            }

            email
            {
                to = "abc@provider.net"
                from = "xyz@provider.net"
            }
        }       
    }
}