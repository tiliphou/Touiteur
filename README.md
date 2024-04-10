This is a naive implementation of a [CMS](https://en.wikipedia.org/wiki/Content_management_system). Its primary intent is to serve as an optimisation training.

The actual project is in the anti-demo folder. The testbench projet is a Selenium test taking advantage of the [metrologie tool](https://gitlab.cluster.ensisa.uha.fr/ecoconception/metrologie) for mesuring resource consumption during development.

First step is to deploy your system into Docker, using docker compose and the file anti-demo/docker-compose.yaml.

In order for the metrology tool to work you need to set two environment variables (including in case you're using Gitlab-CI):
* DOCKER_HOST : the address at which the docker server is available (e.g. tcp://192.168.0.10:2375) ; not needed for local daemon ; in the supplied Gitlab-CI file, this variable is set by the DOCKER_REMOTE_HOST environment variable you can define in [Gitlab-CI settings](https://docs.gitlab.com/ee/ci/variables/#define-a-cicd-variable-in-the-ui)
* POWERSPY_HOST : the address at which an [energy sensor](https://gitlab.cluster.ensisa.uha.fr/ecoconception/powerspy-java) is available

To run testbench, just cd into it and run `mvn test`.
 
this the test for everyone
