#!/usr/bin/groovy

import org.my.jenkins.*

def call(body) {
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    println "Printing Config Values: ${config}"

    ci{
        ci = config.ci
    }

    //cd{
    //    cd = config.cdConfig
    //}
}