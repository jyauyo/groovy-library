package sharedlib

class DockerJenkinUtils extends BaseUtil {

  public DockerJenkinUtils(script, String type = ''){
    super(script, type)
    this.script.steps.echo "**** ${script}"
    //printMessage('**** DockerJenkinUtils ****')
  }

  public build(Map params) {
    
    //sh 'echo Hi From DevOps Team'
    printMessage("${params.projectName}")
    printMessage("${params.version}")

    printMessage("***** Creating Dockerfile")
    
    def jarName = script.steps.sh(script: "ls target/*.jar | head -1", returnStdout: true).trim()
    
     this.script.steps.writeFile file: 'Dockerfile', text:"""
     FROM eclipse-temurin:21-jdk-alpine
     ADD ${jarName} /app/service.jar
     WORKDIR /app
     ENTRYPOINT ["java", "-jar", "/app/service.jar"]
     """

    def docker_registry_environment_ = "${script.env.DOCKER_REGISTRY_ENVIRONMENT}"
    def docker_registry_complete = "${script.env.DOCKER_REGISTRY}"
    printMessage("***** Docker Registry Final: ${docker_registry_complete}");



    def dockerfile = 'Dockerfile'
   def customImage = script.docker.build("${docker_registry_complete}/${params.projectName}:${params.version}", "-f ${dockerfile} .")

   withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS_ID}", usernameVariable: 'dockerHubUser', passwordVariable: 'dockerHubPassword')]){

       script.echo  "${script.env.dockerHubPassword} | login --username ${script.env.dockerHubUser} --password-stdin  ${script.env.DOCKER_URL}"                   
       //sh "docker push ${env.DOCKER_REGISTRY}${env.DOCKER_REGISTRY_ENVIRONMENT}/app-microservice:${APP_VERSION}"
   }
   printMessage("***** Publishing to Docker Registry: ${APP_VERSION}")
   customImage.push()
    
    //echo gitAuthorName()//other groovy
  }
}
