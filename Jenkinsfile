
//git 凭证id
def git_auth = "dcb13c80-af50-422d-acf2-e244089fcf9a"
//git的url地址
def git_url = "https://github.com/dinghaoxiaoqin/boot_project.git"
//镜像的版本号
def tag = "latest"
//harbor 的地址
//def harbor_url = "47.94.81.128:8001"
//阿里云镜像仓库地址
def aliyun_url = "registry.cn-hangzhou.aliyuncs.com/dhqxq/dockerdepository"
//镜像库的名称
//def harbor_project = "bootproject"
//阿里云镜像库名称
def aliyun_project = "dockerdepository"
//harbor的凭据id
//def harbor_auth = "11fe98d8-eb9f-4290-b61b-d8ba573c439c"
//阿里云仓库的凭据id
del aliyun_auth = "0c9d941e-c2f6-44b8-a8a5-abfc3a985340"
//构建的微服务名称
def boot_name = ""
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
     def name = "${boot_docker}"
     def imageName = ""
     if(name == "boot-auth" || name == "boot-gateway" || name == "rrk-file" || name== "manage-gateway"){
        echo "${boot_docker}:${tag}"
        imageName = name+":${tag}"
        boot_name = name
     } else{
         String[] splitName =  name.tokenize('/')
        // echo "分组的数组："+splitName
        // echo "获取第二个值："+splitName[1]
          imageName = splitName[1]+":${tag}"
          boot_name = splitName[1]
     }
     //定义镜像的名字
     sh "docker tag ${imageName} ${aliyun_url}/${aliyun_project}/${imageName}:${tag}"
     //推送镜像到harbor
     withCredentials([usernamePassword(credentialsId: "${aliyun_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
      //登录阿里云
     sh "docker login --username=${username} registry.cn-hangzhou.aliyuncs.com"
      //镜像上传到阿里云仓库
     sh "docker push ${aliyun_url}/${aliyun_project}/${imageName}:${tag}"

     sh "echo 镜像上传成功"
        }
      //服务部署
       sshPublisher(publishers: [sshPublisherDesc(configName: "master_192.168.248.102", transfers: [sshTransfer(cleanRemote: false, excludes: "", execCommand: "/opt/jenkins_shell/deploy.sh $harbor_url $harbor_project $boot_name $tag $port", execTimeout: 960000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: "[, ]+", remoteDirectory: "", remoteDirectorySDF: false, removePrefix: "", sourceFiles: "")], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
    }

}