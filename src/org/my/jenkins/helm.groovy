package org.my.jenkins

void install(ciConfig, cdConfig) 
{
    def repository = "${ciConfig.dockerRegistry}/${ciConfig.repoName}"
    def tag = "${ciConfig.version}.${env.BUILD_ID}"

    git(
        branch: cdConfig.chart.branch, 
        url: cdConfig.chart.repo
    )

    if(cdConfig.infra)
    {
        createNamspace(cdConfig.chart)
        sh(
            "helm upgrade --install ${cdConfig.chart} ${cdConfig.chart} --namespace ${cdConfig.chart}"
        )
    }
    else
    {
        createNamspace(cdConfig.repoName)
        sh(
            "helm upgrade --install ${ciConfig.repoName} ${ciConfig.repoName} \
            --set image.repository=${repository} \
            --set image.tag=${tag} \
            --namespace ${ciConfig.repoName}"
        )
    }
}

void createNamspace(namespace)
{
    def status = sh(
                    script: "kubectl create namspace ${namespace} || kubectl label namespace ${namespace} istio-injection=enabled"
                    returnStatus: true
                )
    
}