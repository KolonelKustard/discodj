#!/bin/sh

GHPAGES_LOCAL=./discodj-gh-pages

if [ -d "$REPO_HOME" ]; then
  git --work-tree=$GHPAGES_LOCAL pull origin gh-pages
else
  git clone -b gh-pages --single-branch https://github.com/KolonelKustard/discodj $GHPAGES_LOCAL
fi
