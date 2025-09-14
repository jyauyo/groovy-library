package sharelib

protected script;
protected script;
protected String type;

abstract class BaseUtil {
  
    protected String openJdkJava = "open-jdk-21"
    protected def currentCredentialsId

    protected BaseUtil() {}
    
    protected BaseUtil(script, String type = '') {
      this.script = script
      this.type =  type
    
      def remoteConfigs =  this.script.scm.getUserRemoteConfigs()
      for (Object remoteConfig : remoteConfigs) {
        this.currentCredentialsId = remoteConfig.getCredentialsId()
        printMessage("currentCredentialsId for ${remoteConfig}: ${currentCredentialsId}")
      }
    
      protected void printMessage(String message) {
        this.script.steps.echo "[DEVOPS] ${message}"
      }
    }
  }
