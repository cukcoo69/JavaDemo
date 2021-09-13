# Rabbitmq_Demo
- To run the project, we need a rabbit server.
- There is several ways to create like: use docker, create an instance in web (https://www.cloudamqp.com/plans.html)
or download rabbitmq.exe file(I think).
- You can use a way I use to create a rabbit server by docker like me by run this command:
docker run -d --name rabbit-demo-2 -p 5672:5672 -p 15672:15672 rabbitmq:management
- You can create an instance by this tutorial: https://topdev.vn/blog/gioi-thieu-cloudamqp-mot-rabbitmq-server-tren-cloud/