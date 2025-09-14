package sharedlib

class DockerJenkinUtils extends BaseUtil {

  public DockerJenkinUtils(script, String type = ''){
    super(script, type)
    this.script.steps.echo "**** ${script}"
    //printMessage('**** DockerJenkinUtils ****')
  }

  public build(Map config = [:]) {
    
    sh 'echo Hi From DevOps Team'
    printMessage("${config.projectName}")
    printMessage("${config.version}")
    //echo gitAuthorName()//other groovy
  }
}
