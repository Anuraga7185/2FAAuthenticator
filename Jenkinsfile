pipeline {
  agent any

  environment {
    ANDROID_HOME = "/opt/android-sdk"
    GRADLE_OPTS = "-Dorg.gradle.jvmargs=-Xmx4g"
    PATH = "${env.PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools"
  }

  stages {
    stage('Checkout') {
      steps {
        git credentialsId: 'github-token', url: 'https://github.com/Anuraga7185/2FAAuthenticator', branch: 'main'
      }
    }

    stage('Build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew clean assembleRelease'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

    stage('Lint') {
      steps {
        sh './gradlew lint'
      }
    }

    stage('Archive') {
      steps {
        archiveArtifacts artifacts: '**/build/outputs/**/*.apk', allowEmptyArchive: true
      }
    }
  }

  post {
    always {
      echo "Pipeline finished!"
    }
  }
}
