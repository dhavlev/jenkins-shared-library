package org.my.jenkins

void install(config) 
{
    git(
        branch: config.cd.chart.branch, 
        url: config.cd.chart.repo
    )

    if(config.cd.infra)
    {
        createNamspace(config.cd.infra.chart)
        sh(
            "helm upgrade --install ${config.cd.infra.chart} ${config.cd.infra.chart} --namespace ${config.cd.infra.chart}"
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
                    script: "kubectl create namespace ${namespace} || kubectl label namespace ${namespace} istio-injection=enabled",
                    returnStatus: true
                )
    
}