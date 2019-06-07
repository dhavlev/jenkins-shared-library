#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    
    ci{
        ci = config.ci
    }

    //cd{
    //    cd = config.cdConfig
    //}
}