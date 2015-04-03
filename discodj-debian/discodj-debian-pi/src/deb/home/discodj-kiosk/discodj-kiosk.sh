#!/bin/sh

echo Starting DiscoDJ Player

echo Switching screensaver off
xset -dpms
xset s off

echo Hiding cursor
unclutter &

echo Starting window manager
matchbox-window-manager &

while true; do
  echo Starting browser
  chromium --kiosk http://localhost
done

echo DiscoDJ Player stopped
