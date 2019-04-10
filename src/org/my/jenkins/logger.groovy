package org.my.jenkins

void log(String message, String level = "INFO") 
{        
    switch(level)
    {
        case 'INFO':
            println("INFO: ${message}")
        break
        case 'ERROR':
            println("ERROR: ${message}")
        break
        case 'WARN':
            println("WARN: ${message}")
        break
        default:
            println("UNKNOWN LOG: ${message}")
        break
    }    
}