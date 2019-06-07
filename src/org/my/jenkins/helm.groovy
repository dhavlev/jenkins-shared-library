package org.my.jenkins

void install(ciConfig, cdConfig) 
{
    def repository = "${ciConfig.dockerRegistry}/${ciConfig.repoName}"
    def tag = "${ciConfig.version}.${env.BUILD_ID}"

    git(
        branch: cdConfig.chart.branch, 
        url: cdConfig.chart.repo
    )
    
    sh(
        "helm upgrade --install ${ciConfig.repoName} ${ciConfig.repoName} \
        --set image.repository=${repository} \
        --set image.tag=${tag} \
        --namespace ${ciConfig.repoName}"
    )
}