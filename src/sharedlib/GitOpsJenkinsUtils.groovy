
package sharedlib

class GitOpsJenkinsUtils extends BaseUtil {

  def dockerJenkinsUtils
  
  public GitOpsJenkinsUtils(script, String type = ''){
    super(script, type)
    dockerJenkinsUtils = new DockerJenkinsUtils(script, type)
  }

  public buildAndPushImage(){
    printMessage("***** Build And Push Image")
    dockerJenkinsUtils.build()
  }

  public syncWithArgoCd(Map params) {
    
    //sh 'echo Hi From DevOps Team'
    printMessage("${params.projectName}")
    printMessage("${params.version}")
    printMessage("${params.nroPase}")

    printMessage("***** Sync With ArgoCd")

    script.withCredentials([script.usernamePassword(credentialsId: "${script.env.ARGOCD_CREDENTIALS_ID}", usernameVariable: 'ARGOCD_USERNAME', passwordVariable: 'ARGOCD_PASSWORD')]){
      script.sh "argocd login ${script.env.ARGOCD_HOST} --username ${script.env.ARGOCD_USERNAME} --password ${script.env.ARGOCD_PASSWORD} --insecure"
      script.sh "argocd app set sistema-solar --sync-policy none --grpc-web;"
      script.sh "argocd app set sistema-solar --revision ${params.branch} --grpc-web;"
      script.sh "argocd app set sistema-solar --sync-policy automated --grpc-web;"
      script.sh "argocd app sync sistema-solar"
      script.sh "argocd app patch sistema-solar --patch '{\"metadata\":{\"labels\":{\"paseNro\":\"${params.nroPase}\"}}}' --type merge"
    }
  }
}
