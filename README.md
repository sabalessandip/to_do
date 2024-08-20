# Deployment Guide

This guide provides detailed instructions on how to deploy the Three-Tier To-Do/Task Manager application using various deployment methods. The application comprises four key components: **Frontend (Flutter Web)**, **API Gateway (Nginx)**, **Backend (Spring Boot)**, and **Database (MySQL)**.

## Clone the Repository
Before starting any deployment, clone the repository to your local machine

```sh
git clone https://github.com/sabalessandip/to_do
```

Navigate to the root folder of the cloned repository

```sh
cd to_do
```

## Deployment Using Docker Compose

**1. Update API Path**
<br>
<br>
Modify the ```baseUrl``` in ```api_service.dart``` within the frontend to point to the Docker Compose environment.
```Dart
static const String baseUrl = 'http://localhost:82';
```

**2. Package the Application**
<br>
<br>
Navigate to the cloned folder and run the following command to package the frontend and backend
```sh
sh build_all.sh
```

**3. Build and Run Docker Containers**
```sh
docker-compose up -d
```

**4. Access the Application**
<br>
<br>
Launch your browser in disabled web security mode to prevent CORS errors. On Mac, use the following command for chrome:
```sh
open -na Google\ Chrome --args --user-data-dir=<path> --disable-web-security
```

The application will be accessible at http://localhost:81.


**Clean the Deployment**
```sh
docker-compose down
```

## Deployment on Amazon ECS

### Create Infrastructure Using CloudFormation
Navigate to ```aws-ecs``` directory to use CloudFormation templates to set up the necessary infrastructure. Wait for each stack to complete before proceeding to the next step.

**1. Create VPC**

```sh
aws cloudformation create-stack \
    --stack-name three-tier-app-vpc  \
    --template-body file://three-tier-app-vpc.yaml
```

**2. Create Security Groups**
<br>
<br>
Refer to the ```VPCId``` output from the ```three-tier-app-vpc``` stack.

```sh
aws cloudformation create-stack \
    --stack-name three-tier-app-sec-groups  \
    --template-body file://three-tier-app-security-groups.yaml \
    --parameters ParameterKey=VPCId,ParameterValue=<vpcid>
```

**3. Create Load Balancers**
<br>
<br>
Refer to the outputs of ```three-tier-app-vpc``` and ```three-tier-app-sec-groups``` stacks to populate the parameters in ```alb-parameters.json```.

```sh
aws cloudformation create-stack \
    --stack-name three-tier-app-albs \
    --template-body file://three-tier-app-alb.yaml \
    --parameters file://alb-parameters.json
```

**4. Update API Path**
<br>
<br>
Modify the ```baseUrl``` in ```api_service.dart``` in the frontend with the Gateway ALB’s DNS name.

```dart
static const String baseUrl = 'http://<gateway-alb-dns-name>';
```

**5. Package the Application**
<br>
<br>
Navigate to the cloned folder and run the following command to package the frontend and backend
```sh
sh build_all.sh
```

**6. Create ECR Repositories and Task Definitions**
<br>
<br>
Refer to the outputs of ```three-tier-app-vpc```, ```three-tier-app-albs```, and ```three-tier-app-sec-groups``` stacks to populate the parameters in ```taskdef-parameters.json```.

```sh
aws cloudformation create-stack \
    --stack-name three-tier-app-taskdef \
    --template-body file://three-tier-app-repo-taskdef.yaml \
    --capabilities CAPABILITY_NAMED_IAM
```

Push the Docker images for the database, backend, gateway, and frontend from their respective folders using the commands provided in each ECR repository.

**7. Create Cluster and Services**
<br>
<br>
Initially, the services are created with zero deployments. Once the stack is successfully created, update the services with the desired count.

```sh
aws cloudformation create-stack \
    --stack-name three-tier-app-cluster \
    --template-body file://three-tier-app-cluster.yaml \
    --parameters file://cluster-parameters.json
```

**Access the Application**
<br>
<br>
The application will be accessible at http://[frontend-alb-dns-name].

**Clean the Deployment**
<br>
<br>
Delete the CloudFormation stacks to remove the AWS resources.

## Deployment Using Minikube

**1. Start Minikube**
```sh
minikube start
```

**2. Enable Required Add-ons**
```sh
minikube addons enable metrics-server
minikube addons enable ingress
minikube addons enable ingress-dns
```

**3. Start Minikube Dashboard**
```sh
minikube dashboard
```

**4. Connect Docker Client to Minikube**
<br>
<br>
Run the following command to connect the local Docker client to the Minikube Docker client, allowing you to build images locally without pushing them to a public repository. Note that this connection is specific to a terminal session.
```sh
eval $(minikube docker-env)
```

**5. Update API Path**
<br>
<br>
Uncomment the ```baseUrl``` in ```api_service.dart``` from the frontend with the minikube Gateway URL.

**6. Package the Application**
<br>
<br>
Run the following command to package the frontend and backend
```sh
sh build_all.sh
```

