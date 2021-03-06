# Введение к лабам
Большая часть лабораторных работ выполнены с помощью приложения LabsLoader, в котором находятся классы, 
названные соответственно выполненной лабораторной работе. Эти классы реализуют лабы, т.е. могут соответсвующими 
методами, например, собрать снимки контейнеров докера, запустить все необходимые для лабы контейнеры или остановить их.

В свою очередь классы лабораторных работ, используют класс Docker, созданный для упрощения, чтобы не приходилось 
возиться с терминалом. В нём реализован вызов докер-команд из Java-приложения, за счёт метода runCommand (да, 
можно было использовать sh-скрипты, но я сделал как сделал, мне так удобнее).


# Лабораторные по высоконагруженному вебу
## 1. Балансировка нагрузки с Nginx
### О чём эта лаба
Для реализации данной лабы надо было создать простое веб-приложение, содержащее HTTP endpoint, при обращении к
которому возвращается ответ вида {“counter”: “1”} (сделал по-другому: { "WebAppID": 0, "counter": 1 } ). При 
каждом обращении счетчик должен увеличиваться. Далее необходимо запустить несколько экземпляров данного приложения 
и Nginx, который будет балансировать нагрузку между ними.

В данном решении все экземпляры приложения и nginx запускаются в контейнерах Docker.

### Файлы к этой лабе
Директории и файлы, используемые для данной лабы:
- DockerFiles
  - dockerfile_counterApp (для сборки контейрера с нашим веб-приложением)
  - dockerfile_nginxLoadBalancer_Hash (для сборки контейнера с nginx, балансирующего по алгоритму Hash)
  - dockerfile_nginxLoadBalancer_IpHash (для сборки контейнера с nginx, балансирующего по алгоритму IP Hash)
  - dockerfile_nginxLoadBalancer_LeastConn (для сборки контейнера с nginx, балансирующего по алгоритму Least Connections)
  - dockerfile_nginxLoadBalancer_LeastTime (для сборки контейнера с nginx, балансирующего по алгоритму Least Time)
  - dockerfile_nginxLoadBalancer_Random (для сборки контейнера с nginx, балансирующего по алгоритму Random)
  - dockerfile_nginxLoadBalancer_RoundRobin (для сборки контейнера с nginx, балансирующего по алгоритму Round Robin)
- JARs
  - SimpleSpringBootApp.jar (JAR с нашим веб-приложением)
- NginxConfigFiles
  - nginxLoadBalancer_Hash.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Hash)
  - nginxLoadBalancer_IpHash.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму IP Hash)
  - nginxLoadBalancer_LeastConn.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Least Connections)
  - nginxLoadBalancer_LeastTime.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Least Time)
  - nginxLoadBalancer_Random.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Random)
  - nginxLoadBalancer_RoundRobin.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Round Robin)
- SimpleSpringBootApp (папка с maven-проектом веб-приложения, из которого получили JARник)

### Как проверить эту лабу
Чтобы запустить контейнеры с экземплярами веб-приложения и Nginx-ом, нужно всего лишь прописать в main у LabsLoader-a

    LabHighLoad1.prepare();
    LabHighLoad1.start();

и запустить его. Он создаст и запустит 4 контейнера с веб-приложением и 1 с nginx.

Чтобы остановить контейнеры, нужно прописать в main у LabsLoader-a

    LabHighLoad1.stop();

и запустить проект.

Для выбора способа балансировки нужно раскомменить соответствующую строку в LabsHighLoad1.start(){}, взамен той, что 
используется.

## 2. Разделяемое хранилище данных с использованием Redis
### О чём эта лаба
В рамках данной лабы было нужно доработать так приложение из предыдущей лабораторной, чтобы счётчик входящих 
запросов хранился в Redis и, таким образом, был общим.

### Файлы к этой лабе
Директории и файлы, используемые для данной лабы:
- DockerFiles
  - dockerfile_counterAppRedis (для сборки контейрера с нашим веб-приложением)
  - dockerfile_nginxLoadBalancer_Random (для сборки контейнера с nginx, балансирующего по алгоритму Random)
  - docker-compose_redis.yml
- JARs
  - CounterAppForRedis.jar (JAR с нашим веб-приложением)
- NginxConfigFiles
  - nginxLoadBalancer_Random.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Random)
- CounterAppForRedis (папка с maven-проектом веб-приложения, из которого получили JARник)

### Как проверить эту лабу
Чтобы подготовить все докер-снимки, нужно всего лишь прописать в main у LabsLoader-a

    LabHighLoad2.prepare();
    
и запустить его.

Чтобы запустить все контейнеры, нужно поменять запускаемый метод на

    LabHighLoad2.start();
    
Данный метод запустит командную строку для docker-compose.

Чтобы остановить контейнеры, нужно прописать в main у LabsLoader-a

    LabHighLoad2.stop();

и запустить проект.

## 3. Балансировки нагрузки с использованием очередей Kafka
### О чём эта лаба 
В рамках данной лабораторной работы необходимо было реализовать 2 приложения: одно неравномерно загружает данные в
очередь Kafka-топика, а другой с определённой задержкой берёт данные из очереди.

