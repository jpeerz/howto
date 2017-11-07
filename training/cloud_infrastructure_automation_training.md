# Cloud Infrastructure Automation Training

### Overview

This document describes how to achieve few skills on server management job. It focuses on _learn by doing_.

### Required Technical Skills/Tools

* Basic/Advanced knowledge of networking
    * nslookup
    * ifconfig
    * nmap
    * netstat
    * tcpdump
    * uptime
* Experience with basic communication protocols like
    * ftp
    * http
    * ssh
    * telnet
    * snmtp
* service management
* package management
* Advance bash experience, doing tasks like
    * file manipulation (search, move, copy, folder structure)
    * string manipulation (search, regexp, replace)
    * cat , more, less
    * init
    * htop , ps , kill
    * uptime
    * vmstat
    * strace
* Any modern script like
    * ruby
    * python
    * perl
* At least one of next compiled/interpreted languages experience.
    * c/c++
    * java
* Virtual Machine Management
    * any vm manager
* API implementation
    * credentials setup
    * client usage
    * api performance
    * api automation

### Diving Into Automation

In order to improve our skills on server automation we need to excercise the ability of managing and controlling content remotely. Let get started by doing next tasks.

#### Document and Share

1. Before you start please write as you go. Document all knowledge gained during the excercise. Suggestion (Wiki, Markdown, TXT)
2. Create a personal account at any GIT enabled website.
3. Configure git credentials on your local machine and do a documentation backup.
4. Search for any key storage tool online to keep track of every password, secret you create on the way.

#### Command line tool installation

1. Let's install any prefered shell/console application to access a server remotely.
2. Configure local credentials and store secrets safe. Investigate/Read/Document how and where important files are located on the client machine.
3. List the contents of the user folder from a computer under your local network. Document all steps required to get there.

#### Connection and Communication

1. Access remote server from your local shell application.
2. Configure connection to by pass password input (certificates, keys, etc)

#### Server playground setup

1. Subscribe/Configure any remote server of your choice. Suggestions [Openshift](https://manage.openshift.com/), [Heroku](https://signup.heroku.com), [CodeAnyWhere](https://codeanywhere.com/dashboard)
2. Install and configure a web server
3. Publish a static web page on new web server instance
4. Publish a dynamic web page on new web server instance (shows folder contents, date, random content)
5. Add javascript to your web site, use frameworksif you like.
6. Create some REST end-points. Enable security.
7. Watch web server logs: Create a web uri to display self server activity.
8. Spot the error: root cause analisys.
9. Generate a folder structure from a plain text file descriptor.
10. Generate random txt/json/xml files with content downloaded from any website. organice files into folders created previously.
11. Install/configure google maps api. use [getting started with APIs ](https://console.cloud.google.com/apis/dashboard)

#### Machine state backup

1. Define and document how to go back to a previous server state. Investigate about modern tools to do that.

#### Localhost work

1. Setup a local virtual machine to work with remote playground. replicate confguration created previously.
2. Destroy and recreate.
3. Create and configure a AWS account
4. Setup local credentials and install CLI tool.
5. Write server descriptor to deploy basic LAMP machine.

### Other insteresting resources

* [Oracle Cloud](https://myservices.us.oraclecloud.com/mycloud/signup?language=en)
* [CFN designer](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/working-with-templates-cfn-designer-json-editor.html?icmpid=docs_cfn_console_designer)















