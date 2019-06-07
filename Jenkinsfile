
@Library("jenkins-shared-library") _

def cdConfig = [
    chart: [
        repo: "https://github.com/dhavlev/helm-charts.git",
        branch: "voting-app"
    ]

    deploy: [
        int: true,
        qa: false,
        stage: false,
        prod: false
    ]
]

def ciConfig = [
    dockerRegistry: "dhavlev",
    repoName: "voting-app",
    version: 1.0.0
]


main{
    ci = ciConfig
    cd = cdConfig
}