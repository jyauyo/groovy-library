package sharedlib

class DockerJenkinUtils {

  public build(Map config) {
    sh 'echo Hi From DevOps Team'
    echo "${config.projectName}"
    echo "${config.version}"
    //echo gitAuthorName()//other groovy
  }
}
