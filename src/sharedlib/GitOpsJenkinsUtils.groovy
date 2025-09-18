
package sharedlib

class GitOpsJenkinsUtils extends BaseUtil {

  def dockerJenkinsUtils
  
  public GitOpsJenkinsUtils(script, String type = ''){
    super(script, type)
    dockerJenkinsUtils = new DockerJenkinsUtils(script, type)
  }

  public buildAndPushImage(){
    
    printMessage("Build And Push Image")
    
    dockerJenkinsUtils.build(projectName: "${projectName}", version: "${version}", nroPase: "${nroPase}")
  }

  public syncWithArgoCd(String argocdRepoYaml, String argocdNamespace, String argocdProject) {
    
    printMessage("Sync With ArgoCd")

    script.withCredentials([script.usernamePassword(credentialsId: "${script.env.ARGOCD_CREDENTIALS_ID}", usernameVariable: 'ARGOCD_USERNAME', passwordVariable: 'ARGOCD_PASSWORD')]){

      script.sh "argocd login ${script.env.ARGOCD_HOST} --username ${script.env.ARGOCD_USERNAME} --password ${script.env.ARGOCD_PASSWORD} --insecure"

      def existsArgoCdApp = this.script.sh(
        script: "argocd app list | grep -wq ${projectName} && echo true || echo false",
        returnStdout: true).trim() == 'true'      
      
      printMessage("existsArgoCdApp: ${existsArgoCdApp}")
      
      if (existsArgoCdApp) {
        printMessage("Update argoCd Application: ${projectName}")
        script.sh "argocd app set ${projectName} --sync-policy none --grpc-web;"
        script.sh "argocd app set ${projectName} --revision ${script.env.BRANCH} --grpc-web;"                
        script.sh "argocd app patch ${projectName} --patch '{\"metadata\":{\"labels\":{\"paseNro\":\"${nroPase}\"}}}' --type merge"
        script.sh "argocd app set ${projectName} --sync-policy automated --grpc-web;"
      } else {
        printMessage("Creating argoCD Application: ${projectName}")
        script.sh """ 
        #!/bin/bash
        argocd app create ${projectName} \
        --repo https://github.com/${argocdRepoYaml} \
        --revision ${script.env.BRANCH} \
        --path solar-system \
        --dest-server ${script.env.ARGOCD_CLUSTER_K8S} \
        --dest-namespace ${argocdNamespace} \
        --project ${argocdProject} \
        --label paseNro=${nroPase} \
        --grpc-web;
        """
      }
      script.sh "argocd app sync ${projectName}"
    }
  }
}
