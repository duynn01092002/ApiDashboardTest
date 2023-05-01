FROM amazonlinux:latest

RUN yum update -y && \
    yum install -y java-17-amazon-corretto-devel

RUN wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz && \
    tar xvf apache-maven-3.8.8-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.8.8 /opt/maven && \
    echo "export M2_HOME=/opt/maven" > /etc/profile.d/maven.sh && \
    echo "export PATH=${M2_HOME}/bin:${PATH}" >> /etc/profile.d/maven.sh && \
    chmod +x /etc/profile.d/maven.sh && \
    source /etc/profile.d/maven.sh

COPY . /app
WORKDIR /app

RUN mvn install

CMD ["mvn", "clean", "test"]