FROM ubuntu

# Update Ubuntu package manager and install dependencies
RUN apt-get update && apt-get install -y curl wget gnupg2

# Install JDK 17
RUN apt-get install -y openjdk-17-jdk

# Install Maven 3.8.6
RUN curl -fsSL https://apache.osuosl.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz | tar xvz -C /usr/local && \
    ln -s /usr/local/apache-maven-3.8.8/bin/mvn /usr/local/bin/mvn

RUN chmod +x /usr/local/apache-maven-3.8.8/bin/mvn

# Copy pom.xml to the container
COPY pom.xml /usr/src/mymaven/
COPY src /usr/src/mymaven/src

# Install dependencies
RUN mvn -f /usr/src/mymaven/pom.xml dependency:resolve


RUN apt-get install -y wget xvfb unzip

# Set up the Chrome PPA
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list

# Update the package list and install chrome
RUN apt-get update -y
RUN apt-get install -y google-chrome-stable

# Set up Chromedriver Environment variables
ENV CHROMEDRIVER_VERSION 2.19
ENV CHROMEDRIVER_DIR /chromedriver
RUN mkdir $CHROMEDRIVER_DIR

# Download and install Chromedriver
RUN wget -q --continue -P $CHROMEDRIVER_DIR "http://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip"
RUN unzip $CHROMEDRIVER_DIR/chromedriver* -d $CHROMEDRIVER_DIR

# Put Chromedriver into the PATH
ENV PATH $CHROMEDRIVER_DIR:$PATH

CMD ["mvn", "clean", "package"]