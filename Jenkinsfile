pipeline {
    agent any
    
    stages {
        stage("SCM CHECKOUT"){
            steps{
                
                echo "====++++executing SCM CHECKOUT++++===="
                git branch: 'OnsHanafi_ETUDIANT', credentialsId: 'GithubCredentials', url: 'https://github.com/OnsHanafi/Foyer.git'
            }
            post{
                success{
                    echo "====++++SCM CHECKOUT executed successfully++++===="
                }
                failure{
                    echo "====++++SCM CHECKOUT execution failed++++===="
                }
        
            }
        }
        stage("MAVEN Clean&Compile"){
            steps{
                
                echo "====++++executing Build++++===="
                sh "mvn clean compile"
            }
            post{
                success{
                    echo "====++++ maven build executed successfully++++===="
                }
                failure{
                    echo "====++++ maven build execution failed++++===="
                }
        
            }
        }
        stage("Mockito & JaCoCo"){
            steps{
                
                echo "====++++executing Test++++===="
                // sh 'mvn clean package' //Jacoco  
                sh '''
                mvn test
                mvn jacoco:report
                '''
                
                              
            }
            post{
                success{
                    echo "====++++ maven Test executed successfully++++===="
                }
                failure{
                    echo "====++++ maven Test execution failed++++===="
                }
        
            }
        }
        stage("Trivy Scan FS"){
            steps{
                echo "====++++executing TRIVY FS SCAN ++++===="
                sh '''
                    trivy fs --format table -o fs.html .
                '''                
            }
        }
        stage("SONARQUBE"){
            environment{
                SCANNER_HOME = tool 'sonar-scanner'
            }
            steps{
                withSonarQubeEnv('sonarqube-server') {
                    sh '''
                        ${SCANNER_HOME}/bin/sonar-scanner -Dsonar.projectKey=OHP -Dsonar.projectName=OHP \
                        -Dsonar.java.binaries=target \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml 
                    '''
                }
            }
        }
        stage("Maven Package") {
            steps {
                script {
                    sh "mvn package -DskipTests=true"
                }
            }
        }
        stage("Deploy To NEXUS"){
            steps{
                sh '''
                    mvn deploy -DskipTests
                '''
            }
            post{
                success{
                    echo "====++++executed successfully++++===="
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
                }
                failure{
                    echo "====++++execution failed++++===="
                }
            }    
        }
        stage("Build Image"){
            environment{
                ARTIFACT_URL = "http://192.168.1.26:8081/repository/maven-releases/tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar"
            }
            steps{
                echo "Building Docker Image........"
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials',usernameVariable: 'NEXUS_USERNAME',passwordVariable: 'NEXUS_PASSWORD')])
                {
                    sh '''
                        docker build -t onshanafi/foyer-app:5.0.0 \
                        --build-arg NEXUS_USERNAME=\$NEXUS_USERNAME \
                        --build-arg NEXUS_PASSWORD=\$NEXUS_PASSWORD \
                        --build-arg ARTIFACT_URL=\$ARTIFACT_URL . 
                    '''
                }    
            }
        }
        stage("Trivy Scan Image"){
            steps{
                echo "====++++executing TRIVY IMAGE SCAN ++++===="
                sh '''
                    trivy image --timeout 10m  --format table -o trivy-image-report.html onshanafi/foyer:5.0.0
                '''                
            }
        }
        stage("Push Image"){
            steps{
                echo "Pushing Docker Image to DockerHub........"
                script{
                    withDockerRegistry(credentialsId: 'DockerHubCredentials', toolName: 'docker') {
                        sh '''
                            docker push onshanafi/foyer:5.0.0 
                        '''    
                    }    
                }

            }
        }
        stage("Docker Compose"){
            steps{
                echo "Launching Docker Compose........"
                sh '''
                    docker compose up -d
                '''
            }
        }
        stage("Deploy to K8S Cluster"){
            steps{
                echo "Launching Deployment........"
                withKubeConfig(caCertificate: '', clusterName: 'dev-foyer-demo', contextName: '', credentialsId: 'k8s-credentials', namespace: 'dev', restrictKubeConfigAccess: false, serverUrl: 'https://devaks1-lis27v19.hcp.eastus2.azmk8s.io') {
                  sh'''
                        kubectl apply -f ./Deployment/K8S/app-deploy
                        
                  '''
                  sleep 20 
                }
            }
        }
        stage("Verify Deployment"){
            steps{
                echo "Verifying Deployment........"
                withKubeConfig(caCertificate: '', clusterName: 'dev-foyer-demo', contextName: '', credentialsId: 'k8s-credentials', namespace: 'dev', restrictKubeConfigAccess: false, serverUrl: 'https://devaks1-lis27v19.hcp.eastus2.azmk8s.io') {
                  sh'''
                        kubectl get pods -n dev
                        kubectl get deploy -n dev
                        kubectl get svc -n dev
                        
                  '''
                }   
            }
        }
        
    }
    
    post{
        always{
            script{
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
                def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'
                
                def body = """
                    <html>
                        <body>
                            <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                                <h2>${jobName} - Build ${buildNumber}</h2>
                            </div>
                            <div style="background-color: ${bannerColor}; padding: 10px;">
                                <h2 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h2>
                            </div>
                            <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                        </body>
                    </html>
                """
                
                emailext (
                    subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                    body: body,
                    to: 'ons.hanafi2@gmail.com',
                    from: 'jenkins@gmail.com',
                    replyTo: 'jenkins@gmail.com',
                    mimeType: 'text/html',
                    attachementsPattern: '**/trivy-image-report.html',
 
                )
                
                
                
            }
        }
    }
}
