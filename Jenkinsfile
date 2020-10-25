
//git 凭证id
def git_auth = "dcb13c80-af50-422d-acf2-e244089fcf9a"
//git的url地址
def git_url = "https://github.com/dinghaoxiaoqin/boot_project.git"


node {
  stage('拉取代码'){
     checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
  }
  stage('编译 公共工程 common'){
   sh "mvn -f rrk-common clean install"
  }
   stage('编译 打包微服务工程'){
     sh "mvn -f boot-auth clean install"
    }

}