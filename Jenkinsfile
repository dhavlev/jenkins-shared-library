
@Library("jenkins-shared-library") _

def deployConfig = [
    deploy: [
        int: true,
        qa: true,
        stage: false,
        prod: false
    ]
]


ci{
    cd = deployConfig
}