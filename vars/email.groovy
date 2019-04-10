def call(body) 
{
    // evaluate the body block, and collect configuration into the object    
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    echo  "Email sent to - ${config.to} from ${config.from}"
}