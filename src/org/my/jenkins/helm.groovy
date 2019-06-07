package org.my.jenkins

void install(ciConfig, cdConfig) 
{
    def repository = "${ciConfig.dockerRegistry}/${ciConfig.reponame}"
    def tag = "${ciConfig.version}.${env.BUILD_ID}"

    git(
        branch: cdConfig.chart.branch, 
        url: cdConfig.chart.repo
    )
    
    sh(
        "helm upgrade --install ${ciConfig.reponame} ${ciConfig.reponame} \
        --set image.repository=${repository} \
        --set image.tag=${tag} \
        --namespace ${config.reponame}"
    )
}