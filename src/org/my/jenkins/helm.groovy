package org.my.jenkins

void install(config) 
{
    git(
        branch: cdConfig.chart.branch, 
        url: cdConfig.chart.repo
    )

    if(config.cd.infra)
    {
        createNamspace(config.cd.chart)
        sh(
            "helm upgrade --install ${config.cd.chart} ${config.cd.chart} --namespace ${config.cd.chart}"
        )
    }
    else
    {
        def repository = "${config.ci.dockerRegistry}/${config.ci.repoName}"
        def tag = "${config.ci.version}.${env.BUILD_ID}"

        createNamspace(config.ci.repoName)
        sh(
            "helm upgrade --install ${config.ci.repoName} ${config.ci.repoName} \
            --set image.repository=${repository} \
            --set image.tag=${tag} \
            --namespace ${config.ci.repoName}"
        )
    }
}

void createNamspace(namespace)
{
    def status = sh(
                    script: "kubectl create namspace ${namespace} || kubectl label namespace ${namespace} istio-injection=enabled",
                    returnStatus: true
                )
    
}