
@Library("jenkins-shared-library") _

def deployConfig = [
    deploy: [
        int: true,
        qa: false,
        stage: false,
        prod: false
    ]
]

def dockerConfig = [
    dockerRegistry: "dhavlev",
    repoName: "voting-app",
    version: 1.0.0
]


ci{
    docker = dockerConfig
    cd = deployConfig
}