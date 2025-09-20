def call() {
      return {
        docker {
            image 'jyauyor/maven-argocd-jdk21:1.0.4'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.m2:/var/maven/.m2:z -e MAVEN_CONFIG=/var/maven/.m2 -e MAVEN_OPTS=-Duser.home=/var/maven'
        }
    }
}
