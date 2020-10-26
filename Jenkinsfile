
//git 凭证id
def git_auth = "dcb13c80-af50-422d-acf2-e244089fcf9a"
//git的url地址
def git_url = "https://github.com/dinghaoxiaoqin/boot_project.git"
//镜像的版本号
def tag = "latest"
//harbor 的地址
def harbor_url = "47.94.81.128:8001"
//镜像库的名称
def harbor_project = "bootproject"
//harbor的凭据id
def harbor_auth = "11fe98d8-eb9f-4290-b61b-d8ba573c439c"


node {
  stage('拉取代码'){
     checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
  }
  stage('编译 公共工程 common'){
   sh "mvn -f rrk-common clean install"
  }
  stage('编译 打包微服务工程'){
     echo "编译 微服务工程"

     sh "mvn -f ${boot_docker} clean install"

     echo "打包微服务工程"

     sh "mvn -f ${boot_docker} clean package dockerfile:build"
    }

  stage('上传镜像'){

     //定义镜像的名字
     def imageName = "${boot_docker}:${tag}"
     sh "docker tag ${imageName} ${harbor_url}/${harbor_project}/${imageName}"
     //推送镜像到harbor
     withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
         //登录harbor
         sh "docker login -u ${username} -p ${password} ${harbor_url}"
         //镜像上传到harbor
         sh "docker push ${harbor_url}/${harbor_project}/${imageName}"

         sh "echo 镜像上传成功"
     }
    }

}