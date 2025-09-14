package sharedlib

class DockerJenkinUtils {

  public DockerJenkinUtils(script, String type = ''){
    echo '**** DockerJenkinUtils ****'
  }

  public build(Map config) {
    sh 'echo Hi From DevOps Team'
    echo "${config.projectName}"
    echo "${config.version}"
    //echo gitAuthorName()//other groovy
  }
}
