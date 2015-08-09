#!/bin/sh

vagrant up
vagrant ssh -c '/vagrant/apt-deploy.sh '$1
vagrant halt