В данной работе для создания Kafka-сервера использовался docker-compose.yml от Confluence.

### Файлы к этой лабе
Директории и файлы, используемые для данной лабы:
- DockerFiles
  - docker-compose_kafkaConfluence.yml
- KafkaConsumer (приложение, которое будет читать из очереди) 
- KafkaProducer (приложение, которое будет писать в очередь)

### Как проверить эту лабу

1. Запустите кафка-сервер с помощью docker-compose_kafkaConfluence.yml;
2. Запустите KafkaConsumer и KafkaProducer;
3. Наблюдайте, как пишутся и считываются сообщения из очереди.

## 4. Реализация партиционирования с использованием Postgres
### О чём эта лаба

В данной работе нужно было создать разделённую таблицу и написать запросы создания/удаления/добавления таблицы/партиций
создания глобальных и локатьных индексов, выборки данных

### Файлы к этой лабе

- SQLfiles
  - requests.sql

### Как проверить эту лабу

Отправьте запросики из файла к своей БД. Результат будет примерно таким же как на картинке

![](/ImagesForReadme/postgresResults.png)

# Лабораторные по облаку
## 1. Запуск приложения с использованием Docker
### О чем эта лаба
В рамках данной лабы было нужно создать веб-приложение, а затем поместить его в докер-снимок, на основе которого 
запустить несколько контейнеров, используя docker-compose.

За основу была взята реализация первой лабы по высоконагруженному вебу. Просто был изменён метод start() у класса
данной лабы.

### Файлы к этой лабе
Директории и файлы, используемые для данной лабы:
- DockerFiles
  - dockerfile_counterApp (для сборки контейрера с нашим веб-приложением)
  - dockerfile_nginxLoadBalancer_Random (для сборки контейнера с nginx, балансирующего по алгоритму Random)
  - docker-compose_cloudLaba1.yml
- JARs
  - SimpleSpringBootApp.jar (JAR с нашим веб-приложением)
- NginxConfigFiles
  - nginxLoadBalancer_Random.conf (конфигурационный файл nginx, в котором определена балансировка по алгоритму Random)
- SimpleSpringBootApp (папка с maven-проектом веб-приложения, из которого получили JARник)

### Как проверить эту лабу
Чтобы подготовить все докер-снимки, нужно всего лишь прописать в main у LabsLoader-a

    LabCloud1.prepare();
    
и запустить его.

Чтобы запустить все контейнеры, нужно поменять запускаемый метод на

    LabCloud1.start();
    
Данный метод запустит командную строку для docker-compose.

Чтобы остановить контейнеры, нужно прописать в main у LabsLoader-a

    LabCloud1.stop();

и запустить проект.

## 2. Реализация веб-приложение с использованием протокола S3 
## + 
##3. Реализация веб-приложения с технологий computer vision
### О чем эти лабы

Для реализации лабораторной работы 2 требуется:
- Создать ресурс, поддерживающий протокол S3 у любого облачного провайдера
- Реализовать веб-приложение, которое позволяет:
  - Работать с bucket: создавать, удалять, просматривать список
  - Работать с данными в bucker: создавать, удалять, просматривать данные в bucket

Для реализации 3й лабораторной работы требуется создать веб-приложение, позволяющее:
- Загрузить изображение
- Отобразить информацию о находящихся объектах на изображении

### Файлы к этой лабе

Всё, что лежит в папке ImageWebApp

### Что сделано

Сделан веб сервис который позволяет создавать\удалять бакеты, просматривать в них изображения, 
загружать/удалять их, а также с помощью машинного зрения картинкам могут автоматически подбираться соответствующие 
тэги или типо того.

![](/ImagesForReadme/135557.png)

![](/ImagesForReadme/135710.png)


## 4. Реализация запуска приложения в Kubernetes

Необходимо реализовать запуск приложение в Kubernetes с использованием
облачного провайдера. В исключительных случаях можно запустить Kubernetes
локально.

### Файлы к этой лабе

В качестве запускаемого приложения было использовано веб-приложение со счётчиком 
обращений из самой первой лабораторной работы, за тем лишь исключением, что при создании 
снимка использовался немного изменённый dockerfile.
- DockerFiles
  - dockerfile_counterAppForKubernetes
  
Также полученный снимок доступен из Docker Hub: romanovns/kubernetesapp

Yaml-файлы от проекта

- KubernetesYamls
  - pod.yaml
  - service.yaml


### Что сделано
После установки minikube и kubectl, проверим, будет ли работать наш снимок в кластере 
kubernetes, для начала локально.

Запустим minikube

![](/ImagesForReadme/minikubeStart.png)

И создадим "под" с нашим контейнером

![](/ImagesForReadme/localPodCreation.png)

Присвоим нашему приложению внешний IP и порт и попытаемся получить к нему доступ 
с помощью curl

![](/ImagesForReadme/localSuccess.png)

Также проверим в браузере

![](/ImagesForReadme/localSuccessInBrowser.png)

Сразу же сохранил yaml-файлы, от нашего pod-a и service-a.



   



