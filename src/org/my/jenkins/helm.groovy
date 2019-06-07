package org.my.jenkins

void install(config) 
{
    def repository = "${config.dockerRegistry}/${config.reponame}"
    def tag = "${config.version}.${env.BUILD_ID}"

    echo("Install: ${config}, ${repository}, ${tag}")

    git(
        branch: 'voting-app', 
        url: 'https://github.com/dhavlev/helm-charts.git'
    )
    
    sh(
        "helm upgrade --install ${config.reponame} ${config.reponame} \
        --set image.repository=${repository} \
        --set image.tag=${tag} \
        --namespace ${config.reponame}"
    )
}