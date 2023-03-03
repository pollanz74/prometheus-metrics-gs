# Getting Started with Prometheus and Grafana

### How to build and publish the image to your minikube docker environment

1. Point your shell to minikube's docker-daemon ([Docker Configuration for minikube](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#build-image.examples.docker.minikube)), running the following command to get your :
   ```shell
   eval $(minikube -p minikube docker-env)
   ```
   
1. Update the _**spring-boot-maven-plugin**_ configuration using your minikube environment configuration:
   ```shell
   minikube docker-env
   ```

1. Build and publish the image:
   ```shell
   mvn clean package -P build-image
   ```
   
1. Check on docker:
   ```shell
   docker images
   
   REPOSITORY                                              TAG        IMAGE ID       CREATED         SIZE
   ...
   local/prometheus-metrics-gs                             latest     fd1196e8ae1b   43 years ago    266MB
   ```
1. Deploy on minikube:
   + Preview the kustomization:
      ```shell
      kubectl kustomize kube/overlays/minikube
      ```
   + Apply the kustomization:
      ```shell
      kubectl apply -k kube/overlays/minikube -n appz
      ```
   
### How to add Prometheus and Grafana to your minikube

1. Start minikube:
   ```shell
   minikube start
   ```

1. Add the following namespaces (optional):
   ```shell
   kubectl create ns appz
   kubectl create ns monitoring
   ```

1. Install Prometheus via _**Helm**_ chart:
   + Add prometheus repository:
      ```shell
      helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
      ```
   + Install the Helm chart for Prometheus:
      ```shell
      helm install prometheus prometheus-community/prometheus -n monitoring
      ```
   + Expose the prometheus-server service via NodePort:
      ```shell
      kubectl expose service prometheus-server --type=NodePort --target-port=9090 --name=prometheus-server-np -n monitoring
      ```
     
1. Install Grafana via _**Helm**_ chart:
   + Add prometheus repository:
      ```shell
      helm repo add grafana https://grafana.github.io/helm-charts
      ```
   + Install the Helm chart for Grafana:
      ```shell
      helm install grafana grafana/grafana -n monitoring
      ```
   + Expose the prometheus-server service via NodePort:
      ```shell
      kubectl expose service grafana --type=NodePort --target-port=3000 --name=grafana-np -n monitoring
      ```
   + Retrieve the admin password:
      ```shell
      kubectl get secret -n monitoring grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
      ```
    
1. Update the _**prometheus-server**_ configmap to add the new endpoint:
   ```yaml
   scrape_configs:
    - job_name: prometheus-metrics-gs
      metrics_path: /actuator/prometheus
      static_configs:
        - targets:
            - prometheus-metrics-gs.appz.svc.cluster.local:8080
   ```
#### How to access to the Web UI

1. Prometheus
   ```shell
   minikube service prometheus-server-np --url -n monitoring
   ```
2. Grafana
   ```shell
   minikube service grafana-np --url -n monitoring
   ```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.9/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.9/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#web)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#actuator)
* [Prometheus](https://docs.spring.io/spring-boot/docs/2.7.9/reference/htmlsingle/#actuator.metrics.export.prometheus)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

