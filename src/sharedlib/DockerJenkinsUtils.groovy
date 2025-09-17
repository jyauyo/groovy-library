package sharedlib

class DockerJenkinsUtils extends BaseUtil {

  public DockerJenkinsUtils(script, String type = ''){
    super(script, type)
  }

  public build() {
    script.echo("***** Creating Dockerfile - DockerJenkinsUtils")
    
    //sh 'echo Hi From DevOps Team'
    //printMessage("${params.projectName}")
    //printMessage("${params.version}")   
    
    def jarName = script.steps.sh(script: "ls target/*.jar | head -1", returnStdout: true).trim()
    
     this.script.steps.writeFile file: 'Dockerfile', text:"""
     FROM ${openJdkJava}
     ADD ${jarName} /app/service.jar
     WORKDIR /app
     ENTRYPOINT ["java", "-jar", "/app/service.jar"]
     """

    def docker_registry_environment_ = "${script.env.DOCKER_REGISTRY_ENVIRONMENT}"
    def docker_registry_complete = "${script.env.DOCKER_REGISTRY}"
    printMessage("***** Docker Registry Final: ${docker_registry_complete}");

    def dockerfile = 'Dockerfile'
   def customImage = script.docker.build("${docker_registry_complete}/${projectName}:${version}", "-f ${dockerfile} .")

   script.withCredentials([script.usernamePassword(credentialsId: "${script.env.DOCKER_CREDENTIALS_ID}", usernameVariable: 'dockerHubUser', passwordVariable: 'dockerHubPassword')]){
     script.echo "${script.env.dockerHubPassword} | login --username ${script.env.dockerHubUser} --password-stdin  ${script.env.DOCKER_URL}"
     printMessage("***** Publishing to Docker Registry: ${version}")
     customImage.push()
   }
  }
}
