#!/bin/sh

export LOG4JS_CONFIG=/etc/discodj/discodj-kiosk-log4js.json

echo Starting DiscoDJ Player

while true; do
  echo Starting node player
  node /usr/share/discodj/player/index.js
done

echo DiscoDJ Player stopped