**7. Build docker images**
<br>
<br>
Run the following command to build the docker images for database, backend, gateway and frontend
```sh
sh build_all_images.sh
```

Navigate to ```local-k8s``` folder,

**8. Create Namespace**
```sh
kubectl apply -f namespace.yaml
```

**9. Set Namespace as Default**
```sh
kubectl config set-context --current --namespace=three-tier-app
```

**10. Deploy Config Maps**
```sh
kubectl apply -f config.yaml
```

**11. Create Secrets**
```sh
kubectl apply -f secretes.yaml
```

**12. Deploy Database**
```sh
kubectl apply -f db-deployment.yaml
kubectl apply -f db-service.yaml
```

**13. Deploy Backend**
```sh
kubectl apply -f backend-deployment.yaml
kubectl apply -f backend-service.yaml
```

**14. Deploy API Gateway**
```sh
kubectl apply -f gateway-deployment.yaml
kubectl apply -f gateway-service.yaml
kubectl apply -f gateway-ingress.yaml
```

**15. Deploy Frontend**
```sh
kubectl apply -f frontend-deployment.yaml
kubectl apply -f frontend-service.yaml
kubectl apply -f app-ingress.yaml
```

**16. Modify ```/etc/hosts``` File**
```sh
sudo vi /etc/hosts
```

Add the following line

```sh
127.0.0.1 three-tier-app.local
```

**17. Run Minikube Tunnel**
<br>
<br>
In a separate terminal session, run:
```sh
minikube tunnel
```

**Access the Application**
<br>
<br>
The application will be accessible at http://three-tier-app.local.

**Clean the Deployment**
<br>
<br>
Run the following command to clean up the Minikube cluster
```sh
minikube delete --all
```

## Deployment on Amazon EKS

**1. Create EKS Cluster**
```sh
eksctl create cluster \
  --name three-tier-app-cluster \
  --region ap-south-1 \
  --fargate
```

**2. Create EC2 Node Group**
<br>
<br>
To accommodate add-ons like CoreDNS that require EC2 instances
```sh
eksctl create nodegroup \
  --cluster three-tier-app-cluster \
  --region ap-south-1 \
  --name three-tier-app-nodegroup \
  --node-type t2.medium \
  --nodes 2 \
  --nodes-min 1 \
  --nodes-max 2 \
  --managed
```

**3. Configure kubectl**
<br>
<br>
Configure kubectl to connect to the newly created cluster
```sh
aws eks --region ap-south-1 update-kubeconfig \
    --name three-tier-app-cluster
```

Navigate to ```aws-k8s``` folder,

**4. Install AWS Load Balancer Controller**
<br>
<br>
Follow the AWS guide to set up the AWS Load Balancer Controller, which is required for ALB ingress resources for the gateway and frontend.

(AWS Load Balancer Controller Guide)[https://repost.aws/knowledge-center/eks-alb-ingress-controller-fargate]

**5. Create the ECR repositories for frontend, gateway, backend and database**

```sh
aws ecr create-repository \
    --repository-name three-tier-app-frontend \
    --region ap-south-1 \
    --image-scanning-configuration \
    scanOnPush=false \
    --image-tag-mutability MUTABLE

aws ecr create-repository \
    --repository-name three-tier-app-gateway \
    --region ap-south-1 \
    --image-scanning-configuration \
    scanOnPush=false \
    --image-tag-mutability MUTABLE

aws ecr create-repository \
    --repository-name three-tier-app-backend \
    --region ap-south-1 \
    --image-scanning-configuration \
    scanOnPush=false \
    --image-tag-mutability MUTABLE

aws ecr create-repository \
    --repository-name three-tier-app-database \
    --region ap-south-1 \
    --image-scanning-configuration \
    scanOnPush=false \
    --image-tag-mutability MUTABLE
```

**6. Build and push the docker images for gateway, backend and database from respective folders using push command specified in ECR repositories ```View push commands``` section.**

**7. Deploy the Application**
<br>
<br>
Apply the manifest files to create the database, backend, and gateway deployments, following the steps from 8 to 14 as in the Minikube deployment above.

**8. Retrieve Gateway Ingress DNS**
```sh
kubectl describe ingress gateway-ingress
```

**9. Update Frontend API Path**
<br>
<br>
Modify the ```baseUrl``` in ```api_service.dart``` in the frontend with the Gateway ingress DNS name
```dart
static const String baseUrl = 'http://<gateway-ingress-dns>';
```

**10. Deploy Frontend**
<br>
<br>
Create and push the frontend image to the ECR repository, then apply the frontend manifests as shown following the step 15 from the Minikube deployment above.

**11. Retrieve Frontend Ingress DNS**
```sh
kubectl describe ingress frontend-ingress
```

**Access the Application**
<br>
<br>
Launch the browser in disabled web security mode and Test the application.

The application will be accessible at http://[frontend-ingress-dns].

**Clean the Deployment**
<br>
<br>
Delete the CloudFormation stacks to remove the AWS resources.
