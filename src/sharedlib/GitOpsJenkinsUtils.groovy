
package sharedlib

class GitOpsJenkinsUtils extends BaseUtil {

  def dockerJenkinsUtils
  
  public GitOpsJenkinsUtils(script, String type = ''){
    super(script, type)
    dockerJenkinsUtils = new DockerJenkinsUtils(script, type)
  }

  public buildAndPushImage(){
    printMessage("***** ooooooo Version: ${version}")
    printMessage("***** Build And Push Image")
    dockerJenkinsUtils.build(projectName: "${projectName}", version: "${version}", nroPase: "${nroPase}")
  }

  public syncWithArgoCd() {
    
    //sh 'echo Hi From DevOps Team'
    //printMessage("${params.projectName}")
    //printMessage("${params.version}")
    //printMessage("${params.nroPase}")

    printMessage("***** Sync With ArgoCd")

    script.withCredentials([script.usernamePassword(credentialsId: "${script.env.ARGOCD_CREDENTIALS_ID}", usernameVariable: 'ARGOCD_USERNAME', passwordVariable: 'ARGOCD_PASSWORD')]){
      //EXISTE=$(argocd app list | grep ${projectName}  | echo 1 || echo 2)
      script.sh "argocd login ${script.env.ARGOCD_HOST} --username ${script.env.ARGOCD_USERNAME} --password ${script.env.ARGOCD_PASSWORD} --insecure"

      def argocdok = this.script.sh(
        script: "argocd app list | grep ${projectName} && echo true || echo false",
        returnStdout: true).trim()
      
      printMessage("***** Does app exists? ${argocdok}")
      if (argocdok) {
        printMessage("***** update")
      } else {
        printMessage("***** crear")
      }
      /*
      argocd app create capturador-plataformaunica \
      --repo http://gitlab.insi.sunat.peru/gestionsaldos/recaudacionms2-tributaria-gsc-capturador-plataformaunica-backend.git \
      --revision 2023-001 \
      --path k8s/desarrollo/capturador-plataformaunica \
      --dest-server https://172.26.58.21:8443 \
      --dest-namespace gestionsaldos \
      --project gestion-saldos \
      --label paseNro=PASEXXX \
      --label version=1.1.0-1 \
      --grpc-web;
      */
      
      
      script.sh "argocd app set ${projectName} --sync-policy none --grpc-web;"
      script.sh "argocd app set ${projectName} --revision ${script.env.BRANCH} --grpc-web;"
      script.sh "argocd app set ${projectName} --sync-policy automated --grpc-web;"
      script.sh "argocd app sync ${projectName}"
      script.sh "argocd app patch ${projectName} --patch '{\"metadata\":{\"labels\":{\"paseNro\":\"${nroPase}\"}}}' --type merge"
    }
  }
}
