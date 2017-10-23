## Implementing logs aggregator

### The big picture

image

### Oveview

Top logger services by slant
[~best-log-aggregation-monitoring-tools](https://www.slant.co/topics/326/~best-log-aggregation-monitoring-tools)

Recomended video
[logging: logstash and other things - Jordan Sissel of DreamHost - PuppetConf '12](https://www.youtube.com/watch?v=RuUFnog29M4)

### Ingredients

* server
* java repo
* elasticsearch repo

### Create test environment

Using *Xenial* via *Vagrant* vm and puppet

    apt -q -y install puppet
    apt-mark -q hold puppet puppet-common

### install java (OPTIONAL)

    sudo add-apt-repository -y ppa:webupd8team/java
    sudo /usr/bin/apt-get -q -y update
    sudo echo 'oracle-java8-installer shared/accepted-oracle-license-v1-1 select true' | sudo /usr/bin/debconf-set-selections
    sudo /usr/bin/apt-get install -q -y oracle-java8-installer
    sudo update-java-alternatives -s java-8-oracle

### Using logstash with elasticsearch

Setup elasticsearch repositories

    wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
    sudo apt-get install apt-transport-https
    echo "deb https://artifacts.elastic.co/packages/5.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-5.x.list
    sudo apt-get update

https://www.elastic.co/guide/en/elasticsearch/reference/current/deb.html
https://www.elastic.co/guide/en/elasticsearch/reference/master/heap-size.html
    
    sudo apt-get install elasticsearch
    vi /etc/elasticsearch/elasticsearch.yml
    vi /etc/elasticsearch/jvm.options
        -Xms512m
        -Xmx1g
    sudo systemctl start elasticsearch.service
    
Easy way to monitor service activity

    sudo journalctl -f
    sudo journalctl --unit elasticsearch --since  "2016-10-30 18:17:16"
    
https://www.elastic.co/guide/en/logstash/current/installing-logstash.html#package-repositories

    sudo apt-get install logstash

https://www.elastic.co/guide/en/logstash/current/running-logstash.html#running-logstash-systemd

    vi /etc/logstash/logstash.yml
    sudo systemctl start logstash.service

Test elasticsearch endpoint

    curl -XGET 'localhost:9200/?pretty'
    
### First test

    root@ubuntu-xenial:/vagrant# systemctl start logstash.service
    root@ubuntu-xenial:/vagrant# ps aux | grep logstash
        logstash 27235  152 39.8 3444488 404764 ?      SNsl 23:22   0:15 /usr/bin/java -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+DisableExplicitGC -Djava.awt.headless=true -Dfile.encoding=UTF-8 -XX:+HeapDumpOnOutOfMemoryError -Xmx1g -Xms256m -Xss2048k -Djffi.boot.library.path=/usr/share/logstash/vendor/jruby/lib/jni -Xbootclasspath/a:/usr/share/logstash/vendor/jruby/lib/jruby.jar -classpath : -Djruby.home=/usr/share/logstash/vendor/jruby -Djruby.lib=/usr/share/logstash/vendor/jruby/lib -Djruby.script=jruby -Djruby.shell=/bin/sh org.jruby.Main /usr/share/logstash/lib/bootstrap/environment.rb logstash/runner.rb --path.settings /etc/logstash
        root     27270  0.0  0.0  14224   928 pts/0    S+   23:22   0:00 grep --color=auto logstash
    root@ubuntu-xenial:/vagrant# whereis logstash
        logstash: /etc/logstash /usr/share/logstash
    root@ubuntu-xenial:/vagrant# /usr/share/logstash/bin/logstash -e 'input { stdin { } } output { stdout {} }'

[advanced-pipeline](https://www.elastic.co/guide/en/logstash/current/advanced-pipeline.html)

## Install filebeat on client machine (same vagrant box)

    sudo apt-get install filebeat    
    vi /etc/filebeat/filebeat.yml    
        filebeat.prospectors:
        - input_type: log
            paths:
            - /var/log/syslog
        output.logstash:
        hosts: ["localhost:5044"]    
    systemctl start filebeat.service

Create your first pipeline

    vi /etc/logstash/conf.d/localhost_syslog.conf
        input {
            beats {
                port => "5044"
            }
        }
        filter {
            grok {
                match => { "message" => "%{COMBINEDAPACHELOG}"}
            }
            geoip {
                source => "clientip"
            }
        }
        output {
            stdout { codec => rubydebug }
        }
    systemctl restart logstash.service

## Test config
    
    root@ubuntu-xenial:/home/ubuntu# /usr/share/logstash/bin/logstash -f /etc/logstash/conf.d/localhost_syslog.conf --config.test_and_exit
        WARNING: Could not find logstash.yml which is typically located in $LS_HOME/config or /etc/logstash. You can specify the path using --path.settings. Continuing using the defaults
        Could not find log4j2 configuration at path /usr/share/logstash/config/log4j2.properties. Using default config which logs to console
        Configuration OK
        15:48:23.938 [LogStash::Runner] INFO  logstash.runner - Using config.test_and_exit mode. Config Validation Result: OK. Exiting Logstash
    root@ubuntu-xenial:/home/ubuntu#
    
    systemctl restart filebeat.service
    
## Deploy kibana

    sudo apt-get install kibana    
    whereis kibana
        kibana: /etc/kibana /usr/share/kibana
    vi /etc/kibana/kibana.yml
        elasticsearch.url: "http://localhost:9200"

* Set elasticsearch.url to point at your Elasticsearch instance config/kibana.ym

    systemctl start kibana.service

* Run bin/kibana

[kibana](http://localhost:5601)

Control main services

    ubuntu@ubuntu-xenial:~$ 
    ubuntu@ubuntu-xenial:~$ sudo su
    root@ubuntu-xenial:/home/ubuntu# systemctl start elasticsearch.service
    root@ubuntu-xenial:/home/ubuntu# systemctl start logstash.service
    root@ubuntu-xenial:/home/ubuntu# systemctl start filebeat.service
    root@ubuntu-xenial:/home/ubuntu# systemctl start kibana.service
    root@ubuntu-xenial:/home/ubuntu# 

Monitor for startup activity

    Apr 19 21:42:49 ubuntu-xenial su[1779]: pam_systemd(su:session): Cannot create session: Already running in a session
    Apr 19 21:43:01 ubuntu-xenial systemd[1]: Starting Elasticsearch...
    Apr 19 21:43:01 ubuntu-xenial systemd[1]: Started Elasticsearch.
    Apr 19 21:43:08 ubuntu-xenial systemd[1]: Started logstash.
    Apr 19 21:43:15 ubuntu-xenial systemd[1]: Started filebeat.
    Apr 19 21:43:21 ubuntu-xenial systemd[1]: Started Kibana.

Check web instance is available

    root@ubuntu-xenial:/home/ubuntu# curl 'localhost:9200/_cat/indices?v'
        health status index               uuid                   pri rep docs.count docs.deleted store.size pri.store.size
        yellow open   .kibana             Ru4w0KGQRLOa-Ogp5dHk7Q   1   1          2            0     23.4kb         23.4kb
        yellow open   filebeat-2017.04.20 bhSrqJLpQk2RTrJikBjTXw   5   1    1278749            0    152.8mb        152.8mb
        yellow open   logstash-2017.04.20 ez3TSPJZSniovcTPHtd92Q   5   1     586259            0     76.1mb         76.1mb
        yellow open   filebeat-2017.04.19 LvDgezqwSDaVVzg0utKIqg   5   1    3414161            0      407mb          407mb
    root@ubuntu-xenial:/home/ubuntu# 

## nagios and elastic search

[CHECK_NRPE: Received 0 bytes from daemon. Check the remote](https://support.nagios.com/forum/viewtopic.php?f=6&t=32473)

You can also alter the commands in your /usr/local/nagios/etc/nrpe/common.cfg. Some example command definitions are as follows:

    command[check_total_procs]              =/usr/local/nagios/libexec/check_procs -w 150 -c 200
    command[check_for_elasticsearch_proc]   =/usr/local/nagios/libexec/check_procs -a elasticsearch -w 1:1 -c 1:1
    command[check_for_logstash_proc]        =/usr/local/nagios/libexec/check_procs -a logstash -w 2:2 -c 2:2

Start with data on elasticsearch


