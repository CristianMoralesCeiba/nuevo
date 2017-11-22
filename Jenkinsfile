#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }

    stage('install tools') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-yarn -DnodeVersion=v6.11.4 -DyarnVersion=v1.2.1"
    }

    stage('yarn install') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn"
    }

	stage('docker') {
		sh "./mvnw package dockerfile:build -DskipTests"
        sh "docker-compose -f '${workspace}\\src\\main\\docker\\app.yml' up -d"
		sleep (125000)
		sh "jmeter.bat -n -t  '${workspace}\\prueba.jmx' -l testJmeter.jtl"
		sh "docker stop (docker ps -aq)"
    }

    stage('backend tests') {
        try {
            sh "./mvnw test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        try {
            sh "./mvnw com.github.eirslett:frontend-maven-plugin:yarn -Dfrontend.yarn.arguments=test"
        } catch(err) {
            throw err
        } finally {
            junit '**/target/test-results/karma/TESTS-*.xml'
        }
    }

    stage('packaging') {
        sh "./mvnw package -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }

    stage('quality analysis') {
        withSonarQubeEnv('Sonar') {
            sh "./mvnw sonar:sonar \
				-Dsonar.host.url=https://sonarcloud.io \
				-Dsonar.organization=cristianmoralesceiba-github \
				-Dsonar.login=293000f651ed0f95823ff7a9d5e177bf9e91d63c"
        }
    }
}
