#!/bin/sh

if vagrant up; then
  if vagrant ssh -c '/vagrant/apt-deploy.sh '$1; then
    vagrant halt
  else
    vagrant destroy -f
    exit 1
  fi
else
  vagrant destroy -f
  exit 1
fi
