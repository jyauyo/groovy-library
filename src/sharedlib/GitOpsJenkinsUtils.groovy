
package sharedlib

class GitOpsJenkinsUtils extends BaseUtil {

  def dockerJenkinUtils = new DockerJenkinUtils(this);
  
  public GitOpsJenkinsUtils(script, String type = ''){
    super(script, type)
  }

  public buildAndPushImage(Map params){
    printMessage("buildAndPushImage")
    dockerJenkinUtils.build(params)
  }

  public syncWithArgoCd(Map params) {
    
    //sh 'echo Hi From DevOps Team'
    printMessage("${params.projectName}")
    printMessage("${params.version}")

    printMessage("***** Sync With ArgoCd")

    script.withCredentials([script.usernamePassword(credentialsId: "${script.env.ARGOCD_CREDENTIALS_ID}", usernameVariable: 'ARGOCD_USERNAME', passwordVariable: 'ARGOCD_PASSWORD')]){
      script.sh "argocd login ${ARGOCD_HOST} --username ${env.ARGOCD_USERNAME} --password ${env.ARGOCD_PASSWORD} --insecure"
      script.sh "argocd app set sistema-solar --sync-policy none --grpc-web;"
      script.sh "argocd app set sistema-solar --revision ${BRANCH} --grpc-web;"
      script.sh "argocd app set sistema-solar --sync-policy automated --grpc-web;"
      script.sh "argocd app sync sistema-solar"
      script.sh "argocd app patch sistema-solar --patch '{\"metadata\":{\"labels\":{\"paseNro\":\"${nroPase}\"}}}' --type merge"
    }
  }
}
