package sharedlib

abstract class BaseUtil {
  protected script
  protected String type
  protected String openJdkJava = "eclipse-temurin:21-jdk-alpine"
  protected def currentCredentialsId
  protected String sonarqubeurl
  
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
  }
  
  protected void printMessage(String message) {
    this.script.steps.echo "[DEVOPS] ${message}"
  }
}
