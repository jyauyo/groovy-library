package sharedlib

abstract class BaseUtil {
  protected script
  protected String type
  protected String openJdkJava = "eclipse-temurin:21-jdk-alpine"
  protected def currentCredentialsId
  protected String sonarqubeurl
  protected def projectName
  protected def nroPase
  protected def APP_VERSION
  
  protected BaseUtil() {}
  
  protected BaseUtil(script, String type = '') {
    this.script = script
    this.type =  type
  
    //def remoteConfigs =  this.script.scm.getUserRemoteConfigs()
    //for (Object remoteConfig : remoteConfigs) {
      //this.currentCredentialsId = remoteConfig.getCredentialsId()
      //this.script.steps.echo "******** currentCredentialsId for ${remoteConfig}: ${currentCredentialsId}"
    //}
  }
  
  public void prepare() {
    this.sonarqubeurl = "";

    this.projectName = this.script.scm.getUserRemoteConfigs()[0].getUrl().tokenize('/').last().split("\\.")[0]
    this.script.echo("***** Project Name: ${projectName}");
    
    def pom = this.script.readMavenPom file: 'pom.xml'
    this.nroPase = pom.properties.nroPase
    this.script.echo "***** NroPase: ${nroPase}"
    
    this.APP_VERSION = this.script.sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
    this.script.echo "***** Version: ${APP_VERSION}"
    
  }
  
  protected void printMessage(String message) {
    this.script.echo "[DEVOPS] ${message}"
  }
}
