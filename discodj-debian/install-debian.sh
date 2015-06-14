#!/bin/sh

UNAME=discodj-kiosk
BACKUP=/home/$UNAME/backup

set -e

if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

apt-get install -y curl
curl -sL https://deb.nodesource.com/setup_0.12 | bash -

apt-get install -y openjdk-7-jre hostapd dnsmasq xinit

JAVA7=`update-alternatives --list java | grep java.*7`
if [ -z "$JAVA7" ]; then
  echo "Couldn't find Java 7"
  exit 1
fi
update-alternatives --set $JAVA7

echo "deb http://kolonelkustard.github.io/discodj/apt-repo discodj main" > /etc/apt/sources.list.d/discodj.list

apt-get update
apt-get install --assume-yes --allow-unauthenticated discodj

a2enmod proxy_http
a2enmod rewrite
a2dissite default
a2ensite discodj
a2ensite discodj-kiosk
service apache2 restart

addgroup --system --quiet $UNAME
adduser --system --quiet --ingroup $UNAME --shell /bin/bash --disabled-password $UNAME

mkdir -p $BACKUP
cp /etc/network/interfaces $BACKUP
cp /etc/inittab $BACKUP

echo "#!/bin/sh" > /home/$UNAME/.profile
echo "xinit /usr/share/discodj/player/discodj-kiosk.sh" >> /home/$UNAME/.profile
chmod +x /home/$UNAME/.profile

chown -R $UNAME:$UNAME /home/$UNAME

sed -i -e 's/1:.*:respawn:\/sbin\/getty.*/1:2345:respawn:\/bin\/login -f discodj-kiosk tty1 <\/dev\/tty1 >\/dev\/tty1 2>\&1/g' /etc/inittab

echo "interface=wlan0" > /etc/hostapd/hostapd.conf
echo "driver=nl80211" >> /etc/hostapd/hostapd.conf
echo "ssid=DiscoDJ" >> /etc/hostapd/hostapd.conf
echo "channel=1" >> /etc/hostapd/hostapd.conf

echo 'DAEMON_CONF="/etc/hostapd/hostapd.conf"' > /etc/default/hostapd

echo "address=/#/10.69.69.1" > /etc/dnsmasq.d
echo "dhcp-range=10.69.69.2,10.69.69.254,12h" >> /etc/dnsmasq.d
