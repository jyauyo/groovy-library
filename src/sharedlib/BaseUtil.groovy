package sharedlib

abstract class BaseUtil {
  protected script
  protected String type
  protected String openJdkJava = "eclipse-temurin:21-jdk-alpine"
  protected def currentCredentialsId
  String sonarqubeurl
  def projectName
  def nroPase
  def version
  
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
    printMessage("***** Project Name: ${projectName}");

    def pom = this.script.readMavenPom file: 'pom.xml'
    this.nroPase = pom.properties.nroPase
    
    script.env.NRO_PASE = this.nroPase
    this.script.writeFile file: "${nroPase}.txt", text:"""${nroPase}"""
    
    printMessage("***** NroPase: ${script.env.NRO_PASE}")
    
    //this.version = this.script.sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
    this.version = this.script.readMavenPom().getVersion()
    printMessage("***** Version: ${version}")
    
  }
  
  protected void printMessage(String message) {
    this.script.steps.echo "[DEVOPS] ${message}"
  }
}
