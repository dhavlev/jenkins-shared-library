#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    if(config.ci)
    {
        ci{
            ci = config.ci
        }
    }
    
    if(config.cd)
    {
        cd{
            ci = config.ci
            cd = config.cd
        }
    }
}