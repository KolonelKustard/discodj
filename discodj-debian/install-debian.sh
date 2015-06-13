#!/bin/sh

UNAME=discodj-kiosk

set -e

if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

apt-get install -y curl
curl -sL https://deb.nodesource.com/setup_0.12 | bash -

apt-get install -y openjdk-7-jre

JAVA7=`update-alternatives --list java | grep java.*7`
if [ -z "$JAVA7" ]; then
  echo "Couldn't find Java 7"
  exit 1
fi
update-alternatives --set $JAVA7

echo "deb http://kolonelkustard.github.io/discodj/apt-repo discodj main" > /etc/apt/sources.list.d/discodj.list

apt-get update
apt-get install -y discodj
