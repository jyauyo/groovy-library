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
    
     this.script.steps.writeFile file: 'Dockerfilea', text:"""
     FROM eclipse-temurin:21-jdk-alpine
     ADD ${params.jarName} /app/service.jar
     WORKDIR /app
     ENTRYPOINT ["java", "-jar", "/app/service.jar"]
     """

    def docker_registry_environment_ = "${script.env.DOCKER_REGISTRY_ENVIRONMENT}"
    def docker_registry_complete = "${script.env.DOCKER_REGISTRY}"
    printMessage("***** Docker Registry Final: ${docker_registry_complete}");
    //echo gitAuthorName()//other groovy
  }
}
